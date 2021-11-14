package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

public class PostSignInRoute implements Route {

    static final Message ERROR_MESSAGE_CPU_NAME = Message.error("Invalid Username: Cannot use \"CPU\" name.");
    static final Message ERROR_MESSAGE_USERNAME_IN_USE = Message.error("Invalid Username: Username in use.");
    static final Message ERROR_MESSAGE_USERNAME_INVALID = Message.error("Invalid Username: Username needs to include " +
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
     * @param request the HTTP request
     * @param response the HTTP response
     * @return back to home page while signed in
     */
    @Override
    public String handle(Request request, Response response) {
        final Map<String, Object> vm = new HashMap<>();

        final String name = request.queryParams(USERNAME);

        // Don't allow a player to have name cpu (reserved for ai)
        if (name.equals("CPU")) {
            vm.put("message", ERROR_MESSAGE_CPU_NAME);
            return templateEngine.render(new ModelAndView(vm, "login.ftl"));
        }

        // Don't allow a player to have a non valid name
        if (!(playerLobby.isValidName(name))){
            vm.put("message", ERROR_MESSAGE_USERNAME_INVALID);
            return templateEngine.render(new ModelAndView(vm, "login.ftl"));
        }

        // add player to player lobby with valid name
        if (playerLobby.addPlayer(name)) {
            request.session().attribute(USERNAME, name);
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
        // user did not have valid name
        else {
            vm.put("message", ERROR_MESSAGE_USERNAME_IN_USE);
            return templateEngine.render(new ModelAndView(vm, "login.ftl"));
        }
    }

}
