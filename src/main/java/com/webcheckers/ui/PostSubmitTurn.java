package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Piece;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.Objects;
import java.util.logging.Logger;
/**
 * @Author Zane Kitchen Lipski
 * @Author Jaden Kitchen Lipski
 * PostSubmitTurn, check when a turn is being submitted
 */
public class PostSubmitTurn implements Route {

    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());
    private final TemplateEngine templateEngine;
    private final GameManager gameManager;

    /**
     * The constructor for the {@code POST /submitTurn} route handler.
     * @param templateEngine the template engine
     * @param gameManager the game manager for all games
     */
    PostSubmitTurn(final TemplateEngine templateEngine, final GameManager gameManager) {
        // validation
        Objects.requireNonNull(gameManager, "game must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    /**
     * This sends a submitted turn
     * @param request the request
     * @param response the response
     * @return json message type
     * @throws Exception thrown
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);

        Game game = gameManager.findPlayerGame(playerName);

        //If the player is playing an AI, check if AI is stalemate
        if(game.getSinglePlayer() && game.getActiveColor() == Piece.Color.WHITE){
            if(game.checkStalemate()){game.submitMove(); return null;}
        }

        //checks if there is a forcible jump on the game board
        Move latest = game.getPastMoves().get(game.getPastMoves().size() - 1);
        Message msg;

        if(latest.isJump()){
            if(game.getPromotion()){game.submitMove(); msg = Message.info("Turn Submitted");}
            else if (game.canJump(latest.getEnd())) {msg = Message.error("Another Jump can be made");}
            else {game.submitMove(); msg = Message.info("Turn Submitted");}
        }
        else {game.submitMove(); msg = Message.info("Turn Submitted");}

        Gson gson = new GsonBuilder().create();
        return gson.toJson( msg );
    }
}
