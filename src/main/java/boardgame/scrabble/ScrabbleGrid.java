package scrabble;

import boardgame.Grid;
import java.util.Iterator;

public class ScrabbleGrid extends boardgame.Grid{
    private String line = null;
    private Character cellWall = '|';
    private static int cellWidth = 3;

    public ScrabbleGrid(){
        super(3,3);
        makeGridLine();
    }

    private void makeGridLine(){
        line = "";
        for(int j=0; j<getWidth()*cellWidth; j++){
            line +="-";
            }
    }

    @Override
    public String getStringGrid(){
        if(line == null){
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
                toPrint = toPrint + "\n" + line + "\n";
                i = 0;
            }

        }
        return toPrint;
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
        
        for (int down=1; down<=3; down++){
            parseLineIntoBoard(toParseArray[down], down);
        }
        return currPlayer;
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
        } else if (lineToParse.charAt(2) == ','){
            setValue(1, down, String.valueOf(lineToParse.charAt(0)));
            setValue(2, down, " ");
            setValue(3, down, String.valueOf(lineToParse.charAt(3)));
        } else{
            setValue(1, down, String.valueOf(lineToParse.charAt(0)));
            setValue(2, down, String.valueOf(lineToParse.charAt(2)));
            setValue(3, down, " ");  
        }
    }

    private void setLineTriple(String lineToParse, int down){
        setValue(1, down, String.valueOf(lineToParse.charAt(0)));
        setValue(2, down, String.valueOf(lineToParse.charAt(1)));
        setValue(3, down, String.valueOf(lineToParse.charAt(2)));
    }
}
