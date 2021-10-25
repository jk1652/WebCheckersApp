package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class PostCheckTurn implements Route {

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    public PostCheckTurn(final TemplateEngine templateEngine, final GameManager gameManager) {
        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return true;
    }
}
