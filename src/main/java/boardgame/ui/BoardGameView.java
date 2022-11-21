package boardgame.ui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import game.GameUI;
import boardgame.BoardGame;
import boardgame.Player;
import boardgame.GameStorage;
import java.io.IOException;

/**
 * Abstract class representing the view for playing a board game.
 * Extends JPanel class.
 */
public abstract class BoardGameView extends JPanel{
    private JLabel gameNameLabel;
    private JLabel playerTurnLabel;
    private BoardGame game;
    private PositionAwareButton[][] buttons;
    private GameUI root;
    private JMenuBar menuBar;
    private Player player1=null;
    private Player player2=null;
    
    /**
     * Create an instance of BoardGameView
     * @param wide the width of the board
     * @param tall the height of the board
     * @param gameFrame the GameUI the board is located in
     */
    public BoardGameView(int wide, int tall, GameUI gameFrame){
        // call the superclass constructor
        super(); 

        // load player data
        loadPlayer(1);
        loadPlayer(2);

        // set root and controller
        root = gameFrame;
        instantiateController();

        // create UI
        setLayout(new BorderLayout());
        makeMenuBar();
        root.setJMenuBar(menuBar);

        gameNameLabel = new JLabel();
        playerTurnLabel = new JLabel("Player 1's Turn");
        add(gameNameLabel, BorderLayout.NORTH);
        add(playerTurnLabel, BorderLayout.SOUTH); 

        add(makeButtonGrid(tall,wide), BorderLayout.CENTER);
        
        updateView();
    }

    /*---------------------------------------------------------------------------------
     *                              Accessor Methods
     ---------------------------------------------------------------------------------*/
    /**
     * Accessor method for game
     * @return game
     */
    public BoardGame getGame(){
        return game;
    }

    /**
     * Accessor method for playerTurnLabel text
     * @return playerTurnLabel text
     */
    public String getPlayerTurnLabel(){
        return playerTurnLabel.getText();
    }

    /**
     * Accessor method for player1
     * @return player1
     */
    public Player getPlayer1(){
        return player1;
    }

    /**
     * Accessor method for player2
     * @return player2
     */
    public Player getPlayer2(){
        return player2;
    }

    /*---------------------------------------------------------------------------------
     *                              Mutator Methods
     ---------------------------------------------------------------------------------*/
    /**
     * Mutator for game
     * @param pGame to set
     */
    public void setGame(BoardGame pGame){
        game = pGame;
    }

    /**
     * Mutator for playerTurnLabel text
     * @param playerNum sets playerTurnLabel to playerNum's turn
     */
    public void setPlayerTurnLabel(int playerNum){
        playerTurnLabel.setText(String.format("Player %d's turn", playerNum));
    }

    /**
     * Mutator for player1
     * @param pPlayer1 to set
     */
    public void setPlayer1(Player pPlayer1){
        player1 = pPlayer1;
    }

    /**
     * Mutator for player2
     * @param pPlayer2 to set
     */
    public void setPlayer2(Player pPlayer2){
        player2 = pPlayer2;
    }

    /**
     * Mutator for gameNameLabel text
     * @param gameName to set
     */
    public void setGameNameLabel(String gameName){
        gameNameLabel.setText(gameName);
    }

    /**
     * Must be overridden if used. Creates an instance of the game controller and sets game to it.
     */
    protected abstract void instantiateController();

    /**
     * Increments wins and games played for playerNum
     * @param playerNum to increment
     */
    public void incrementPlayerWins(int playerNum){
        if (playerNum == 1){
            if (player1 != null){
                player1.incrementWins();
                player1.incrementGamesPlayed();
            }
        } else if (playerNum == 2){
            if (player2 != null){
                player2.incrementWins();
                player2.incrementGamesPlayed();
            }
        }
    }

    /**
     * Increments losses and games played for playerNum
     * @param playerNum to increment
     */
    public void incrementPlayerLosses(int playerNum){
        if (playerNum == 1){
            if (player1 != null){
                player1.incrementLosses();
                player1.incrementGamesPlayed();
            }
        } else if (playerNum == 2){
            if (player2 != null){
                player2.incrementLosses();
                player2.incrementGamesPlayed();
            }
        }
    }

    /**
     * Increments games played for player1 and player2
     */
    public void incrementPlayerTie(){
        player1.incrementGamesPlayed();
        player2.incrementGamesPlayed();
    }

    /**
     * Asks user if they want to load playerNum. If yes, load data for player playerNum from file
     * @param playerNum to load
     */
    protected void loadPlayer(int playerNum){
        JOptionPane loadOption = new JOptionPane();
        int selection;
        selection = loadOption.showConfirmDialog(null,
        String.format("Load player %d?", playerNum), "Load Player", JOptionPane.YES_NO_OPTION);

        if (selection == JOptionPane.YES_OPTION){
            choosePlayerLoad(playerNum);
        }
    }

    private void choosePlayerLoad(int playerNum){
        File playerFolder = new File("./assets/players");
        JFileChooser fc = new JFileChooser(playerFolder);
        Path playerPath;
        Player loadedPlayer = new Player();
        
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            playerPath = fc.getSelectedFile().toPath();
            try{
                GameStorage.load(loadedPlayer, playerPath);
                if (playerNum == 1){
                    setPlayer1(loadedPlayer);
                } else{
                    setPlayer2(loadedPlayer);
                }
            } catch (IOException ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                                        "Invalid Scrabble File", JOptionPane.ERROR_MESSAGE);
                loadPlayer(playerNum);
            } catch (RuntimeException rex){
                JOptionPane.showMessageDialog(null, rex.getMessage(),
                                        "Invalid Scrabble File", JOptionPane.ERROR_MESSAGE);
                loadPlayer(playerNum);
            }
        }
    }

    /**
     * Asks player if they want to save player playerNum. If yes, prompt user to select file to save to.
     * @param playerNum
     */
    protected void savePlayer(int playerNum){
        JOptionPane saveOption = new JOptionPane();
        int selection;
        Player currPlayer;
        if (playerNum == 1){
            currPlayer = getPlayer1();
        } else{
            currPlayer = getPlayer2();
        }
        if (currPlayer != null){
            selection = saveOption.showConfirmDialog(null,
            String.format("Save player %d?", playerNum), "Load Player", JOptionPane.YES_NO_OPTION);
            
            if (selection == JOptionPane.YES_OPTION){
                choosePlayerSave(playerNum);
            }
        }
    }

    private void choosePlayerSave(int playerNum){
        File playerFolder = new File("./assets/players");
        JFileChooser fc = new JFileChooser(playerFolder);
        Player playerToSave;
        fc.setDialogTitle("Specify a file to save");
        int userSelect = fc.showSaveDialog(this);
        
        if (userSelect == JFileChooser.APPROVE_OPTION){
            if (playerNum == 1){
                playerToSave = getPlayer1();
            } else{
                playerToSave = getPlayer2();
            }
            try{
                Path path = fc.getSelectedFile().toPath();
                GameStorage.save(playerToSave, path);
            } catch (IOException ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Filename", JOptionPane.ERROR_MESSAGE);
                savePlayer(playerNum);
            }
        }
    }

    private void makeMenuBar(){
        JMenu fileMenu = new JMenu("File");
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        fileMenu.add(makeNewGameMenuItem());
        fileMenu.add(makeSaveGameMenuItem());
        fileMenu.add(makeLoadGameMenuItem());
        fileMenu.add(makeExitGameMenuItem());
    }
  
    private JMenuItem makeNewGameMenuItem(){
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.addActionListener(e->newGame());
        return menuItem;
    }

    private JMenuItem makeSaveGameMenuItem(){
        JMenuItem menuItem = new JMenuItem("Save");
        menuItem.addActionListener(e->saveGame());
        return menuItem;
    }

    private JMenuItem makeLoadGameMenuItem(){
        JMenuItem menuItem = new JMenuItem("Load");
        menuItem.addActionListener(e->loadGame());
        return menuItem;
    }

    private JMenuItem makeExitGameMenuItem(){
        JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(e->exitGame());
        return menuItem;
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
                                        enterInput(e);
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
            incrementPlayerData();
            selection = gameOver.showConfirmDialog(null,
            gameOverMessage(), "Play Again?", JOptionPane.YES_NO_OPTION);
            if(selection == JOptionPane.NO_OPTION){
                exitGame();
            } else{
                newGame();
            }
        }
    }

    /**
     * Must be overridden if used. Increments player win, loss and games played data according to result
     *  of game.
     */
    protected abstract void incrementPlayerData();

    /**
     * Must be overridden if used. Returns game over message according to result of game.
     * @return String message of game over
     */
    protected abstract String gameOverMessage();

    /**
     * Refreshes the UI game grid to match the controller game grid.
     */
    protected void updateView(){
        //update the labels on the buttons according to the model
        for (int y=0; y<game.getHeight(); y++){
            for (int x=0; x<game.getWidth(); x++){  
                buttons[y][x].setText(game.getCell(buttons[y][x].getAcross(),buttons[y][x].getDown())); 
            }
        }
    }

    /**
     * Starts a new game.
     */
    protected void newGame(){
        game.newGame();
        setPlayerTurnLabel(1);
        updateView();
    }

    /**
     * Must be overridden if used. Saves game in progress to user selected file.
     */
    protected abstract void saveGame();

    /**
     * Must be overridden if used. Saves game in progress to user selected file.
     */
    protected abstract void loadGame();

    protected void exitGame(){
        savePlayer(1);
        savePlayer(2);
        root.start();
    }

    /**
     * Must be overridden if used. Prompt user to input something at clicked on grid location.
     * @param e ActionEvent representing grid location clicked.
     */
    protected abstract void enterInput(ActionEvent e);
}
