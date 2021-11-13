package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import jdk.swing.interop.SwingInterOpUtils;
import spark.*;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static spark.Spark.halt;

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

        Map<String, Object> vm = new HashMap<>();

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);

        Player player = playerLobby.getPlayer(playerName);

        Game game = gameManager.findPlayerGame(playerName);

        player.saveGame(game);
        //player.savedGamesDidGoUp();

        Player otherPlayer = playerLobby.getPlayer(game.getOpponentName(playerName));

        //if inactive player asks to save do nothing
        Piece.Color userColor = game.getUserColor(playerName);
        if (userColor != game.getActiveColor()){
            otherPlayer = null;
            response.redirect(WebServer.GAME_URL);
            return false;
        }
        // if any moves has occured do nothing
        if (game.getMoveSize() > 0) {
            for(int i = game.getMoveSize(); i > 0; i--){
                game.undoMove();
            }
            //response.redirect(WebServer.GAME_URL);
            //return Message.error("Can't save with loaded moves");
        }

        if (otherPlayer != null) {
            otherPlayer.saveGame(game);
            otherPlayer.savedGamesDidGoUp();
        }

        request.session().attribute("message", Message.info("Game was Saved"));
        gameManager.finishGame(playerName);
        response.redirect(WebServer.HOME_URL);
        return null;

    }
}
