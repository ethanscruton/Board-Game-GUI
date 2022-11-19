package scrabble;

import boardgame.Grid;
import java.util.Iterator;

public class ScrabbleGrid extends boardgame.Grid{
    private String cellFloor = null;
    private Character cellWall = '|';
    private static int cellWidth = 3;

    public ScrabbleGrid(){
        super(3,3);
        makeGridLine();
    }

    private void makeGridLine(){
        cellFloor = "";
        for(int j=0; j<getWidth()*cellWidth; j++){
            cellFloor +="-";
            }
    }

    @Override
    public String getStringGrid(){
        if(cellFloor == null){
            makeGridLine();
        }
        Iterator<String> iter = iterator();
        String toPrint ="";
        int i=0;
        while(iter.hasNext()){
            String cell = iter.next();
            toPrint = toPrint + " "+ cell+ cellWall;
            i++;
            if(i == getWidth()){
                toPrint = toPrint + "\n" + cellFloor + "\n";
                i = 0;
            }

        }
        return toPrint;
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
            } else if (toParseArray[i].length() < 2){
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
        if ((c != 'O') && (c != 'E')){
            throw new RuntimeException("Line 1 char 1 should be 'O' or 'E'");
        }
    }

    private void validateLineWithLength2(String line, int lineNum){
        if (line.charAt(0) != ',' || line.charAt(1) != ','){
            throw new RuntimeException("Invalid line format for line " + (lineNum+1));
        }
    }

    private void validateLineWithLength3(String line, int lineNum){
        int commaCount = 0;
        int digitCount = 0;
        for (int i=0; i<3; i++){
            if (Character.isDigit(line.charAt(i))){
                digitCount++;
            } else if (line.charAt(i) == ','){
                commaCount++;
            } else{
                throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + (i+1));
            }
        }
        if (commaCount != 2 || digitCount != 1){
            throw new RuntimeException("Invalid line format for line " + (lineNum+1));
        }   
    }

    private void validateLineWithLength4(String line, int lineNum){
        int commaCount = 0;
        int digitCount = 0;
        for (int i=0; i<4; i++){
            if (Character.isDigit(line.charAt(i))){
                digitCount++;
            } else if (line.charAt(i) == ','){
                commaCount++;
            } else{
                throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + (i+1));
            }
        }
        if (commaCount != 2 || digitCount != 2){
            throw new RuntimeException("Invalid line format for line " + (lineNum+1));
        }   
    }

    private void validateLineWithLength5(String line, int lineNum){
        if (!Character.isDigit(line.charAt(0))){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 1);
        } else if (line.charAt(1) != ','){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 2);
        } else if (!Character.isDigit(line.charAt(2))){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 3);
        } else if (line.charAt(3) != ','){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 4);
        } else if (!Character.isDigit(line.charAt(4))){
            throw new RuntimeException("Invalid character at line " + (lineNum+1) + " char " + 5);
        }
    }

    private void validatePlayerTurns(String[] toParseArray){
        int oddCount = 0;
        int evenCount = 0;
        String currLine;
        char currChar;
        char mostRecentTurn = toParseArray[0].charAt(0);

        for (int i=1; i<=3; i++){
            currLine = toParseArray[i];
            for (int j=0; j<currLine.length(); j++){
                currChar = currLine.charAt(j);
                if (Character.isDigit(currChar)){
                    if (Character.getNumericValue(currChar) % 2 == 0){
                        evenCount++;
                    } else{
                        oddCount++;
                    }
                }
            }
        }
        if (mostRecentTurn == 'O'){
            if ((oddCount - 1) != evenCount){
                throw new RuntimeException("Invalid game. Odd should have 1 more number on board than even");
            }
        } else{
            if (oddCount != evenCount){
                throw new RuntimeException("Invalid game. Odd should have same amount of numbers as even");
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
