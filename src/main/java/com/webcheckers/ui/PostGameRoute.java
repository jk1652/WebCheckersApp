package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.AI;
import com.webcheckers.model.Game;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class PostGameRoute implements Route {
    public static final String OPPONENT_NAME = "opponentName";
    public static final String GAME_ID_ATTRIBUTE = "gameID";
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;
    private final TemplateEngine templateEngine;
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * The constructor for the {@code POST /game} route handler.
     * @param templateEngine The default {@link TemplateEngine} to render page-level HTML views.
     * @param playerLobby the player lobby
     * @param gameManager the game manager
     */
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

    /**
     * sends game related information
     * @param request the HTTP request
     * @param response the HTTP response
     * @return game info
     * @throws Exception exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String playerName = request.session().attribute(PostSignInRoute.USERNAME);
        String opponentName = request.queryParams(OPPONENT_NAME);
        String easy = request.queryParams("easy");
        String med = request.queryParams("med");
        String hard = request.queryParams("hard");
        String LoadGame = request.queryParams("test");
        Integer gameID = request.session().attribute(GAME_ID_ATTRIBUTE);
        Game game = null;

        if (LoadGame != null){
            LOG.fine("load game");
        }


        // if ai is selected
        if (easy != null){
            //start AI game
            LOG.fine("game against ai easy triggered");
            gameManager.createGame(playerName, AI.difficulty.Easy);
        }
        else if (med != null){
            LOG.fine("game against ai aggressive triggered");
            gameManager.createGame(playerName, AI.difficulty.Aggressive);
        }
        else if (hard != null) {
            LOG.fine("game against ai defensive triggered");
            gameManager.createGame(playerName, AI.difficulty.Defensive);
        }
        else {
            LOG.fine("ai null");
        }

        Map<String, Object> vm = new HashMap<>();

        if (game != null) {

            vm.put("title", "Checkers!");
            vm.put("currentUser", playerName);
            vm.put("viewMode", Game.View.PLAY);
            vm.put("redPlayer", game.getRedPlayer());
            vm.put("whitePlayer", game.getWhitePlayer());
            vm.put("activeColor", game.getActiveColor());
            vm.put("board", game.getBoardView());
            vm.put("flip", game.getRedPlayer().getName().equals(playerName));
            return templateEngine.render(new ModelAndView(vm , "game.ftl"));
        }

        if (!playerLobby.checkPlayerExist(opponentName)) {
            request.session().attribute("message", Message.error("Selected Player is not Online"));
            response.redirect(WebServer.HOME_URL);
            return null;
        }

        if (playerName.equals(opponentName)) {
            request.session().attribute("message", Message.error("Selected Player is Current User"));
            response.redirect(WebServer.HOME_URL);
            return null;
        }

        if (gameManager.findPlayerGame(opponentName) == null) {
            if (gameID == null) { // create a game
                game = gameManager.createGame(playerName, opponentName);
                request.attribute(GAME_ID_ATTRIBUTE, game.getGameID());
            }
            else {
                game = gameManager.getActiveGame(gameID);
                if (game == null) { // There is no active game for this ID
                    response.redirect(WebServer.HOME_URL);
                    return null;
                }
            }

            vm.put("title", "Checkers!");
            vm.put("currentUser", playerName);
            vm.put("viewMode", Game.View.PLAY);
            vm.put("redPlayer", game.getRedPlayer());
            vm.put("whitePlayer", game.getWhitePlayer());
            vm.put("activeColor", game.getActiveColor());
            vm.put("board", game.getBoardView());
            vm.put("flip", game.getRedPlayer().getName().equals(playerName));
            return templateEngine.render(new ModelAndView(vm , "game.ftl"));
        }
        else {
        	request.session().attribute("message", Message.error("That player is already in a game!"));
        	response.redirect(WebServer.HOME_URL);
        	return null;
        }
    }
}
