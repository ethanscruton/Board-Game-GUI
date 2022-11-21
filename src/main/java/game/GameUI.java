package game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import scrabble.ScrabbleView;
import tictactoe.TictactoeView;

/**
* Class representing the UI for the entire game suite.
* Extends JFrame
*/
public class GameUI extends JFrame{
    private JPanel homepagePanel;
    private JPanel gameContainer;
    private JPanel mainContainer;

    public GameUI(String title){
        // call the superclass constructor
        super();
        // set the title of the game frame
        this.setTitle(title);
        mainContainer = new JPanel();
        gameContainer = new JPanel();
        homepagePanel = new HomepageView(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(mainContainer);
        
        start();
    }

    /**
     * Remove all content from mainContainer and show homepage
     */
    public void start(){
        mainContainer.removeAll();
        mainContainer.add(homepagePanel);
        updateContentArrangement();
    }

    /**
     * Remove all content from mainContainer and start Scrabble game
     */
    protected void scrabble(){
        mainContainer.removeAll();
        mainContainer.add(gameContainer);
        gameContainer.removeAll();

        gameContainer.add(new ScrabbleView(3,3,this));
        updateContentArrangement();
    }

    /**
     * Remove all content from mainContainer and start Tic Tac Toe game
     */
    protected void tictactoe(){
        mainContainer.removeAll();
        mainContainer.add(gameContainer);
        gameContainer.removeAll();
        gameContainer.add(new TictactoeView(3,3,this));
        updateContentArrangement();
    }

    /**
     * Exit the application
     */
    protected void exitApplication(){
        System.exit(0); // stop program
        dispose(); // close window
        setVisible(false); // hide window
    }

    private void updateContentArrangement(){
        getContentPane().repaint();
        getContentPane().revalidate();
        pack();
    }

    /**
     * Main method for running GameUI
     * @param args
     */
    public static void main(String[] args){
        GameUI example = new GameUI("NxM Games");
        example.setVisible(true);
    } 
}
