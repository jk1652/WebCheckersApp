package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostSubmitTurn implements Route {

    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());
    private final TemplateEngine templateEngine;
    private final GameManager gameManager;

    PostSubmitTurn(final TemplateEngine templateEngine, final GameManager gameManager) {
        // validation
        Objects.requireNonNull(gameManager, "game must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);

        Game game = gameManager.findPlayerGame(playerName);

        boolean check = game.forceJump();

        Message msg;
        if (check) {
            msg = Message.error("Jump available");
        }
        else {
            game.submitMove();
            msg = Message.info("Turn Submitted");
        }


        Gson gson = new GsonBuilder().create();
        return gson.toJson( msg );
    }
}
