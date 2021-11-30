---
geometry: margin=1in
---
# PROJECT Design Documentation

## Team Information
>* Team name: Team 8
  > * Team members
  >   * Quentin Ramos
  >   * Zane Kitchen Lipski
  >   * Jaden Kitchen Lipski
  >   * Spencer Creveling
  >   * David Pritchard

## Executive Summary

>The WebCheckers is a web based application that allows players to log in with a unique alphanumeric username and play 
>checkers against other players. The user can also play against AI opponents with varying difficulties and save games 
>against opponents to play against them at another time.

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

###the following sections describes the features of the application that are required by the MVP as defined by the client.

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
![The WebCheckers Domain Model](domain-model-placeholder.png)

> The Player is one of the most important entities.
> The player represents a user interacting with our application.
> The player can either play against opponents; the opponent be an AI or another online player.
> Another important entity is the game, which represents a checkers game.
> A game contains a board made of 64 squares with 12 red pieces and 12 white pieces on the board.
> The pieces have certain restrictions on their movement that need to be adhered to.


## Architecture and Design

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)
>as seen above the Architecture of our system is relatively simple the user will load the preset HTML CSS & CSS while still
> being able to be loaded in any OS or hardware. once loaded the user communicates to with our server that utilises java to manage 
> all logic and jetty to handle web server POST/GET/PUT commands that the client may issue, these systems are then alsop utilised by 
> our model tier logic inorder to properly run the game as outlines in out MVP.

### Summary

>The following Tiers/Layers model shows a high-level view of the webapp's architecture.

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)

>As a web application, the user interacts with the system using a
browser.  The client-side of the UI is composed of HTML pages with
some minimal CSS for styling the page. There is also some JavaScript
that has been provided to the team by the architect.

>The server-side tiers include the UI Tier that is composed of UI Controllers and Views.
Controllers are built using the Spark framework and View are built using the FreeMarker framework.
The Application and Model tiers are built using plain-old Java objects (POJOs).

>Details of the components within these tiers are supplied below.


### Overview of User Interface

>This section describes the web interface flow; this is how the user views and interacts
>with the WebCheckers application.

![The WebCheckers Web Interface Statechart](web-interface-placeholder.png)

>From the perspective of a user, the user first logs in with a valid
>alphanumeric username. Upon logging in, the user views a list of players they
>are able to challenge, and can challenge one at a time. After challenging a
>listed player, the player is then directed to a game page and the player that
>controls the red pieces moves first and the players then take turns moving. A
>game can be finished when all of a player’s pieces are gone or a player
>resigns.

>Here is an example of how we start a game.

![The WebCheckers Sequence Diagram](SWEN%20Team%208%20Sequence%20Diagram.png)


### UI Tier
> _The **GetHomeRoute** handles the rendering of the homepage. Like all UI components,
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

>**GetSignInRoute** renders the signin page and nothing else. **PostSignInRoute**, invoked
when submit is pressed, validates the username (is it alphanumeric, not "CPU", and
not in use), adds the validated playername to the PlayerLobby object shared between
many UI tier classes, and updates the username session attribute to mark the player
as signed in as well as store the username. On an invalid username, the signin page
is rendered. On a valid username, the player is redirected to the homepage.

>**PostGameRoute** handles the creation of games. Using the parameters in the request, it
determines what difficulty of bot to use, if applicable, and gets the name of the
requested opponent. Using the PlayerLobby, it checks that the player is both online
and not the same player, checks that the opponent is not already in a game, and it
creates and renders the game. **GetGameRoute** renders the game, checks for win
conditions, passes the game end condition message for the template and a boolean stating
whether the game has ended for some JS nonsense. If the opponent saves, or the game ends,
it redirects the player to the homepage. Checking winners is done through the Game class,
checking player status is done through GameManager, and checking player existence is done
through PlayerLobby.

>**PostLoadGameRoute** handles loading a game into the GameManager from the Map of saved
games in Player objects. Load buttons on the homepage have an attribute under an integer
and the selected one will have a nonnull value containing the key of the game. The game is
retrieved from the map in the Player object, retrieved from the PlayerLobby and username
attribute, and passed to a load function in the GameManager. Checking that the game is versus
an AI is done with a field in Game, checking that the opponent is online is done using
PlayerLobby. If the opponent is offline or already in a game, the player is redirected to
the homepage. Otherwise, the player is redirected to the game page and the page is rendered.

>**PostSaveGameRoute** handles interaction between the UI from the save button on the game
and saved games in the Player objects. The route tests if the player is the active color
using the Game class and redirects the user to the game, ignoring the request, if the
player is not the active player. If the game has a winner, the request is ignored and the
player is redirected to the game page. If there are moves in the move stack (technically
an ArrayList), then they are undone. Once these tests are passed, the game is saved for
the player who clicked the button. If the player is an AI, its name is stored as null. This
is checked in the game object to determine when not to save the game.

>**PostSignOutRoute** is invoked when the signout button is pressed. It removes the player
from the PlayerLobby object and clears the username attribute to mark the player as not
signed in.

>The remaining routes are invoked through the JavaScript and Ajax calls on the game page. All
the following Routes get the game object, for whatever they need, through the GameManager
using the username attribute.

>**PostBackupRoute** invoked when the backup button is pressed on the page. It calls the
undoMove function in the Game object and returns a JSON formatted Message depending on
whether there was a move to back up.

>**PostCheckTurn** is invoked when there is a POST request to "/checkTurn" and it returns true
in a JSON Message if the player is active, checked using Game,  or the board is in the exit
state, checked using Board. Otherwise, false is returned.

>**PostResignGameRoute** checks if the player can resign, if they're the active player. If
so, the resign and win flag is set using Board.

>**PostValidateRoute** is invoked when there is a POST request to "/validateMove" and it
checks the validity of the passed move, retrieved through a parameter in the requets, by
using the validateMove function in the Game object. If the move is valid, it puts the move
on the move stack and sets the moved piece to a king, if necessary. A JSON Message containing
with a boolean for the validity of the move is returned.


### Application Tier
> For our application Tier we have 2 classes, GameManager, and PlayerLobby
> For PlayerLobby we are able to add players into the active pool of online players
> When a player decides to sign out they are removed from this pool of players online
> To prevent multiple users from signing with the same username, PlayerLobby will 
> compare the name they inputted to every other online player
> For The GameManager we can create games between players, in which will put players
> into a game to play against each other and this game will be stored in a pool of active games.
> Once a game has finished the game will be removed from the active pool, also this application
> will be able to load games, if they were already created then saved previously

### Model Tier
> Our model tier is the backbone of our WebCheckers project. The WebCheckers Model Tier encapsulates the game
> class which holds AI, Board, Game, Move, Piece, Player, Position, Row, Space.
> For AI we are able to select from 3 different AI from the Homepage, In which the AI will 
> act like a player and makes moves as if another player was making them. 
> The moves that the AI decides are based on a set of preferred moves based on the difficulty selected
> The Board class, stores the board and all the pieces on the board, in which the board will
> determine that when one player has all their pieces missing the other player will become the winner
> For the Game class that will validate moves on the board, as it stores the information of what 2 players 
> are playing each other, or it will store player vs AI.
> For the Move class all it does is determine if a jump or simple move is being made.
> For the Piece class we declare the color of the pieces on the board.
> For the Player class we determine that a player will have a list of saved games and their username stored
> For the Position class we can determine the position of pieces on the board.
> For the Row class we are able to use this class to make rows on the board in combination with the
> Space class that will provide space for pieces to be placed on and determine if spaces are droppable

### Design Improvements

> Some possible design improvements that we could make, could be more
> logging, and properly defining our win state for a game within the game model.
> Currently we have very minimal logging, improving our logging would allow
> trouble shooting to be much easier and creates a better understanding of 
> how the program works during operation. Then for our win state, our win state 
> for a game is held within the board connected to a game. This is not that big 
> of a problem for right now because a board and game are 1:1, but this could 
> cause problems if that was different. Moving the win state for a game 
> to a game would remove this possible problem.

> When looking at the code metrics in regard to our complexity our UI Classes suffer from high average operation complexity.
> The reason for these metrics is that all the logic required for each HTML request is managed in one large function. 
> We can improve these metrics by splitting up our logic into more specialized needs instead of having a catch all Function.
> By doing this our average operation complexity should meet the target numbers.

>In regard to out Java docs coverage we have many outliers that could be improved upon, many of our more important classes
> have 100% or near 100% code coverage however many classes also have 0% coverage. We can improve upon this issue by going 
> class by class and adding the necessary javadocs inorder remove these outliers.

> The canJump function checks each direction and has many nested ifs, long ones at that. 
> It also has some problems with duplication and violations of the Law of Demeter.
> `space.getPiece().getColor() != current.getColor()` could be replaced with:
> `!space.pieceColorMatches(current)`. Because we tried making many catch all functions, this is a relatively common problem.

## Testing
> During our testing sessions we ensured that all MVP user stories where being met
> we would often have several people testing these issues both through playing the game
> and creating custom scenarios though code overall almost all errors have been solved

### Acceptance Testing
> For our Acceptance Testing, we currently pass all the acceptance criteria tests. There were no concerns
> with any of the acceptance testing for any of the user stories.

### Unit Testing and Code Coverage
>during the development of our test we ended up implementing ToStrings as well as setters
>inorder to help use create ideal scenarios so that we could test as many possible branches.
>we still struggled to reach certain branches however as you will see below we have extensive
>testing coverage. 

![Code Coverage](overallcodecoverage.png)

> One part that was uniquely challenging to test was the AI, as its use of randomness makes
> it harder to test all random outcomes. However, by running tests multiple times and going through each 
> version of the AI we were able to get great AI coverage.

![AI Coverage](AiCovrarge.png)

> We struggled with testing the UI tier with JUnit, we aimed for high coverage to be be confident that the functions
> will work; however due to having troubles with mockito, we had lower coverage than we hoped for.

![UI Coverage](UIcoverage.png)

> We aimed for 100% coverage, but couldn't quite reach that. We ended up with 93% coverage, which was good enough.

>Another notable outlier in our code metrics are Java docs coverage, while many of the larger important classes have 
> 100% or near 100% javadoc coverage many smaller classes have 0% javadoc coverage. we could improve this by
> going through each class and adding the required javadocs to increase our coverage and remove these outliers.








