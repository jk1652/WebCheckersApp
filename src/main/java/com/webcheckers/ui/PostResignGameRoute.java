package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostResignGameRoute implements Route {

    private GameManager gameManager;

    public PostResignGameRoute(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String name = request.queryParams(PostSignInRoute.USERNAME); //get player's name
        gameManager.finishGame(name);

        Message msg = Message.info("Resigned from game, please redirect to the home page!");

        return msg;

    }
}
