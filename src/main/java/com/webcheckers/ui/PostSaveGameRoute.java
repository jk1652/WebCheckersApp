package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.logging.Logger;

/**
 * @Author Zane Kitchen Lipski
 * @Author Jaden Kitchen Lipski
 * PostResignGameRoute
 */
public class PostSaveGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    private final Gson gson;
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param gson
     * @param templateEngine
     * @param gameManager
     */
    public PostSaveGameRoute(final Gson gson, final TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameManager gameManager){
        this.gson = gson;
        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
    }

    /**
     * sends resign game state
     * @param request
     * @param response
     * @return reisgn game state json message
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);

        Player player = playerLobby.getPlayer(playerName);

        Game game = gameManager.findPlayerGame(playerName);

        Message msg;
        if(game.getUserColor(playerName) == game.getActiveColor()){
            player.saveGame(game);

            Player otherPlayer = playerLobby.getPlayer(game.getOpponentName(playerName));

            if (otherPlayer != null) {
                otherPlayer.saveGame(game);
                otherPlayer.savedGamesDidGoUp();
                request.session().attribute("message", Message.info("Your game against " + otherPlayer + " was Saved"));
            }

            else{
                request.session().attribute("message", Message.info("Your game against the AI was Saved"));
            }
            gameManager.finishGame(playerName);
            response.redirect(WebServer.HOME_URL);
            return null;
        }

        else{
            msg = Message.error("You can only save the game on your turn");
        }

        return gson.toJson(msg);
    }
}
