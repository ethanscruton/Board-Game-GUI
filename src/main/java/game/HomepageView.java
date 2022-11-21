package game;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Component;

/**
 * This class represents the homepage of the game suite.
 *  Allows user to start Scrabble, Tic Tac Toe, or exit application.
 * Extends JPanel
 */
public class HomepageView extends JPanel{
    private JButton startScrabbleButton;
    private JButton startTictactoeButton;
    private JButton exitApplicationButton;
    private GameUI root;

    public HomepageView(GameUI gameFrame){
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        root = gameFrame;
        
        createScrabbleButton();
        createTictactoeButton();
        createExitApplicationButton();

        add(startScrabbleButton);
        add(startTictactoeButton);
        add(exitApplicationButton);
        setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    private void createScrabbleButton(){
        startScrabbleButton = new JButton("Play Scrabble");
        startScrabbleButton.addActionListener(e->root.scrabble());
    }

    private void createTictactoeButton(){
        startTictactoeButton = new JButton("Play Tictactoe");
        startTictactoeButton.addActionListener(e->root.tictactoe());
    }

    private void createExitApplicationButton(){
        exitApplicationButton = new JButton("Exit");
        exitApplicationButton.addActionListener((e->root.exitApplication()));
    }
}
