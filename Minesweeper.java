/*
 * Alison Garrity
 * AP computer science final project, June 2016
 * Assignment: learn something you don't already know
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Minesweeper implements MouseListener
{
    private String[][] board; //array of board numbers
    private boolean[][] isShown;
    private JButton[][] buttons;
    private JLabel[][] labels; //displays the numbers in different colors
    private JFrame window = new JFrame("Minesweeper"); //main window
    private boolean lost;
    private int length; //size of board
    private ImageIcon flag = new ImageIcon("C:\\Users\\nosil\\Documents\\flag.png");
    private ImageIcon q = new ImageIcon("C:\\Users\\nosil\\Documents\\q.png");
    private JFrame counterFrame = new JFrame(); //secondary window
    private int mineCounter; //number of mines yet to be marked
    private JLabel counterLabel = new JLabel("", SwingConstants.CENTER); //displays mineCounter
    private JLabel left = new JLabel("Mines left:");
    public void mouseClicked(MouseEvent e)
    {

    }

    public void mousePressed(MouseEvent e)
    {

    }

    public void mouseReleased(MouseEvent e)
    {
        Component a = e.getComponent();
        String command = a.getName();
        int pos = command.indexOf(" ");
        //find corrdinates of button clicked
        int i = Integer.parseInt(command.substring(0, pos));
        int j = Integer.parseInt(command.substring(pos + 1));
        if(!buttons[i][j].isEnabled())
            return;
        //if left click, select button
        if(SwingUtilities.isLeftMouseButton(e))
        {
            if(labels[i][j] != null && labels[i][j].getIcon() == flag)
                return;
            else
                select(i, j);
        }
        //if right click, switch the icon displayed
        else if(SwingUtilities.isRightMouseButton(e))
        {    
            if(labels[i][j] == null)
                setImage(i, j, "!");
            else if(labels[i][j].getIcon() == flag)
                setImage(i, j, "?");
            else
                setImage(i, j, "");
        }
        //count the number of mines marked
        int counter = 0;
        for(int num1 = 1; num1 < length + 1; num1++)
        {
            for(int num2 = 1; num2 < length + 1; num2++)
            {
                if(labels[num1][num2] != null && labels[num1][num2].getIcon() == flag && labels[num1][num2].isVisible())
                    counter++;
            }
        }
        counterLabel.setText(mineCounter - counter + "");
    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    public Minesweeper(int l)
    {
        length = l;
        //initialize arrays to have a border of 1 to avoid special algorithms for sides and corners
        board = new String[length + 2][length + 2];
        isShown = new boolean[length + 2][length + 2];
        buttons = new JButton[length + 2][length + 2];
        labels = new JLabel[length + 2][length + 2];
        //1 in every 7 squares is a mine
        mineCounter = (int)(Math.pow(length, 2) / 7);
        //initialize main window
        GridLayout grid = new GridLayout(length, 10);
        window.setLayout(grid);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(50 * length, 50 * length);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
        //initialize JButtons
        for(int i = 0; i < length + 2; i++)
        {
            for(int j = 0; j < length + 2; j++)
            {
                buttons[i][j] = new JButton();
                buttons[i][j].setName(i + " " + j);
                buttons[i][j].addMouseListener(this);
                if(i != 0 && j != 0 && i != length + 1 && j != length + 1)
                    window.add(buttons[i][j]);
            }
        }
        //initialize secondary window
        counterFrame.setVisible(true);
        counterFrame.setLocation(10, 10);
        counterFrame.setSize(200, 200);
        counterLabel.setSize(100, 20);
        left.setSize(200, 20);
        left.setFont(left.getFont().deriveFont(24.0f));
        left.setLocation(35, 30);
        counterFrame.setResizable(false);
        counterFrame.add(left);
        counterLabel.setText(mineCounter + "");
        counterFrame.add(counterLabel);
        counterLabel.setFont(counterLabel.getFont().deriveFont(64.0f));
        //choose a random spot for mine and place it there if the square is empty
        int counter = (int)(Math.pow(length, 2) / 7);
        while(counter > 0)
        {
            int random1 = (int)(length * Math.random() + 1);
            int random2 = (int)(length * Math.random() + 1);
            if(board[random1][random2] == null)
            {
                board[random1][random2] = "X";
                counter--;
            }
        }
        //count the number of mines around each square; create board
        for(int i = 1; i < length + 1; i++)
        {
            for(int j = 1; j < length + 1; j++)
            {
                if(board[i][j] == null)
                {
                    int counter2 = 0;
                    if(board[i][j - 1] != null && board[i][j - 1].equals("X"))
                        counter2++;
                    if(board[i + 1][j - 1] != null && board[i + 1][j - 1].equals("X"))
                        counter2++;
                    if(board[i + 1][j] != null && board[i + 1][j].equals("X"))
                        counter2++;
                    if(board[i + 1][j + 1] != null && board[i + 1][j + 1].equals("X"))
                        counter2++;
                    if(board[i][j + 1] != null && board[i][j + 1].equals("X"))
                        counter2++;
                    if(board[i - 1][j + 1] != null && board[i - 1][j + 1].equals("X"))
                        counter2++;
                    if(board[i - 1][j] != null && board[i - 1][j].equals("X"))
                        counter2++;
                    if(board[i - 1][j - 1] != null && board[i - 1][j - 1].equals("X"))
                        counter2++;

                    if(counter2 != 0)
                        board[i][j] = counter2 + "";
                    else
                        board[i][j] = " ";
                }
            }
        }
    }

    public void select(int i, int j)
    {
        //base case
        if(board[i][j] == null)
            return;
        isShown[i][j] = true;
        buttons[i][j].setEnabled(false);
        //if mine clicked, end game and reveal board
        if(board[i][j].equals("X"))
        {
            lost = true;
            counterFrame.setVisible(false);
            for(int a = 1; a < length + 1; a++)
            {
                for(int b = 1; b < length + 1; b++)
                {
                    if(board[a][b].equals("X"))
                    {
                        buttons[a][b].setEnabled(false);
                        setImage(a, b, "X");
                    }
                    else
                        setImage(a, b, " ");
                }
            }
        }
        //if '0' clicked, use recursion to open up all surrounding empties and one layer of numbers
        else if(board[i][j].equals(" "))
        {
            if(board[i][j - 1] != null && board[i][j - 1].equals(" ") && !isShown[i][j - 1])
                select(i, j - 1);
            else
            {
                buttons[i][j - 1].setEnabled(false);
                if(!isShown[i][j - 1])
                    setNum(i, j - 1);
            }
            if(board[i - 1][j - 1] != null && board[i - 1][j - 1].equals(" ") && !isShown[i - 1][j - 1])
                select(i - 1, j - 1);
            else
            {
                buttons[i - 1][j - 1].setEnabled(false);
                if(!isShown[i - 1][j - 1])
                    setNum(i - 1, j - 1);
            }
            if(board[i - 1][j] != null && board[i - 1][j].equals(" ") && !isShown[i - 1][j])
                select(i - 1, j);
            else
            {
                buttons[i - 1][j].setEnabled(false);
                if(!isShown[i - 1][j])
                    setNum(i - 1, j);
            }
            if(board[i - 1][j + 1] != null && board[i - 1][j + 1].equals(" ") && !isShown[i - 1][j + 1])
                select(i - 1, j + 1);
            else
            {
                buttons[i - 1][j + 1].setEnabled(false);
                if(!isShown[i - 1][j + 1])
                    setNum(i - 1, j + 1);
            }
            if(board[i][j + 1] != null && board[i][j + 1].equals(" ") && !isShown[i][j + 1])
                select(i, j + 1);
            else
            {
                buttons[i][j + 1].setEnabled(false);
                if(!isShown[i][j + 1])
                    setNum(i, j + 1);
            }
            if(board[i + 1][j + 1] != null && board[i + 1][j + 1].equals(" ") && !isShown[i + 1][j + 1])
                select(i + 1, j + 1);
            else
            {
                buttons[i + 1][j + 1].setEnabled(false);
                if(!isShown[i + 1][j + 1])
                    setNum(i + 1, j + 1);
            }
            if(board[i + 1][j] != null && board[i + 1][j].equals(" ") && !isShown[i + 1][j])
                select(i + 1, j);
            else
            {
                buttons[i + 1][j].setEnabled(false);
                if(!isShown[i + 1][j])
                    setNum(i + 1, j);
            }
            if(board[i + 1][j - 1] != null && board[i + 1][j - 1].equals(" ") && !isShown[i + 1][j - 1])
                select(i + 1, j - 1);
            else
            {
                buttons[i + 1][j - 1].setEnabled(false);
                if(!isShown[i + 1][j - 1])
                    setNum(i + 1, j - 1);
            }
            if(labels[i][j] != null && (labels[i][j].getIcon() == flag || labels[i][j].getIcon() == q))
                labels[i][j].setVisible(false);
        }
        //if number clicked, reveal number
        else
        {
            buttons[i][j].setEnabled(false);
            setNum(i, j);
        }
    }

    public void setNum(int i, int j)
    {
        //base case
        if(labels[i][j] != null)
            labels[i][j].setVisible(false);
        //create JLabel with desired number
        labels[i][j] = new JLabel(board[i][j], SwingConstants.CENTER);
        if(board[i][j] == null)
            return;
        //color JLabel based on number
        switch(board[i][j])
        {
            case "1":
            labels[i][j].setForeground(new Color(200, 0, 200));//purple
            break;
            case "2":
            labels[i][j].setForeground(new Color(0, 150, 0));//green
            break;
            case "3":
            labels[i][j].setForeground(Color.RED);
            break;
            case "4":
            labels[i][j].setForeground(Color.BLUE);
            break;
            case "5":
            labels[i][j].setForeground(new Color(255, 127, 0));//orange
            break;
            case "6":
            labels[i][j].setForeground(Color.RED);
            break;
            case "7":
            labels[i][j].setForeground(Color.MAGENTA);
            break;
            case "8":
            labels[i][j].setForeground(new Color(0, 0, 255, 150));//light blue
            break;
            default:
            if(buttons[i][j].getText().equals(" "))
                buttons[i][j].setText("  ");
            else
                buttons[i][j].setText(" ");
            return;
        }
        buttons[i][j].add(labels[i][j]);
        if(buttons[i][j].getText().equals(" "))
            buttons[i][j].setText("  ");
        else
            buttons[i][j].setText(" ");
        isShown[i][j] = true;        
    }

    public void setImage(int i, int j, String letter)
    {
        ImageIcon icon;
        if(labels[i][j] != null)
            labels[i][j].setVisible(false);
        switch(letter)
        {
            case "X":
            icon = new ImageIcon("C:\\Users\\nosil\\Documents\\bomb.png");
            labels[i][j] = new JLabel(icon);
            buttons[i][j].add(labels[i][j]);
            break;
            case "!":
            labels[i][j] = new JLabel(flag);
            buttons[i][j].add(labels[i][j]);
            break;
            case "?":
            labels[i][j] = new JLabel(q);
            buttons[i][j].add(labels[i][j]);
            break;
            case "":
            labels[i][j] = null;
            break;
            default:
            if(labels[i][j] != null && labels[i][j].getIcon() != flag && labels[i][j].getIcon() != q)
                labels[i][j].setVisible(true);
            break;
        }
        //the code breaks if this isn't here and I have no clue why
        if(buttons[i][j].getText().equals(" "))
            buttons[i][j].setText("  ");
        else
            buttons[i][j].setText(" ");
    }

    public boolean isWon()
    {
        //if all the non-mine squares are revealed, the game is over
        //count opened squares
        int counter = 0;
        for(int i = 1; i < length + 1; i++)
        {
            for(int j = 1; j < length + 1; j++)
            {
                if(!isShown[i][j])
                    counter++;
            }
        }
        //reveal board if won
        if(counter == (int)(Math.pow(length, 2) / 7))
        {
            for(int i = 1; i < length + 1; i++)
            {
                for(int j = 1; j < length + 1; j++)
                {
                    buttons[i][j].setEnabled(false);
                    setImage(i, j, board[i][j]);
                }
            }
            counterFrame.setVisible(false);
            return true;
        }
        return false;
    }

    public boolean isLost()
    {
        return lost;
    }

    public void exit()
    {
        window.setVisible(false);
        counterFrame.setVisible(false);
    }
}