package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.logging.Logger;

/**
 * @author Zane Kitchen Lipski
 * To check if a move made was valid
 */
public class PostValidateMove implements Route {
    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code POST /validateMove} route handler.
     * @param templateEngine the template engine
     * @param gameManager the game manager for all games
     */
    public PostValidateMove(final TemplateEngine templateEngine, final GameManager gameManager) {
        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    /**
     * sends valid move to model
     * @param request the request
     * @param response the response
     * @return json message
     * @throws Exception thrown
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        String name = request.session().attribute(PostSignInRoute.USERNAME); //get player's name
        LOG.config("player " + name + " makes a move");
        Game game = gameManager.findPlayerGame(name);

        Gson gson = new Gson();
        //get move that was made
        Move move = gson.fromJson(request.queryParams("actionData"),Move.class);

        boolean yea = game.validateMove(move);

        Message msg;

        if (yea) { //if true: info msg and make move
            msg = Message.info(game.getValidity());
            game.makeMove(move); game.setKing(move);
        }
        else { //if false: error msg
            msg = Message.error(game.getValidity());
        }

        String json;
        json = gson.toJson(msg);
        return json;
    }
}
