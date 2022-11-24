package boardgame;

/**
 * Class representing a player. Keeps track of wins, losses, and games played.
 * Implements Saveable interface.
 */
public class Player implements Saveable {
    private int wins;
    private int losses;
    private int gamesPlayed;

    public Player(){
        wins = 0;
        losses = 0;
        gamesPlayed = 0;
    }

    public int getWins(){
        return wins;
    }

    public int getLosses(){
        return losses;
    }

    public int getGamesPlayed(){
        return gamesPlayed;
    }

    private void setWins(int pWins){
        wins = pWins;
    }

    private void setLosses(int pLosses){
        losses = pLosses;
    }

    private void setGamesPlayed(int pGamesPlayed){
        gamesPlayed = pGamesPlayed;
    }

    public void incrementWins(){
        wins++;
    }

    public void incrementLosses(){
        losses++;
    }

    public void incrementGamesPlayed(){
        gamesPlayed++;
    }

    /**
     * Implemented Saveable method. Returns a string representation of instance of Player class
     *  that can be saved to file.
     * @returns string representing Player
     */
    public String getStringToSave(){
        return String.format("%d\n%d\n%d\n", wins, losses, gamesPlayed);
    }

    /**
     * Implemented Saveable method. Validates and loads data from toLoad into Player instance.
     * @param toLoad string representing Player
     */
    public void loadSavedString(String toLoad){
        String[] toLoadArray = toLoad.split("\n", 0);
        validateLoadString(toLoadArray);
        setWins(Integer.parseInt(toLoadArray[0]));
        setLosses(Integer.parseInt(toLoadArray[1]));
        setGamesPlayed(Integer.parseInt(toLoadArray[2]));
    }

    private void validateLoadString(String[] toLoadArray){
        validateLineCount(toLoadArray);
        validateLineContents(toLoadArray);
        validateGamesPlayed(toLoadArray);
    }

    private void validateLineCount(String[] toLoadArray){
        if (toLoadArray.length != 3){
            throw new RuntimeException("Load file should have 3 lines");
        }
    }

    private void validateLineContents(String[] toLoadArray){
        String currLine;
        for (int i=0; i<3; i++){
            currLine = toLoadArray[i];
            for (int j=0; j<currLine.length(); j++){
                if (!Character.isDigit(currLine.charAt(j))){
                    throw new RuntimeException("Load file should only contain integers");
                }
            }
        }
    }

    private void validateGamesPlayed(String[] toLoadArray){
        int lWins = Integer.parseInt(toLoadArray[0]);
        int lLosses = Integer.parseInt(toLoadArray[1]);
        int lGamesPlayed = Integer.parseInt(toLoadArray[2]);

        if (lGamesPlayed < (lWins + lLosses)){
            throw new RuntimeException("Games played should be greater or equal wins + losses");
        }
    }
}
