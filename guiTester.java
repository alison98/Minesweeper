import javax.swing.*;
public class guiTester
{    
    public static void main(String[] args)
    {
        int size;
        Object[] levels = {"Easy", "Medium", "Hard"};
        //use GUI to take user input for difficulty
        Object selected = JOptionPane.showInputDialog(null, "Choose level", null, -1, null, levels, levels[0]);
        //set board size based on selection
        if(selected.equals("Easy"))
            size = 10;
        else if(selected.equals("Medium"))
            size = 15;
        else
            size = 20;
        Minesweeper mines = new Minesweeper(size);
        //allow game code to run
        while(!mines.isWon() && !mines.isLost())
        {

        }
        //create ending message
        String greeting;
        if(mines.isLost())
            greeting = "Better luck next time!";
        else
            greeting = "Congratulations!";
        //create ending GUI
        int x = JOptionPane.showConfirmDialog(null, greeting + "\nWould you like to play again?", null, JOptionPane.YES_NO_OPTION);
        //if no, close
        if(x == JOptionPane.NO_OPTION)
            System.exit(0);
        //if yes, recursively call the main method (probably not the best way to do that)
        else
        {
            mines.exit();
            mines = null;
            main(args);
        }
    }
}