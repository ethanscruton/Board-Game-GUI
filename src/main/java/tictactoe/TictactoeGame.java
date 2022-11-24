package tictactoe;

/**
 * This class represents the board of a 3x3 game of number scrabble.
 * The class provides methods for updating the board and checking if the board contains a winner.
 */
public class TictactoeGame extends boardgame.BoardGame implements boardgame.Saveable{
    private int currPlayer;
    private int winner;

    /*---------------------------------------------------------------------
     * Constructors
     --------------------------------------------------------------------*/  

    /**
     * Constructor for new scrabble game
     */
    public TictactoeGame(){
        super(3,3);
        setGrid(new TictactoeGrid());
        currPlayer = 1;
        winner = -1;
    }

    /*---------------------------------------------------------------------
     * Accessors
     --------------------------------------------------------------------*/
    public int getCurrPlayer(){
        return currPlayer;
    }

    public int getCurrPlayerChar(){
        if (getCurrPlayer() == 1){
            return 'X';
        }
        return 'O';
    }
    

    @Override
    public int getWinner(){
        return winner;
    }

    /*---------------------------------------------------------------------
     * Mutators
     --------------------------------------------------------------------*/
    private void setCurrPlayer(int pCurrPlayer){
        currPlayer = pCurrPlayer;
    }

    private void setWinner(int pWinner){
        winner = pWinner;
    }

    /*---------------------------------------------------------------------
     * takeTurn method and helpers
     --------------------------------------------------------------------*/    
    /** 
     * Facilitates the placement of an input on the board
     *  with string input. Validates input prior to placing the input value. 
     * @param across across index, 1 based
     * @param down  down index, 1 based
     * @param input  string input from game
     * @return boolean  returns true if input was placed false otherwise
     */
    @Override
    public boolean takeTurn(int across, int down, String input){
        if (isValidStringInput(across, down, input)){
            setValue(across,down,input);
            checkForWinner();
            setCurrPlayer((getCurrPlayer() % 2) + 1);
            return true;
        }
        return false;
    }

    private boolean isValidStringInput(int across, int down, String input){
        if (input.length() != 1){
            throw new RuntimeException("More than one character input");
        } else if ((getCurrPlayer() == 1) && (!input.equals("X"))){
            throw new RuntimeException("X not input");
        } else if ((getCurrPlayer() == 2) && (!input.equals("O"))){
            throw new RuntimeException("O not input");
        } else if (getCell(across,down) != " "){
            throw new RuntimeException("Position already in use");
        }
        return true;
    }

    /** 
     * Facilitates the placement of an input on the board
     *  with integer input. Validates input prior to placing the input value. 
     *  Throws exceptions when validating input.
     * @param across across index, 1 based
     * @param down  down index, 1 based
     * @param input  int input from game
     * @return boolean  returns false always as int should never be input
     */    
    @Override
    public boolean takeTurn(int across, int down, int input){
        if (input == 1){
            takeTurn(across, down, "X");
            return true;
        } else if (input == 2){
            takeTurn(across, down, "O");
            return true;
        } else{
            throw new RuntimeException("Only 1 and 2 are acceptable integers");
        }
    }

    private void checkForWinner(){
        if (horizontalWinner()){
            setWinner(getCurrPlayer());
        } else if (verticalWinner()){
            setWinner(getCurrPlayer());
        } else if (rightDiagonalWinner()){
            setWinner(getCurrPlayer());
        } else if (leftDiagonalWinner()){
            setWinner(getCurrPlayer());
        } else if (tieGame()){
            setWinner(0);
        }
    }

    private boolean horizontalWinner(){
        for (int down = 1; down <= 3; down++){
            if ((getCell(1, down) != " ")
                && (getCell(1, down).equals(getCell(2, down)))
                && (getCell(2, down).equals(getCell(3, down)))){
                return true;
            }
        }
        return false;
    }

    private boolean verticalWinner(){
        for (int across = 1; across <= 3; across++){
            if ((!getCell(across, 1).equals(" "))
                && (getCell(across, 1).equals(getCell(across, 2)))
                && (getCell(across, 2).equals(getCell(across, 3)))){
                return true;
            }
        }
        return false;
    }

    private boolean rightDiagonalWinner(){
        if ((getCell(1,1) != " ")
            && (getCell(1,1).equals(getCell(2,2)))
            && (getCell(2,2).equals(getCell(3,3)))){
            return true;
        }
        return false;
    }

    private boolean leftDiagonalWinner(){
        if ((!getCell(1,3).equals(" "))
            && (getCell(1,3).equals(getCell(2,2)))
            && (getCell(2,2).equals(getCell(3,1)))){
            return true;
        }
        return false;
    }

    private boolean tieGame(){
        for (int down=1; down<=3; down++){
            for (int across=1; across<=3; across++){
                if (!getCell(across, down).equals(" ")){
                    return false;
                }
            }
        }
        return true;
    }

    /*---------------------------------------------------------------------
     * isDone method and helpers
     --------------------------------------------------------------------*/    
    /** Checks if game is done
    * @returns true if winner is not -1, false otherwise
    */      
    @Override
    public boolean isDone(){
        if (getWinner() != -1){
            return true;
        }
        return false;
    }

    /*---------------------------------------------------------------------
     * newGame method and helpers
     --------------------------------------------------------------------*/ 
    /**
     * Resets the game grid, current player, and winner
    */
    @Override
    public void newGame(){
        getGrid().emptyGrid();
        currPlayer = 1;
        winner = -1;
    }
    
    /*---------------------------------------------------------------------
     * getGameState method and helpers
     --------------------------------------------------------------------*/
    // TO ADD: this method
    @Override
    public String getGameStateMessage(){
        return toString();
    }

    /*---------------------------------------------------------------------
     * Saveable implementation
     --------------------------------------------------------------------*/
    /** 
     * Creates a CSV string representation of the board
     * @return CSV string representation of the board
     */ 
    @Override
    public String getStringToSave(){
        TictactoeGrid myGrid = (TictactoeGrid)getGrid();
        return myGrid.parseBoardIntoString(playerIntToChar((getCurrPlayer()%2)+1));
    }

    /** 
     * Loads a CSV string representation of the board into the current instance of ScrabbleGrid
     * @param saved CSV string representation of the board
     */ 
    @Override
    public void loadSavedString(String saved){
        
        //here there should be code to parse the saved string into a board.
        //I would probably write a method in my KakuroBoard class that did the parsing
        // and just pass it the string/

        /* must cast it to get a reference that can use Kakuro grid
        methods*/
        char playerChar;

        TictactoeGrid myGrid = (TictactoeGrid)getGrid();

        playerChar = myGrid.parseStringIntoBoard(saved);
        // set current player to be opposite of most recent turn
        setCurrPlayer((playerCharToInt(playerChar) % 2) + 1);
    }

    private int playerCharToInt(char playerChar){
        if (playerChar == 'X'){
            return 1;
        }
        return 2;
    }

    private char playerIntToChar(int playerInt){
        if (playerInt == 1){
            return 'X';
        }
        return 'O';
    }

    public boolean isBoardEmpty(){
        return ((TictactoeGrid)getGrid()).isEmpty();
    }

}