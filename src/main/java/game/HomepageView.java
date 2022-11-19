package game;


import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;

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
    }

    private void createScrabbleButton(){
        startScrabbleButton = new JButton("Play Scrabble");
        startScrabbleButton.addActionListener(e->root.scrabble());
    }

    private void createTictactoeButton(){
        startTictactoeButton = new JButton("Play Tictactoe");
        startTictactoeButton.addActionListener(e->root.tictactoe(null));
    }

    private void createExitApplicationButton(){
        exitApplicationButton = new JButton("Exit");
        exitApplicationButton.addActionListener((e->root.exitApplication()));
    }
}
