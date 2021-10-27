package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostResignGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    private final Gson gson;
    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    public PostResignGameRoute(final Gson gson, final TemplateEngine templateEngine, final GameManager gameManager){
        this.gson = gson;
        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Map<String, Object> vm = new HashMap<>();

        //String name = request.session().attribute(PostSignInRoute.USERNAME); //get player's name

        //Message msg = Message.info(name + "Resigned from game, please redirect to the home page!");

        //return (new Gson()).toJson(msg);

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);

        Game game = gameManager.findPlayerGame(playerName);
        Board board = game.getBoardView();

        Message msg;
        msg = Message.info(playerName + " Resigned from game, please redirect to the home page!");
        LOG.config(playerName + " Resigned from game, please redirect to the home page!");




        //Gson gson = new Gson();

        //String json;
        //json = gson.toJson(msg);
        //return json;
        //response.redirect(WebServer.HOME_URL);


        board.setWinner(game.getUserColor(playerName));

        return (gson.toJson(msg));

        /**
        final Map<String, Object> modeOptions = new HashMap<>(2);

        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", playerName + " Resigned from game, please redirect to the home page!");

        //modeOptions.put("isGameOver", false);
        //modeOptions.put("gameOverMessage", "");

        //vm.put("message", msg);
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        vm.put("title", "Checkers!");
        vm.put("currentUser", playerName);
        vm.put("viewMode", Game.View.PLAY);
        vm.put("redPlayer", game.getRedPlayer());
        vm.put("whitePlayer", game.getWhitePlayer());
        vm.put("activeColor", game.getActiveColor());
        vm.put("board", game.getBoardView());
        vm.put("flip", game.getRedPlayer().getName().equals(playerName));
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));

        //request.session().attribute("modeOptionsAsJSON", gson.toJson(modeOptions));
        //return gson.toJson(modeOptions);
         */
    }
}
