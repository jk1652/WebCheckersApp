package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

public class PostSignOutRoute implements Route {

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    PostSignOutRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);
        playerLobby.removePlayer(playerName);
        request.session().removeAttribute(PostSignInRoute.USERNAME);
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;

    }
}
