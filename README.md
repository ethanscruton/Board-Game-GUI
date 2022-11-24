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

## Limitations

No player save/load option while playing Tic Tac Toe/Number Scrabble, only when starting/exiting a game.

## Author Information

Ethan Scruton
escruton@uoguelph.ca

## Development History
* 1.0
    * A2 resubmission. Fixed various bugs
* 0.6
    * Removing toString tests
* 0.5
    * Adding toString method for each Class
* 0.4
    * Adding test cases
* 0.3
    * Submission version 1
* 0.2
    * Initial working version
* 0.1
    * Initial Release

## Acknowledgments

N/A