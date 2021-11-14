package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @Author Zane Kitchen Lipski
 * @Author Jaden Kitchen Lipski
 * PostCheckTurn, check who's turn it is
 */
public class PostCheckTurn implements Route {

    private final GameManager gameManager;

    /**
     * The constructor for the {@code POST /checkTurn} route handler.
     *
     * @param gameManager the game manager
     */
    public PostCheckTurn(final GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * sends check turn request
     * @param request the HTTP request
     * @param response the HTTP response
     * @return json message
     * @throws Exception exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        String name = request.session().attribute(PostSignInRoute.USERNAME); //get player's name
        Player player = new Player(name);
        Game game = gameManager.findPlayerGame(name);

        Gson gson = new GsonBuilder().create();
        Message msg;

        if (game == null) {
            msg = Message.info("true");
            return gson.toJson( msg );
        }

        Piece.Color activeColor = game.getActiveColor();

        Board board = game.getBoardView();


        // when game is over update other player view
        if (board.getExitState()) {
            msg = Message.info("true");
            return gson.toJson( msg );
        }

        // if red player asks if it is their turn and they are active
        if (game.getRedPlayer().equals(player) && activeColor == Piece.Color.RED){
            msg = Message.info("true");
        }
        // if white player asks if it is their turn and they are active
        else if (game.getWhitePlayer().equals(player) && activeColor == Piece.Color.WHITE) {
            msg = Message.info("true");
        }
        // if player is not active
        else {
            msg = Message.info("false");
        }

        return gson.toJson( msg );
    }
}
