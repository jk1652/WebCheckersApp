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
    PostSignInRoute(PlayerLobby playerLobby, TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException when an invalid result is returned after making a guess
     */
    @Override
    public String handle(Request request, Response response) {
        final Map<String, Object> vm = new HashMap<>();
        final Session session = request.session();
        session.attribute("signingIn");

        final String name = request.queryParams(USERNAME);

        System.out.println(name);

        if (playerLobby.addPlayer(name)) {
            session.attribute(USERNAME, name);
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
        else {
            //vm.put(ERROR_MESSAGE_USERNAME_IN_USE, new Error(ERROR_MESSAGE_USERNAME_IN_USE));
            vm.put("message", ERROR_MESSAGE_USERNAME_IN_USE);
            return templateEngine.render(new ModelAndView(vm, "login.ftl"));
        }
    }

    private ModelAndView error(final Map<String, Object> vm, final String message) {
        vm.put(USERNAME, message);
        vm.put("message.type", "error");
        return new ModelAndView(vm, "login.ftl");
    }

}
