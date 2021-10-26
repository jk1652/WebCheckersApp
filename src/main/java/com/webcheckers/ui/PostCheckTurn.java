package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
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


        String name = request.session().attribute(PostSignInRoute.USERNAME); //get player's name
        Player player = new Player(name);
        Game game = gameManager.findPlayerGame(name);

        Piece.Color activecolor = game.getActiveColor();

        Message msg;
        // if red player asks if it is their turn and they are active
        if (game.getRedPlayer().equals(player) && activecolor == Piece.Color.RED){
            msg = Message.info("true");
            request.session().attribute("message", msg);
            //return true;
        }
        // if white player asks if it is their turn and they are active
        else if (game.getWhitePlayer().equals(player) && activecolor == Piece.Color.WHITE) {
            msg = Message.info("true");
            request.session().attribute("message", msg);
            //return true;
        }
        // if player is not active
        else {
            msg = Message.info("false");
            request.session().attribute("message", msg);
            //return false;
        }

        String json;
        Gson gson = new GsonBuilder().create();
        json = gson.toJson( msg );
        return json;

    }
}
