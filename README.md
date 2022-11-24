# Connect Four

The purpose of the project is to run a boardgame suite using a GUI.

## Description

Upon running the game suite, the user has the option to start a Number Scrabble game, start a Tic Tac Toe game,
or exit the application from the main menu. When starting a game, the user can create a new player,
load an existing player, or play as a Guest for player 1 and player 2 of the game. While playing the game, 
the user has the option to start a new game, save the game, load an existing game, or exit the game to the main menu. 
Upon exiting the game to the main menu, the user can save the player data for player 1 and 2.

In addition the the GUI game suite, a command line version of Tic Tac Toe can also be played.

## Getting Started

### Dependencies

* gradle

### Executing program

* Building the program
```
gradle build
```
```
BUILD SUCCESSFUL in 3s
3 actionable tasks: 3 up-to-date
```
* Getting command line info to run program
```
gradle run
```
```
> Task :run
To run the GUI game suite by jar:
java -jar build/libs/A3.jar

To run the GUI game suite by class:
java -cp build/classes/java/main/ game.GameUI

To run the command line Tic Tac Toe by class:
java -cp build/classes/java/main/ game.TextUI
```
* Running the GUI program
```
java -jar build/libs/A3.jar
```
or
```
java -cp build/classes/java/main/ game.GameUI
```
* Running the command line program
```
java -cp build/classes/java/main/ game.TextUI
```

## Player Save File Format
```
W
L
G

```
Where W is an integer representing wins, L is an integer representing losses, 
and G is an integer representing games played.

## Limitations

No player save/load option while playing Tic Tac Toe/Number Scrabble, only when starting/exiting a game.

User must input their symbol for Tic Tac Toe. Gameplay would be more fluent if they only had to click
the grid location they want to place their turn.

## Author Information

Ethan Scruton
escruton@uoguelph.ca

## Development History
* 1.0
    * A3 submission version
* 0.6
    * Creating TextUI class
* 0.5
    * Working full GUI suite
* 0.4
    * Creating abstract BoardGameView class
* 0.3
    * ScrabbleGame working (with GUI)
* 0.2
    * ScrabbleGame working (no GUI)
* 0.1
    * Initial Release

## Acknowledgments

N/A