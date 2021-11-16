package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
/**
 * The UI Controller to GET the Game page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetGameRoute implements Route {
  private static final Gson gson = new GsonBuilder().create();
  private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());
  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;
  private final GameManager gameManager;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /Game} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby,
   final GameManager gameManager) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetGameRoute is initialized.");
    this.playerLobby = playerLobby;
    this.gameManager = gameManager;
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Game page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetGameRoute is invoked.");
    String playerName = request.session().attribute(PostSignInRoute.USERNAME);
    Game game = gameManager.findPlayerGame(playerName);

    Map<String, Object> vm = new HashMap<>();

    Board board = null;

    if (game != null) {
        board = game.getBoardView();
    }

    if (board != null && board.getExitState()) {
        gameManager.finishGame(playerName);
        LOG.finer("Game was Resigned.");
        game = gameManager.findPlayerGame(playerName);
    }

    if (game != null) {
        Piece.Color winner = game.getWinner();
        final Map<String, Object> modeOptions = new HashMap<>(2);
        vm.put("title", "Checkers!");
        vm.put("currentUser", playerName);
        vm.put("viewMode", Game.View.PLAY);
        vm.put("redPlayer", game.getRedPlayer());
        vm.put("whitePlayer", game.getWhitePlayer());
        vm.put("activeColor", game.getActiveColor());
        vm.put("board", game.getBoardView());
        vm.put("flip", game.getRedPlayer().getName().equals(playerName));

        //First check for a stalemate
        if(game.checkStalemate() && winner == null){
            modeOptions.put("isGameOver", true);
            modeOptions.put("gameOverMessage", "The match has come to a stalemate and cannot proceed");
        }
        //Check if there is a declared winner.
        if (winner != null && Objects.requireNonNull(board).getResign() == Boolean.FALSE) {
            Piece.Color userColor = game.getUserColor(playerName);
            String winnerName;
            if (userColor.equals(winner))
                winnerName = playerName;
            else
                winnerName = game.getOpponentName(playerName);
            modeOptions.put("isGameOver", true);
            modeOptions.put("gameOverMessage", winnerName + " has won and has captured all of the opposing pieces.");
        }
        else if (winner == null && Objects.requireNonNull(board).getResign() == Boolean.FALSE) {
            modeOptions.put("isGameOver", false);
            modeOptions.put("gameOverMessage", "");
        }
        else if (winner != null && board.getResign() == Boolean.TRUE) {
            Piece.Color userColor = game.getUserColor(playerName);
            String loserName;
            if (userColor.equals(winner))
                loserName = playerName;
            else
                loserName = game.getOpponentName(playerName);
            modeOptions.put("isGameOver", true);
            modeOptions.put("gameOverMessage", loserName + " has lost by resign.");
        }
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
        }
    else {
        if (playerLobby.getPlayer(playerName).currentSavedGamesWentUp()) {
            request.session().attribute("message", Message.info("Your opponent has left, but luckily they saved the game against you :)"));
            playerLobby.getPlayer(playerName).savedGamesOnLVL();
        }
        else {
            request.session().attribute("message", Message.info("Your checkers game has ended"));
        }
    	response.redirect(WebServer.HOME_URL);
    	return null;
    }
  }
}
