package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

public class PostLoadGameRoute implements Route {
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;
    private final TemplateEngine templateEngine;
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * The constructor for the {@code POST /load} route handler.
     * @param templateEngine The default {@link TemplateEngine} to render page-level HTML views.
     * @param playerLobby the player lobby
     * @param gameManager the game manager
     */
    PostLoadGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby,
                  final GameManager gameManager) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    /**
     * Load a game, based on button pressed on homepage
     * @param request the HTTP request
     * @param response the HTTP response
     * @return nothing
     * @throws Exception exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);

        String loadGame = "";

        Player user = playerLobby.getPlayer(playerName);

        Set<String> keys = user.getSaved().keySet();

        // find which button was pressed, so it loads the right game
        int i = 1;
        for (String x: keys){
            String test = request.queryParams(Integer.toString(i));
            i++;
            if (test != null){
                loadGame = x;
                break;
            }
        }
        LOG.fine("load game: " + loadGame);

        Game game = gameManager.LoadGame(user.getSaved().get(loadGame), user);

        if (game != null) {

            Player oppo = playerLobby.getPlayer(game.getOpponentName(playerName));

            Map<String, Object> vm = new HashMap<>();

            // check if you are playing a game vs a real player
            if (!game.getSinglePlayer()) {
                // check if that player is online
                if (playerLobby.getPlayer(game.getOpponentName(playerName)) == null) {
                    gameManager.finishGame(playerName);
                    request.session().attribute("message", Message.error("Selected Player is not Online"));
                    response.redirect(WebServer.HOME_URL);
                    return null;
                }
            }

            // unload saved game from the player's saved games
            user.removeSaveGame(loadGame);

            if (oppo != null){
                oppo.getSaved().values().remove(game);
            }

            vm.put("title", "Checkers!");
            vm.put("currentUser", playerName);
            vm.put("viewMode", Game.View.PLAY);
            vm.put("redPlayer", game.getRedPlayer());
            vm.put("whitePlayer", game.getWhitePlayer());
            vm.put("activeColor", game.getActiveColor());
            vm.put("board", game.getBoardView());
            vm.put("flip", game.getRedPlayer().getName().equals(playerName));
            response.redirect(WebServer.GAME_URL);
            return templateEngine.render(new ModelAndView(vm, "game.ftl"));
        }

        request.session().attribute("message", Message.error("Selected Player is in a Game"));
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}

