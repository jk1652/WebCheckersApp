package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static spark.Spark.halt;

public class PostBackupMove implements Route {

    private final TemplateEngine templateEngine;
    private final GameManager gameManager;

    /**
     * The constructor for the {@code POST /backup} route handler.
     *
     *
     * @param templateEngine template engine to use for rendering HTML page
     * @param gameManager mangers for all the games
     * @throws NullPointerException when the {@code playerLobby} or {@code templateEngine} parameter is null
     */
    PostBackupMove(final TemplateEngine templateEngine, final GameManager gameManager) {
        // validation
        Objects.requireNonNull(gameManager, "game must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    /**
     * This backups a previously valid move
     * @param request
     * @param response
     * @return json message
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);

        Game game = gameManager.findPlayerGame(playerName);

        boolean check = game.undoMove();

        Message msg;
        if (check) { // true
            msg = Message.info("Success, Backed up move");
        }
        else { // false when there are no moves to back up
            msg = Message.error("No Moves in Stack");
        }

        Gson gson = new Gson();
        String json;
        json = gson.toJson(msg);
        return json;

    }
}
