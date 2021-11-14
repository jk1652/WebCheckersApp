package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.Objects;

import static spark.Spark.halt;

public class PostSignOutRoute implements Route {

    private final PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /signout} route handler.
     *
     * @param playerLobby the player lobby
     * @param templateEngine The default {@link TemplateEngine} to render page-level HTML views.
     */
    PostSignOutRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.playerLobby = playerLobby;
    }

    /**
     * This signs out a player after the clicking sign-out
     * @param request the HTTP request
     * @param response the HTTP response
     * @return back to home page
     * @throws Exception exception
     */
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
