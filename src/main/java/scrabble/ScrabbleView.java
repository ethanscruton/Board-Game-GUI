package scrabble;
/**
 *  an example GUI view for an NxM game
 */

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import boardgame.ui.PositionAwareButton;
import boardgame.GameStorage;
import game.GameUI;
import boardgame.ui.BoardGameView;

/**
* 
*/
public class ScrabbleView extends BoardGameView{

    private JLabel messageLabel;
    private ScrabbleGame game;
    private PositionAwareButton[][] buttons;
    private JPanel buttonPanel;
    private GameUI root;
    

    public ScrabbleView(int wide, int tall, GameUI gameFrame, String loadStr){
        // call the superclass constructor
        super(wide, tall, gameFrame, loadStr);    
    }

    protected void instantiateController(String loadStr){
        if (loadStr != null){
            setGameController(new ScrabbleGame(loadStr));
        } else{
            setGameController(new ScrabbleGame());
        }
    }


    protected String gameOverMessage(){
        if (game.getWinner() == 0){
            return "Tie Game!";
        } else if (game.getWinner() == 1){
            return "Odds win!";
        } else{
            return "Evens win!";
        }
    }

    protected void saveGame(){
        if (game.isBoardEmpty()){
            JOptionPane.showMessageDialog(null, "Can't save empty board", "Save error", JOptionPane.ERROR_MESSAGE);
        } else{
            File scrabbleFolder = new File("./assets/scrabble");
            JFileChooser fc = new JFileChooser(scrabbleFolder);
            fc.setDialogTitle("Specify a file to save");
            int userSelect = fc.showSaveDialog(this);
            
            if (userSelect == JFileChooser.APPROVE_OPTION){
                try{
                    Path path = fc.getSelectedFile().toPath();
                    GameStorage.save(game, path);
                } catch (IOException ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Filename", JOptionPane.ERROR_MESSAGE);
                    saveGame();
                }
            }
        }
    }

    protected void loadGame(){
        File scrabbleFolder = new File("./assets/scrabble");
        JFileChooser fc = new JFileChooser(scrabbleFolder);
        Path scrabbleGamePath;
        
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            scrabbleGamePath = fc.getSelectedFile().toPath();
            try{
                GameStorage.load(game, scrabbleGamePath);
            } catch (IOException ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                                        "Invalid Scrabble File", JOptionPane.ERROR_MESSAGE);
            } catch (RuntimeException rex){
                JOptionPane.showMessageDialog(null, rex.getMessage(),
                                        "Invalid Scrabble File", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void enterInput(ActionEvent e){
        //get input from user
        String num = JOptionPane.showInputDialog(enterNumberMessage()); 
        
        //send input to game and update view
        PositionAwareButton clicked = ((PositionAwareButton)(e.getSource()));
        try{
            game.takeTurn(clicked.getAcross(), clicked.getDown(),num);
            clicked.setText(game.getCell(clicked.getAcross(),clicked.getDown()));
        } catch (RuntimeException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String enterNumberMessage(){
        if (game.getCurrPlayer() == 1){
            return "Input an odd number";
        }
        return "Input an even number";
    }

}
