package game;
/**
 * 
 */

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import boardgame.Player;

import scrabble.ScrabbleView;

/**
* 
*/
public class GameUI extends JFrame{
    private JPanel homepagePanel;
    private JPanel gameContainer;
    private JPanel mainContainer;
    private JLabel messageLabel;
    private JMenuBar menuBar;
    private Player player1;
    private Player player2;

    public GameUI(String title){
        // call the superclass constructor
        super();    
        // set the size, title and default close of the jframe
        this.setSize(WIDTH, HEIGHT);
        this.setTitle(title);
        makeMenu();
        setJMenuBar(menuBar);
        mainContainer = new JPanel();
        gameContainer = new JPanel();
        homepagePanel = new HomepageView(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // make a new label to store messages
        add(mainContainer);
        
        mainContainer.add(gameContainer, BorderLayout.CENTER);
        mainContainer.add(homepagePanel, BorderLayout.EAST);
        start();
    }

    private JPanel startupMessage(){
        JPanel temp = new JPanel();
        temp.add(new JLabel("time to play some board games!"));
        return temp;
    }

    public void makeMenu(){
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Load");
        JMenuItem item = new JMenuItem("Scrabble");
        menu.add(item);
        menuBar.add(menu);
        item.addActionListener(e->chooseScrabbleToLoad());
    }

    public void start(){
        mainContainer.removeAll();
        mainContainer.add(homepagePanel);
        getContentPane().repaint();
        getContentPane().revalidate();
        pack();
    }

    protected String chooseScrabbleToLoad(){
        File scrabbleFolder = new File("./assets/scrabble");
        JFileChooser fc = new JFileChooser(scrabbleFolder);
        Path scrabbleGamePath;
        List<String> lines;
        String loadStr;
        
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            scrabbleGamePath = fc.getSelectedFile().toPath();
            try{
                lines = Files.readAllLines(scrabbleGamePath);
                loadStr = String.join("\n", lines) + "\n";
                return loadStr;
            } catch (IOException ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                                        "Invalid Scrabble File", JOptionPane.ERROR_MESSAGE);
                return null;
            } catch (RuntimeException rex){
                JOptionPane.showMessageDialog(null, rex.getMessage(),
                                        "Invalid Scrabble File", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return null;
    }

    protected void scrabble(){
        JOptionPane loadGame = new JOptionPane();
        int selection;
        String loadStr = null;
        mainContainer.removeAll();
        mainContainer.add(gameContainer);
        gameContainer.removeAll();

        selection = loadGame.showConfirmDialog(null,
            "Load Existing Scrabble Game?", "Load Game", JOptionPane.YES_NO_OPTION);
        if (selection == JOptionPane.YES_OPTION){
            loadStr = chooseScrabbleToLoad();
        }
        gameContainer.add(new ScrabbleView(3,3,this,loadStr));
        getContentPane().repaint();
        getContentPane().revalidate();
        pack();
    }

    protected void tictactoe(String loadStr){
        mainContainer.removeAll();
        mainContainer.add(gameContainer);
        gameContainer.removeAll();
        gameContainer.add(startupMessage());
        getContentPane().repaint();
        getContentPane().revalidate();
        pack();
        JOptionPane.showMessageDialog(null,"Judi didn't make a second game");    
    }

    protected void exitApplication(){
        System.exit(0); // stop program
        dispose(); // close window
        setVisible(false); // hide window
    }
    public static void main(String[] args){
        GameUI example = new GameUI("NxM Games");
        example.setVisible(true);
    } 
}
