package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

/**
 * @Author Zane Kitchen Lipski
 * @Author Jaden Kitchen Lipski
 * PostResignGameRoute
 */
public class PostResignGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    private final Gson gson;
    private final GameManager gameManager;

    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param gson The Google JSON parser object used to render Ajax responses.
     * @param gameManager the game manager
     */
    public PostResignGameRoute(final Gson gson, final GameManager gameManager){
        this.gson = gson;
        this.gameManager = gameManager;
    }

    /**
     * sends resign game state
     * @param request the HTTP request
     * @param response the HTTP response
     * @return resign game state json message
     * @throws Exception exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);

        Game game = gameManager.findPlayerGame(playerName);
        Board board = game.getBoardView();

        Message msg;
        msg = Message.info(playerName + " Resigned from game, please redirect to the home page!");
        LOG.config(playerName + " Resigned from game, please redirect to the home page!");

        //check if player asking to resign is active color
        Piece.Color userColor = game.getUserColor(playerName);
        if (userColor == game.getActiveColor()){
            board.setWinner(game.getUserColor(playerName));
        }
        else {
            msg = Message.error("Can't resign now");
        }

        return (gson.toJson(msg));

    }
}
