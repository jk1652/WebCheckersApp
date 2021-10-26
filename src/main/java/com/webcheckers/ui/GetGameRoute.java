package com.webcheckers.ui;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.util.Message;
import com.webcheckers.model.Game;

/**
 * The UI Controller to GET the Game page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetGameRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;
  private final GameManager gameManager;
  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
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
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetGameRoute is invoked.");
    String playerName = request.session().attribute(PostSignInRoute.USERNAME);
    Game game = gameManager.findPlayerGame(playerName);
    Map<String, Object> vm = new HashMap<>();
    if (game != null) {
        // Check if someone won.
        Piece.Color winner = game.getWinner(); 
        if (winner != null) {
          Piece.Color userColor = game.getUserColor(playerName));[]
          if (userColor.equals(winner))
            request.session().attribute("message", Message.error("You won!"));
          else
            request.session().attribute("message", Message.error("You lost.")); 
          response.redirect(WebServer.HOME_URL);
    	    return null;
        }
        vm.put("title", "Checkers!");
        vm.put("currentUser", playerName);
        vm.put("viewMode", Game.View.PLAY);
        vm.put("redPlayer", game.getRedPlayer());
        vm.put("whitePlayer", game.getWhitePlayer());
        vm.put("activeColor", game.getActiveColor());
        vm.put("board", game.getBoardView());
        vm.put("flip", game.getRedPlayer().getName().equals(playerName));
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    } else {
    	response.redirect(WebServer.HOME_URL);
    	return null;
    }
  }
}