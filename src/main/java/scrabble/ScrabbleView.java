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

/**
* 
*/
public class ScrabbleView extends JPanel{

    private JLabel messageLabel;
    private ScrabbleGame game;
    private PositionAwareButton[][] buttons;
    private JPanel buttonPanel;
    private GameUI root;
    

    public ScrabbleView(int wide, int tall, GameUI gameFrame, String loadStr){
        // call the superclass constructor
        super();    
        setLayout(new BorderLayout());
        root = gameFrame;

        // instantiate the controller
        if (loadStr != null){
            setGameController(new ScrabbleGame(loadStr));
        } else{
            setGameController(new ScrabbleGame());
        }

        // make a new label to store messages
        messageLabel = new JLabel("Welcome to Scrabble");
        add(messageLabel, BorderLayout.NORTH);  // Messages go on top   
        add(makeGameManageButtons(), BorderLayout.EAST);
        add(makeButtonGrid(tall,wide), BorderLayout.CENTER);
        updateView();
    }

    public void setGameController(ScrabbleGame controller){
        this.game = controller;
    
    }
    
    private JPanel makeGameManageButtons(){
        JPanel gameManage = new JPanel();
        gameManage.setLayout(new BoxLayout(gameManage, BoxLayout.Y_AXIS));
        gameManage.add(makeNewGameButton());
        gameManage.add(makeSaveGameButton());
        gameManage.add(makeLoadGameButton());
        gameManage.add(makeExitGameButton());
        return gameManage;
    }

    private JButton makeNewGameButton(){
        JButton button = new JButton("New Game");
        button.addActionListener(e->newGame());
        return button;
    }

    private JButton makeSaveGameButton(){
        JButton button = new JButton("Save Game");
        button.addActionListener(e->saveGame());
        return button;
    }

    private JButton makeLoadGameButton(){
        JButton button = new JButton("Load Game");
        button.addActionListener(e->loadGame());
        return button;
    }

    private JButton makeExitGameButton(){
        JButton button = new JButton("Exit Game");
        button.addActionListener(e->exitGame());
        return button;
    }

    private JPanel makeButtonGrid(int tall, int wide){
        JPanel panel = new JPanel();
        buttons = new PositionAwareButton[tall][wide];
        panel.setLayout(new GridLayout(wide, tall));
        for (int y=0; y<wide; y++){
            for (int x=0; x<tall; x++){ 
                //Create buttons and link each button back to a coordinate on the grid
                buttons[y][x] = new PositionAwareButton();
                buttons[y][x].setAcross(x+1); //made the choice to be 1-based
                buttons[y][x].setDown(y+1);
                buttons[y][x].addActionListener(e->{
                                        enterNumber(e);
                                        checkGameState();
                                        });
                panel.add(buttons[y][x]);
            }
        }
        return panel;
    }

/* controller methods start here */

    private void checkGameState(){
        int selection= 0;
        JOptionPane gameOver = new JOptionPane();
        if(game.isDone()){
            selection = gameOver.showConfirmDialog(null,
            gameOverMessage(), "Play Again?", JOptionPane.YES_NO_OPTION);
            if(selection == JOptionPane.NO_OPTION){
                root.start();
            } else{
                newGame();
            }
        }
    }

    private String gameOverMessage(){
        if (game.getWinner() == 0){
            return "Tie Game!";
        } else if (game.getWinner() == 1){
            return "Odds win!";
        } else{
            return "Evens win!";
        }
    }

    protected void updateView(){
        //update the labels on the buttons according to the model
        for (int y=0; y<game.getHeight(); y++){
            for (int x=0; x<game.getWidth(); x++){  
                buttons[y][x].setText(game.getCell(buttons[y][x].getAcross(),buttons[y][x].getDown())); 
            }
        }

    }

    protected void newGame(){
        game.newGame();
        updateView();
    }

    protected void saveGame(){
        String saveStr = game.getStringToSave();

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

    protected void exitGame(){
        root.start();
    }


    private void enterNumber(ActionEvent e){
        //get input from user
        String num = JOptionPane.showInputDialog(enterNumberMessage()); 
        
        //send input to game and update view
        PositionAwareButton clicked = ((PositionAwareButton)(e.getSource()));
        try{
            game.takeTurn(clicked.getAcross(), clicked.getDown(),num);
            clicked.setText(game.getCell(clicked.getAcross(),clicked.getDown()));
        } catch (RuntimeException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
            enterNumber(e);
        }
    }

    private String enterNumberMessage(){
        if (game.getCurrPlayer() == 1){
            return "Input an odd number";
        }
        return "Input an even number";
    }

}
