package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.util.Message;
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
