---
geometry: margin=1in
---
# PROJECT Design Documentation

## Team Information
* Team name: Team 8
* Team members
  * Quentin Ramos
  * Zane Kitchen Lipski
  * Jaden Kitchen Lipski
  * Spencer Creveling
  * David Pritchard

## Executive Summary

The WebCheckers is a web based application that allows players to log in with a unique alphanumeric username and play 
checkers against other players. The user can also play against AI opponents with varying difficulties and save games 
against opponents to play against them at another time.

### Purpose

> The purpose of this project is to make a WebCheckers application in which people could
> play a game of checkers against other online users, and importantly do the project as
> a group in which we communicate via Slack, and update our project status via trello board 
> to keep team members as well as the product owner updated on where their progress is.

### Glossary and Acronyms
> _Provide a table of terms and acronyms._

| Term | Definition |
|------|------------|
| VO | Value Object |
| AI | Artificial Intelligence |


## Requirements

This section describes the features of the application.

### Definition of MVP

> The MVP of this product, should allow players to sign in with an alphanumeric username and play a
> game of WebCheckers against an opponent, In which one of the players wins/loses/resigns.

### MVP Features

> For epics for the MVP we had Game Board, Movement, and Checker Pieces.
> For the Game Board we had to store all the pieces on the board
> For Movement we had to validate all moves that the pieces on the board could do
> For the checker pieces that was to ensure that the player started as the right color
> and could only move their colored pieces

### Roadmap of Enhancements

>The enhancements that we decided to go with were to add AI opponent with varying difficulties to play against
>and to add asynchronous play.

>For the AI opponent enhancement, the user would be presented three varying difficulties on the homepage to 
>play against. After selecting a difficulty, the AI will reactively submit moves once the user submits their move.

>For the asynchronous play the user is able to save the game when it is their turn, in which 
>both players will be exited from the game state and returned to the homepage in which you can 
>then load the saved game as long as the opponent is not in another game.


## Application Domain

This section describes the application domain.

![The WebCheckers Domain Model](domain-model-placeholder.png)

> The Player is one of the most important entities.
> The player represents a user interacting with our application.
> The player can either play against opponents; the opponent be an AI or another online player.
> Another important entity is the game, which represents a checkers game.
> A game contains a board made of 64 squares with 12 red pieces and 12 white pieces on the board.
> The pieces have certain restrictions on their movement that need to be adhered to.


## Architecture and Design

This section describes the application architecture.

### Summary

The following Tiers/Layers model shows a high-level view of the webapp's architecture.

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)

As a web application, the user interacts with the system using a
browser.  The client-side of the UI is composed of HTML pages with
some minimal CSS for styling the page. There is also some JavaScript
that has been provided to the team by the architect.

The server-side tiers include the UI Tier that is composed of UI Controllers and Views.
Controllers are built using the Spark framework and View are built using the FreeMarker framework.
The Application and Model tiers are built using plain-old Java objects (POJOs).

Details of the components within these tiers are supplied below.


### Overview of User Interface

This section describes the web interface flow; this is how the user views and interacts
with the WebCheckers application.

![The WebCheckers Web Interface Statechart](web-interface-placeholder.png)

From the perspective of a user, the user first logs in with a valid
alphanumeric username. Upon logging in, the user views a list of players they
are able to challenge, and can challenge one at a time. After challenging a
listed player, the player is then directed to a game page and the player that
controls the red pieces moves first and the players then take turns moving. A
game can be finished when all of a player’s pieces are gone or a player
resigns.


### UI Tier
> _Provide a summary of the Server-side UI tier of your architecture.
> Describe the types of components in the tier and describe their
> responsibilities.  This should be a narrative description, i.e. it has
> a flow or "story line" that the reader can follow._

The **GetHomeRoute** handles the rendering of the homepage. Like all UI components,
it retrieves the player's username through a session attribute and checks whether
a player is logged in by testing equality with null. Depending on this value, one of
two page formats are rendered. The first, if the player is not logged in, is a
counter of the players in the lobby, through the PlayerLobby class, and a link to
the SignIn page (discussed in paragraph 2). The second, if the player is logged in,
the player is presented with a list of available players, unavailable players, and
buttons to load saved games, which directs the player through
PostLoadGameRoute (discussed in paragraph 4). The player enters an available When a
player enters the name of an available player in a submit box which routes the player
through PostGameRoute (discussed in paragraph 3).

**GetSignInRoute** renders the signin page and nothing else. **PostSignInRoute**, invoked
when submit is pressed, validates the username (is it alphanumeric, not "CPU", and
not in use), adds the validated playername to the PlayerLobby object shared between
many UI tier classes, and updates the username session attribute to mark the player
as signed in as well as store the username. On an invalid username, the signin page
is rendered. On a valid username, the player is redirected to the homepage.

**PostGameRoute** handles the creation of games. Using the parameters in the request, it
determines what difficulty of bot to use, if applicable, and gets the name of the
requested opponent. Using the PlayerLobby, it checks that the player is both online
and not the same player, checks that the opponent is not already in a game, and it 
creates and renders the game. **GetGameRoute** renders the game, checks for win
conditions, passes the game end condition message for the template and a boolean stating
whether the game has ended for some JS nonsense. If the opponent saves, or the game ends,
it redirects the player to the homepage. Checking winners is done through the Game class,
checking player status is done through GameManager, and checking player existence is done
through PlayerLobby.

**PostLoadGameRoute** handles loading a game into the GameManager from the Map of saved
games in Player objects. Load buttons on the homepage have an attribute under an integer
and the selected one will have a nonnull value containing the key of the game. The game is
retrieved from the map in the Player object, retrieved from the PlayerLobby and username
attribute, and passed to a load function in the GameManager. Checking that the game is versus
an AI is done with a field in Game, checking that the opponent is online is done using
PlayerLobby. If the opponent is offline or already in a game, the player is redirected to
the homepage. Otherwise, the player is redirected to the game page and the page is rendered.

**PostSaveGameRoute** handles interaction between the UI from the save button on the game
and saved games in the Player objects. The route tests if the player is the active color
using the Game class and redirects the user to the game, ignoring the request, if the
player is not the active player. If the game has a winner, the request is ignored and the
player is redirected to the game page. If there are moves in the move stack (technically
an ArrayList), then they are undone. Once these tests are passed, the game is saved for
the player who clicked the button. If the player is an AI, its name is stored as null. This
is checked in the game object to determine when not to save the game.

**PostSignOutRoute** is invoked when the signout button is pressed. It removes the player
from the PlayerLobby object and clears the username attribute to mark the player as not
signed in.
 
The remaining routes are invoked through the JavaScript and Ajax calls on the game page. All
the following Routes get the game object, for whatever they need, through the GameManager
using the username attribute.

**PostBackupRoute** invoked when the backup button is pressed on the page. It calls the
undoMove function in the Game object and returns a JSON formatted Message depending on
whether there was a move to back up. 

**PostCheckTurn** is invoked when there is a POST request to "/checkTurn" and it returns true
in a JSON Message if the player is active, checked using Game,  or the board is in the exit
state, checked using Board. Otherwise, false is returned.

**PostResignGameRoute** checks if the player can resign, if they're the active player. If
so, the resign and win flag is set using Board.

**PostValidateRoute** is invoked when there is a POST request to "/validateMove" and it 
checks the validity of the passed move, retrieved through a parameter in the requets, by
using the validateMove function in the Game object. If the move is valid, it puts the move
on the move stack and sets the moved piece to a king, if necessary. A JSON Message containing
with a boolean for the validity of the move is returned.

### Application Tier
> _Provide a summary of the Application tier of your architecture. This
> section will follow the same instructions that are given for the UI
> Tier above._


### Model Tier
> _Provide a summary of the Application tier of your architecture. This
> section will follow the same instructions that are given for the UI
> Tier above._

### Design Improvements
> _Discuss design improvements that you would make if the project were
> to continue. These improvement should be based on your direct
> analysis of where there are problems in the code base which could be
> addressed with design changes, and describe those suggested design
> improvements. After completion of the Code metrics exercise, you
> will also discuss the resulting metric measurements.  Indicate the
> hot spots the metrics identified in your code base, and your
> suggested design improvements to address those hot spots._

## Testing
> _This section will provide information about the testing performed
> and the results of the testing._

### Acceptance Testing
> _Report on the number of user stories that have passed all their
> acceptance criteria tests, the number that have some acceptance
> criteria tests failing, and the number of user stories that
> have not had any testing yet. Highlight the issues found during
> acceptance testing and if there are any concerns._

### Unit Testing and Code Coverage
> _Discuss your unit testing strategy. Report on the code coverage
> achieved from unit testing of the code base. Discuss the team's
> coverage targets, why you selected those values, and how well your
> code coverage met your targets. If there are any anomalies, discuss
> those._
