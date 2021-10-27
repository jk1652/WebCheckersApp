package com.webcheckers.ui;

import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import static spark.Spark.halt;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import static spark.Spark.halt;

import com.webcheckers.appl.PlayerLobby;

import static spark.Spark.halt;

public class PostSignInRoute implements Route {

    static final Message ERROR_MESSAGE_USERNAME_IN_USE = Message.info("Invalid Username: Username in use.");
    static final Message ERROR_MESSAGE_USERNAME_INVALID = Message.info("Invalid Username: Username needs to include " +
            "at least one alphanumeric character.");

    static final String USERNAME = "playerName";

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code POST /signin} route handler.
     *
     * @param playerLobby    {@Link PlayerLobby} that holds over statistics
     * @param templateEngine template engine to use for rendering HTML page
     * @throws NullPointerException when the {@code playerLobby} or {@code templateEngine} parameter is null
     */
    PostSignInRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
    }

    /**
     * This signs a player in
     * @param request
     * @param response
     * @return back to home page while signed in
     */
    @Override
    public String handle(Request request, Response response) {
        final Map<String, Object> vm = new HashMap<>();



        final String name = request.queryParams(USERNAME);

        if (!(playerLobby.isValidName(name))){
            vm.put("message", ERROR_MESSAGE_USERNAME_INVALID);
            return templateEngine.render(new ModelAndView(vm, "login.ftl"));
        }

        if (playerLobby.addPlayer(name)) {
            request.session().attribute(USERNAME, name);
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
        else {
            vm.put("message", ERROR_MESSAGE_USERNAME_IN_USE);
            return templateEngine.render(new ModelAndView(vm, "login.ftl"));
        }
    }

}
