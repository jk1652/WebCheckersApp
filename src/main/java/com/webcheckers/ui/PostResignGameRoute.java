package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.util.Message;
import spark.*;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

public class PostResignGameRoute implements Route {

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    public PostResignGameRoute(final TemplateEngine templateEngine, final GameManager gameManager){
        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Map<String, Object> vm = new HashMap<>();

        String name = request.session().attribute(PostSignInRoute.USERNAME); //get player's name

        Message msg = Message.info("Resigned from game, please redirect to the home page!");

        return (new Gson()).toJson(msg);
    }
}
