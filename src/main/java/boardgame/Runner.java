import scrabble.ScrabbleGame;


public class Runner{
    public static void main(String[] args){
        
        ScrabbleGame currGame = new ScrabbleGame();
        currGame.takeTurn(1, 3, 3);
        String saveStr = currGame.getStringToSave();
        System.out.printf("%s", saveStr);
        currGame.loadSavedString("O\n7,,\n2,,\n9,,\n");
        System.out.println(currGame.toString());
        currGame.loadSavedString(saveStr);
        System.out.println(currGame.toString());
    }
}
