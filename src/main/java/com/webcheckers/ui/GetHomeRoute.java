package com.webcheckers.ui;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetHomeRoute is initialized.");
    this.playerLobby = playerLobby;

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
    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    // display a user message in the Home page
    vm.put("welcome", WELCOME_MSG);

    String playerName = request.session().attribute(PostSignInRoute.USERNAME);

    if (!playerLobby.addPlayer(playerName)) {
      request.session().removeAttribute(PostSignInRoute.USERNAME);
      //PLAYER_NAME = null;
    }

    if (playerName != null) {
      String playerList = new String();

      for (int x = 0; x < playerLobby.numberofPlayers(); x++) {
        if (playerLobby.getPlayerName(x) == playerName) {
          continue;
        }
        playerList = playerList + (playerLobby.getPlayerName(x) + "\n");
      }

      vm.put("currentUser", playerName);
      vm.put("playerList", playerList);
    }
    else {
      vm.put("message", Message.info("Current Number of Players: " +
              String.valueOf(playerLobby.numberofPlayers())));
    }


    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
