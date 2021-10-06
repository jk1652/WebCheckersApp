package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameManager;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

import com.webcheckers.util.Message;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

public class PostGameRoute implements Route {
    public static final String OPPONENT_NAME = "opponentName";
    public static final String GAME_ID_ATTRIBUTE = "gameID";
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    PostGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby,
            final GameManager gameManager) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");
        //
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String playerName = request.session().attribute(PostSignInRoute.USERNAME);
        String opponentName = request.queryParams(OPPONENT_NAME);
        Integer gameID = request.session().attribute(GAME_ID_ATTRIBUTE);
        Game game;
        if (gameManager.findPlayerGame(opponentName) == null) {
		if (gameID == null) { // create a game
		    game = gameManager.createGame(playerName, opponentName);
		    request.attribute(GAME_ID_ATTRIBUTE, game.getGameID());
		} else {
		    game = gameManager.getActiveGame(gameID);
		    if (game == null) { // There is no active game for this ID
		        response.redirect(WebServer.HOME_URL);
		        return null;
		    }
		}
		Map<String, Object> vm = new HashMap<>();
		vm.put("title", "Checkers!");
		vm.put("currentUser", playerName);
		vm.put("viewMode", Game.View.PLAY);
		vm.put("redPlayer", game.getRedPlayer());
		vm.put("whitePlayer", game.getWhitePlayer());
		vm.put("activeColor", game.getActiveColor());
		vm.put("board", game.getBoardView());
		vm.put("flip", game.getRedPlayer().getName().equals(playerName));
		return templateEngine.render(new ModelAndView(vm , "game.ftl"));
        } else {
        	request.session().attribute("message", Message.error("That player is already in a game!"));
        	response.redirect(WebServer.HOME_URL);
        	return null;
        }
    }
}
