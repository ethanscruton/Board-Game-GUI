package scrabble;

/**
 * This class represents the board of a 3x3 game of number scrabble.
 * The class provides methods for updating the board and checking if the board contains a winner.
 */
public class ScrabbleGame extends boardgame.BoardGame implements boardgame.Saveable{
    private int currPlayer;
    private int winner;

    /*---------------------------------------------------------------------
     * Constructors
     --------------------------------------------------------------------*/  

    /**
     * Constructor for new scrabble game
     */
    public ScrabbleGame(){
        super(3,3);
        setGrid(new ScrabbleGrid());
        currPlayer = 1;
        winner = -1;
    }

    /**
     * Constructor for loaded scrabble game
     */
    public ScrabbleGame(String toLoad){
        super(3,3);
        setGrid(new ScrabbleGrid());
        currPlayer = 1;
        loadSavedString(toLoad);
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
            return 'O';
        }
        return 'E';
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
        if (isValidStringInput(input)){
            return takeTurn(across, down, Integer.parseInt(input));
        }
        return false;
    }

    private boolean isValidStringInput(String input){
        try{
            int num = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e){
            throw new RuntimeException("Enter an integer");
        }
    }

    /** 
     * Facilitates the placement of an input on the board
     *  with integer input. Validates input prior to placing the input value. 
     *  Throws exceptions when validating input.
     * @param across across index, 1 based
     * @param down  down index, 1 based
     * @param input  int input from game
     * @return boolean  returns true if input was placed false otherwise
     */    
    @Override
    public boolean takeTurn(int across, int down, int input){
        if (isValidIntInput(input)){
            setValue(across,down,input);
            checkForWinner();
            setCurrPlayer((getCurrPlayer() % 2) + 1);
            return true;
        }
        return false;
    }

    // TO ADD: exception class
    private boolean isValidIntInput(int input){ // throws OutOfRangeException, OddEvenException, AlreadyUsedException {
        
        if (input < 0 || input > 9){
            throw new RuntimeException("Input out of range");
        } else if (boardContains(input)){
            throw new RuntimeException(input + " is already used");
        } else if ((input % 2) != (currPlayer % 2)){
            String currPlayerStr;
            if (currPlayer == 1){
                currPlayerStr = "odd";
            } else{
                currPlayerStr = "even";
            }
            throw new RuntimeException("Select an " + currPlayerStr + " number");
        }
        return true;
    }

    private boolean boardContains(int input){
        String currCell = getNextValue();
        boolean retVal = false;
        while (currCell != null){
            if ((currCell != " ") && (input == Integer.parseInt(currCell))){
                retVal = true;
            }
            currCell = getNextValue();
        }
        return retVal;
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
        for (int row = 1; row <=3; row++){
            if (sumRow(row) == 15){
                return true;
            }
        }
        return false;
    }

    private int sumRow(int row){
        return getCellInt(1,row) + getCellInt(2,row) + getCellInt(3,row);
    }

    private int getCellInt(int across, int down){
        String cell = getCell(across, down);
        if (cell == " "){
            return 0;
        }
        return Integer.parseInt(cell);
    }

    private boolean verticalWinner(){
        for (int col = 1; col <=3; col++){
            if (sumCol(col) == 15){
                return true;
            }
        }
        return false;
    }

    private int sumCol(int col){
        return getCellInt(col,1) + getCellInt(col,2) + getCellInt(col,3);
    }

    private boolean rightDiagonalWinner(){
        if (getCellInt(1,1) + getCellInt(2,2) + getCellInt(3,3) == 15){
            return true;
        }
        return false;
    }

    private boolean leftDiagonalWinner(){
        if (getCellInt(1,3) + getCellInt(2,2) + getCellInt(3,1) == 15){
            return true;
        }
        return false;
    }

    private boolean tieGame(){
        for (int down=1; down<=3; down++){
            for (int across=1; across<=3; across++){
                if (getCell(across, down) == " "){
                    return false;
                }
            }
        }
        return true;
    }

    /*---------------------------------------------------------------------
     * isDone method and helpers
     --------------------------------------------------------------------*/        
    @Override
    public boolean isDone(){
        if (getWinner() != -1){
            return true;
        }
        return false;
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
        ScrabbleGrid myGrid = (ScrabbleGrid)getGrid();
        return myGrid.parseBoardIntoString(playerIntToChar((getCurrPlayer()+1)%2));
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

        ScrabbleGrid myGrid = (ScrabbleGrid)getGrid();

        playerChar = myGrid.parseStringIntoBoard(saved);
        // set current player to be opposite of most recent turn
        setCurrPlayer((playerCharToInt(playerChar) + 1) % 2);
    }

    private int playerCharToInt(char playerChar){
        if (playerChar == 'O'){
            return 1;
        }
        return 2;
    }

    private char playerIntToChar(int playerInt){
        if (playerInt == 1){
            return 'O';
        }
        return 'E';
    }


    public boolean isBoardEmpty(){
        return ((ScrabbleGrid)getGrid()).isEmpty();
    }

}