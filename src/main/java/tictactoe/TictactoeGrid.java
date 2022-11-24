package tictactoe;

import java.util.Iterator;

public class TictactoeGrid extends boardgame.Grid{

    public TictactoeGrid(){
        super(3,3);
    }

    /**
     * Returns nice string representation of grid
     */
    @Override
    public String getStringGrid(){
        Iterator<String> iter = iterator();
        String strGrid = "";
        String currCell;

        for (int down = 1; down <= 3; down++){
            for (int across = 1; across <= 3; across++){
                currCell = iter.next();
                if (currCell == " "){
                    strGrid += ((down-1)*3 + (across)) + " ";
                } else{
                    strGrid += currCell + " ";
                }
            }
            strGrid += "\n";
        }
        return strGrid;
    }

    /** 
     * Check if grid is empty
     * @return true if grid is empty, false otherwise
     */
    public boolean isEmpty(){
        Iterator<String> iter = iterator();

        while (iter.hasNext()){
            String cell = iter.next();
            if (cell != " "){
                return false;
            }           
        }
        return true;
    }

    /** 
     * Facilitates the conversion of ScrabbleGrid into a saveable string
     * @return a string representation of the scrabble board that can be saved to CSV
     */
    public String parseBoardIntoString(char mostRecentTurn){
        Iterator<String> iter = iterator();
        String csvStr = "";
        int i=0;

        csvStr += String.valueOf(mostRecentTurn) + "\n";

        while (iter.hasNext()){
            String cell = iter.next();
            i++;
            if (cell == " "){
                cell = "";
            }

            if(i == getWidth()){
                csvStr += cell + "\n";
                i = 0;
            } else{
                csvStr += cell + ",";
            }
            
        }
        return csvStr;
    }

    /** 
     * Facilitates the conversion string toParse into an instance of ScrabbleGrid
     * @param toParse csv string that represents the state of the ScrabbleGrid
     * @return 'O' or 'E' depending on which player had the last turn
     */
 
    public char parseStringIntoBoard(String toParse){
        char currPlayer = toParse.charAt(0);  
        String[] toParseArray = toParse.split("\n", 0);
        validateStringToParse(toParseArray);
        
        for (int down=1; down<=3; down++){
            parseLineIntoBoard(toParseArray[down], down);
        }
        return currPlayer;
    }

    private void validateStringToParse(String[] toParseArray){
        validateLineCount(toParseArray);
        validateLineLength(toParseArray);
        validateLineFormat(toParseArray);
        validatePlayerTurns(toParseArray);
    }

    private void validateLineCount(String[] toParseArray){
        if (toParseArray.length != 4){
            throw new RuntimeException("CSV format should be four lines");
        }
    }

    private void validateLineLength(String[] toParseArray){
        if (toParseArray[0].length() != 1){
            throw new RuntimeException("Line 1 should only be 1 character");
        }

        for (int i=1; i<3; i++){
            if (toParseArray[i].length() < 2){
                throw new RuntimeException("Line " + (i+1) + " should be 2 or more characters");
            } else if (toParseArray[i].length() > 5){
                throw new RuntimeException("Line " + (i+1) + " should be 5 or less characters");
            }
        }
    }

    private void validateLineFormat(String[] toParseArray){
        validateTurnChar(toParseArray[0].charAt(0));
        for (int i=1; i<3; i++){
            if (toParseArray[i].length() == 2){
                validateLineWithLength2(toParseArray[i], i);
            } else if (toParseArray[i].length() == 3){
                validateLineWithLength3(toParseArray[i], i);
            } else if (toParseArray[i].length() == 4){
                validateLineWithLength4(toParseArray[i], i);
            } else{
                validateLineWithLength5(toParseArray[i], i);
            }
        }
    }

    private void validateTurnChar(char c){
        if ((c != 'X') && (c != 'O')){
            throw new RuntimeException("Line 1 char 1 should be 'X' or 'O'");
        }
    }

    private void validateLineWithLength2(String line, int lineNum){
        if (line.charAt(0) != ',' || line.charAt(1) != ','){
            throw new RuntimeException("Invalid line format for line " + (lineNum+1));
        }
    }

    private void validateLineWithLength3(String line, int lineNum){
        int commaCount = 0;
        int playerSymbolCount = 0;
        for (int i=0; i<3; i++){
            if (isValidPlayerSymbol(line.charAt(i))){
                playerSymbolCount++;
            } else if (line.charAt(i) == ','){
                commaCount++;
            } else{
                throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + (i+1));
            }
        }
        if (commaCount != 2 || playerSymbolCount != 1){
            throw new RuntimeException("Invalid line format for line " + (lineNum+1));
        }   
    }

    private void validateLineWithLength4(String line, int lineNum){
        int commaCount = 0;
        int playerSymbolCount = 0;
        for (int i=0; i<4; i++){
            if (isValidPlayerSymbol(line.charAt(i))){
                playerSymbolCount++;
            } else if (line.charAt(i) == ','){
                commaCount++;
            } else{
                throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + (i+1));
            }
        }
        if (commaCount != 2 || playerSymbolCount != 2){
            throw new RuntimeException("Invalid line format for line " + (lineNum+1));
        }   
    }

    private void validateLineWithLength5(String line, int lineNum){
        if (!isValidPlayerSymbol(line.charAt(0))){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 1);
        } else if (line.charAt(1) != ','){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 2);
        } else if (!isValidPlayerSymbol(line.charAt(2))){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 3);
        } else if (line.charAt(3) != ','){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 4);
        } else if (!isValidPlayerSymbol(line.charAt(4))){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 5);
        }
    }

    private boolean isValidPlayerSymbol(char c){
        if ((c == 'X' || c == 'O')){
            return true;
        }
        return false;
    }

    private void validatePlayerTurns(String[] toParseArray){
        int xCount = 0;
        int oCount = 0;
        String currLine;
        char currChar;
        char mostRecentTurn = toParseArray[0].charAt(0);

        for (int i=1; i<=3; i++){
            currLine = toParseArray[i];
            for (int j=0; j<currLine.length(); j++){
                currChar = currLine.charAt(j);
                if (currChar == 'X'){
                    xCount++;
                } else if (currChar == 'O'){
                    oCount++;
                }
            }
        }
        if (mostRecentTurn == 'X'){
            if ((xCount - 1) != oCount){
                throw new RuntimeException("Invalid game. X should have 1 more symbol on board than O");
            }
        } else{
            if (xCount != oCount){
                throw new RuntimeException("Invalid game. X should have same amount of symbols as O");
            }
        }
    }

    private void parseLineIntoBoard(String lineToParse, int down){
        if (lineToParse.length() == 2){
            setLineEmpty(down);
        } else if (lineToParse.length() == 3){
            setLineSingle(lineToParse, down);
        } else if (lineToParse.length() == 4){
            setLineDouble(lineToParse, down);
        } else{
            setLineTriple(lineToParse, down);
        }
    }

    private void setLineEmpty(int down){
        for (int across=1; across<=3; across++){
            setValue(across, down, " ");
        }
    }

    private void setLineSingle(String lineToParse, int down){
        if (lineToParse.charAt(0) != ','){
            setValue(1, down, String.valueOf(lineToParse.charAt(0)));
            setValue(2, down, " ");
            setValue(3, down, " ");
        } else if (lineToParse.charAt(1) != ','){
            setValue(1, down, " ");
            setValue(2, down, String.valueOf(lineToParse.charAt(1)));
            setValue(3, down, " ");
        } else {
            setValue(1, down, " ");
            setValue(2, down, " ");
            setValue(3, down, String.valueOf(lineToParse.charAt(2)));
        }
    }

    private void setLineDouble(String lineToParse, int down){
        if (lineToParse.charAt(0) == ','){
            setValue(1, down, " ");
            setValue(2, down, String.valueOf(lineToParse.charAt(1)));
            setValue(3, down, String.valueOf(lineToParse.charAt(3)));  
        } else if (lineToParse.charAt(3) == ','){
            setValue(1, down, String.valueOf(lineToParse.charAt(0)));
            setValue(2, down, String.valueOf(lineToParse.charAt(2)));
            setValue(3, down, " ");  
        } else{
            setValue(1, down, String.valueOf(lineToParse.charAt(0)));
            setValue(2, down, " ");
            setValue(3, down, String.valueOf(lineToParse.charAt(3)));
        }
    }

    private void setLineTriple(String lineToParse, int down){
        setValue(1, down, String.valueOf(lineToParse.charAt(0)));
        setValue(2, down, String.valueOf(lineToParse.charAt(2)));
        setValue(3, down, String.valueOf(lineToParse.charAt(4)));
    }
}
