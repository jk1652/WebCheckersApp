package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import com.webcheckers.model.Game;
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
  private final GameManager gameManager;
  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby,
          final GameManager gameManager) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetHomeRoute is initialized.");
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
    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    // display a user message in the Home page
    vm.put("welcome", WELCOME_MSG);

    String playerName = request.session().attribute(PostSignInRoute.USERNAME);

    Player player = playerLobby.getPlayer(playerName);

    if (playerName != null) {
      ArrayList<Player> playerList = playerLobby.getPlayerList();

      //System.out.println(player.getSaved());

      Game playerGame = gameManager.findPlayerGame(playerName);
      if (playerGame != null) { // Player is on the wrong page.
        request.attribute(PostGameRoute.GAME_ID_ATTRIBUTE, playerGame.getGameID());
        response.redirect(WebServer.GAME_URL);
        return null;
      }
      String players = "";
      String readyPlayers = "";
      String inGamePlayers = "";
      // creates players full of player names
      for (Player x : playerList) {
        if (x.equals(new Player(playerName))) {
          continue;
        }
        // if player is in NOT in a game make name GREEN
        if (gameManager.findPlayerGame(x) == null){
          readyPlayers =  readyPlayers + ("<li style=\"color:#52BE80 ;margin-left: 40px;\">" + x.getName() + "</li>");
        }
        // if player is in a game make their name RED
        else {
          inGamePlayers = inGamePlayers + ("<li style=\"color:#CB4335;margin-left: 40px;\">" + x.getName() + "</li>");
        }
      }


      vm.put("currentUser", playerName);

      // if there are no players online, declares so
      if (!readyPlayers.equals("") || !inGamePlayers.equals("")) {
        if (readyPlayers.equals("")){
          readyPlayers = "<p style=\"color:#52BE80;margin-left: 20px;\"><i> No players available</i></p>";
        }
        if (inGamePlayers.equals("")){
          inGamePlayers = "<p style=\"color:#CB4335;margin-left: 20px;\"><i> No players in a game</i></p>";
        }

        players = "<h2 style=\"color:black;\"> Available Players</h2>" + readyPlayers +
                "<h2 style=\"color:black;\"> Already in a game</h2>" + inGamePlayers;
        vm.put("playerListTitle", "<h2><b> Players online </h2></b>");
      }
      else {
        vm.put("playerListTitle", "<h2><b> No Players Online </h2></b>");
      }

      vm.put("playerList", players);
      
      Message message = request.session().attribute("message");
      if (message != null) {
      	vm.put("message", message);
      	request.session().attribute("message", null); // reset the attribute
      }
    }
    else {
      vm.put("message", Message.info("Current Number of Players: " +
              String.valueOf(playerLobby.numberofPlayers())));
    }

    // for saved games
    if (playerName != null){
      Player user = playerLobby.getPlayer(playerName);
      Map<String, Game> saveGames = user.getSaved();

      if (saveGames != null){
        String msg = "";
        Set<String> keys = saveGames.keySet();
        int i = 0;
        for (String x: keys){
          //msg = "<a href=\"/game\" onclick=\"gameManger.LoadGame(" + saveGames.get(x) + ");\">" + msg + x + "<br></a>";

          msg = msg + "<form action=\"./load\" method=\"POST\">\n" +
                  "            <button type=\"submit\" name=\"" + Integer.toString(++i) + "\">" + x + "</button>\n" +
                  "            </form>";

          //msg = msg + "<form id=\"load\" action=\"/load\" method=\"post\"> "
          //        + "<a href=\"/load\"  >" + x + "</a></form><br>";

          //name=\"" + x + "\"
        }
        if (keys.size() > 0) {
          vm.put("saveList", msg);
        }
      }
    }


    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
