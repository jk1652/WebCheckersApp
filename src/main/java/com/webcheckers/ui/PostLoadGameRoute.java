package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameManager;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

import com.webcheckers.util.Message;
import spark.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostLoadGameRoute implements Route {
    public static final String OPPONENT_NAME = "opponentName";
    public static final String GAME_ID_ATTRIBUTE = "gameID";
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;
    private final TemplateEngine templateEngine;
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * The constructor for the {@code POST /game} route handler.
     * @param templateEngine
     * @param playerLobby
     * @param gameManager
     */
    PostLoadGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby,
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
     * @param request
     * @param response
     * @return game info
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {


        String playerName = request.session().attribute(PostSignInRoute.USERNAME);
        //String opponentName = request.queryParams(OPPONENT_NAME);

        Set<String> attr = request.attributes();
        for (String x: attr){
            LOG.fine("attr: " + x);
        }

        String loadGame = request.queryParams("1");
        LOG.fine("load game: " + loadGame);
        Integer gameID = request.session().attribute(GAME_ID_ATTRIBUTE);

        Player user = playerLobby.getPlayer(playerName);


        Set<String> keys = user.getSaved().keySet();

        int i = 1;
        for (String x: keys){
            String test = request.queryParams(Integer.toString(i));
            i++;
            if (test != null){
                loadGame = x;
                break;
            }
        }

        Game game = gameManager.LoadGame(user.getSaved().get(loadGame), user);

        if (game != null) {

            Player oppo = playerLobby.getPlayer(game.getOpponentName(playerName));

            System.out.println(game.getOpponentName(playerName));

            Map<String, Object> vm = new HashMap<>();

            if (!game.getOpponentName(playerName).equals("CPU")) {
                System.out.println("checking player");
                if (playerLobby.getPlayer(game.getOpponentName(playerName)) == null) {
                    gameManager.finishGame(playerName);
                    request.session().attribute("message", Message.error("Selected Player is not Online"));
                    response.redirect(WebServer.HOME_URL);
                    return null;
                }
            }

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

