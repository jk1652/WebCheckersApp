package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Piece;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * Author: Zane Kitchen Lipski
 * To check if a move made was valid
 */

public class PostValidateMove implements Route {
    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    public PostValidateMove(final TemplateEngine templateEngine, final GameManager gameManager) {
        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {


        String name = request.session().attribute(PostSignInRoute.USERNAME); //get player's name
        LOG.config("player makes move: " + name);
        Game game = gameManager.findPlayerGame(name);

        //TODO check that player making request is moving their color piece and it's their turn
        Gson gson = new Gson();

        Move move = gson.fromJson(request.queryParams("actionData"),Move.class);
        game.makeMove(move);
        return true;
    }
}
