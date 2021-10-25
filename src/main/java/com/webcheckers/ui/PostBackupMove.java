package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

public class PostBackupMove implements Route {

    private final TemplateEngine templateEngine;
    private final GameManager gameManager;

    PostBackupMove(final TemplateEngine templateEngine, final GameManager gameManager) {
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

        //TODO Use stack of moves and pop top and error check on legal pop

        return null;

    }
}
