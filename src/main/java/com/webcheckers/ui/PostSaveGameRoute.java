package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @Author Zane Kitchen Lipski
 * @Author Jaden Kitchen Lipski
 * PostResignGameRoute
 */
public class PostSaveGameRoute implements Route {
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /save} route handler.
     *
     * @param playerLobby The player lobby
     * @param gameManager the game manager
     */
    public PostSaveGameRoute(final PlayerLobby playerLobby, final GameManager gameManager){
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
    }

    /**
     * Saves game for current player and opponent, closes game
     * @param request the HTTP request
     * @param response the HTTP response
     * @return resign game state json message
     * @throws Exception exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        String playerName = request.session().attribute(PostSignInRoute.USERNAME);

        Player player = playerLobby.getPlayer(playerName);

        Game game = gameManager.findPlayerGame(playerName);

        //save game for player
        player.saveGame(game);

        Player otherPlayer = playerLobby.getPlayer(game.getOpponentName(playerName));



        //if inactive player asks to save do nothing
        Piece.Color userColor = game.getUserColor(playerName);
        if (userColor != game.getActiveColor()){
            response.redirect(WebServer.GAME_URL);
            return null;
        }
        else if (game.getWinner() != null){ // do nothing if game has a winner
            response.redirect(WebServer.GAME_URL);
            return null;
        }
        else {
            player.saveGame(game);
        }
        // if any moves has occurred do nothing
        if (game.getMoveSize() > 0) {
            for(int i = game.getMoveSize(); i > 0; i--){
                game.undoMove();
            }
        }

        // check if playing against an actual other person (not ai)
        if (otherPlayer != null) {
            //save game for opponent
            otherPlayer.saveGame(game);
            otherPlayer.savedGamesDidGoUp();
            request.session().attribute("message", Message.info("Your game against " +
                    otherPlayer.getName() + " was Saved"));
        } else {
            request.session().attribute("message", Message.info("Your game against the " +
                    game.getAIOpponentDifficulty() + " AI was Saved"));
        }
        // properly close game
        gameManager.finishGame(playerName);
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
