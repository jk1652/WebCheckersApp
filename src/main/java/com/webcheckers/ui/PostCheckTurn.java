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
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;
/**
 * @Author Zane Kitchen Lipski
 * @Author Jaden Kitchen Lipski
 * PostCheckTurn, check who's turn it is
 */
public class PostCheckTurn implements Route {

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code POST /checkTurn} route handler.
     *
     * @param templateEngine
     * @param gameManager
     */
    public PostCheckTurn(final TemplateEngine templateEngine, final GameManager gameManager) {
        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    /**
     * sends checkturn request
     * @param request
     * @param response
     * @return json message
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        String name = request.session().attribute(PostSignInRoute.USERNAME); //get player's name
        Player player = new Player(name);
        Game game = gameManager.findPlayerGame(name);

        Piece.Color activecolor = game.getActiveColor();

        Board board = game.getBoardView();

        Gson gson = new GsonBuilder().create();
        Message msg;
        // when game is over update other player view
        if (board.getExitState()) {
            msg = Message.info("true");
            return gson.toJson( msg );
        }

        // if red player asks if it is their turn and they are active
        if (game.getRedPlayer().equals(player) && activecolor == Piece.Color.RED){
            msg = Message.info("true");
        }
        // if white player asks if it is their turn and they are active
        else if (game.getWhitePlayer().equals(player) && activecolor == Piece.Color.WHITE) {
            msg = Message.info("true");
        }
        // if player is not active
        else {
            msg = Message.info("false");
        }

        return gson.toJson( msg );
    }
}
