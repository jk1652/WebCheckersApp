package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Author: Zane Kitchen Lipski
 * To check if a move made was valid
 */

public class PostValidateMove implements Route {

    private GameManager gameManager;

    public PostValidateMove(GameManager gameManger) {
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {


        String name = request.queryParams(PostSignInRoute.USERNAME); //get player's name
        Game game = gameManager.findPlayerGame(name);
        if (game.getActiveColor() == Piece.Color.RED) { //checks who's turn it is

        }


        return null;
    }
}
