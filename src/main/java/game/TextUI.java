package game;

import tictactoe.TictactoeGame;
import java.util.Scanner;

/**
 * This class represents the UI for a text command line version of tictactoe
 */
public class TextUI {
    private TictactoeGame game;
    private Scanner keyboard;

    public TextUI(){
        game = new TictactoeGame();
        keyboard = new Scanner(System.in);
        start();
    }

    /**
     * Starts a game of tictactoe on the command line
     */
    public void start(){
        printStartGameText();
        while (!game.isDone()){
            showBoard();
            playerTurn();
        }
        showBoard();
        printEndGameText();
    }

    private void showBoard(){
        System.out.println(game.toString());
    }

    private void printStartGameText(){
        System.out.println("Starting Command Line Tic Tac Toe game...\n");
    }

    private void playerTurn(){
        String userInput;
        
        printEnterInputText();
        userInput = keyboard.nextLine();
        if (isValidUserInput(userInput)){
            try{
                addToBoard(userInput);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }      
        } else{
            playerTurn();
        }
    }

    private void printEnterInputText(){
        if (game.getCurrPlayer() == 1){
            System.out.println("X's Turn");
        } else{
            System.out.println("O's Turn");
        }
        System.out.printf("Enter input (1-9): ");
    }

    private boolean isValidUserInput(String userInput){
        try{
            int userInputInt = Integer.parseInt(userInput);
            if ((userInputInt >= 1) && (userInputInt <= 9)){
                return true;
            } else{
                System.out.println("Invalid input");
                return false;
            }

        } catch (NumberFormatException e){
            System.out.printf("Invalid input");
            return false;
        }
    }

    private void addToBoard(String userInput){
        int userInputInt = Integer.parseInt(userInput);
        game.takeTurn(((userInputInt-1) % 3)+1, ((userInputInt-1) / 3)+1, game.getCurrPlayer());
    }

    private void printEndGameText(){
        String endText;
        if (game.getWinner() == 0){
            endText = "Tie Game!";
        } else if (game.getWinner() == 1){
            endText = "X Wins!";
        } else{
            endText = "O Wins!";
        }
        System.out.println(endText);
    }

    public static void main(String[] args){
        new TextUI();
    } 
}
