package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class PostLoadGameRouteTest {
    private PostLoadGameRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;

    private PlayerLobby playerLobby;
    private GameManager gameManager;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = new PlayerLobby();
        gameManager = new GameManager();
        CuT = new PostLoadGameRoute(engine, playerLobby, gameManager);
    }

    /**
     * Test for redirect to homepage when the other player in a saved game
     * is busy.
     */
    @Test
    public void testBusyOpponent() {
        playerLobby.addPlayer("test1");
        playerLobby.addPlayer("test2");
        playerLobby.addPlayer("other");
        Player player1 = playerLobby.getPlayer("test1");
        Player player2 = playerLobby.getPlayer("test2");
        // create and save game
        Game game = gameManager.createGame("test1", "test2");
        String gameKey = player1.saveGame(game);
        player2.saveGame(game);
        gameManager.finishGame("test2");
        // put opponent in a new game
        gameManager.createGame("test2", "other");
        // test
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
        when(request.queryParams("1")).thenReturn(gameKey);
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify(response, atLeast(1)).redirect(WebServer.HOME_URL);
    }

    /**
     * Test game redirect when other player is idle.
     */
    @Test
    public void testIdleOpponent() {
        playerLobby.addPlayer("test1");
        playerLobby.addPlayer("test2");
        Player player1 = playerLobby.getPlayer("test1");
        Player player2 = playerLobby.getPlayer("test2");
        // create and save game
        Game game = gameManager.createGame("test1", "test2");
        String gameKey = player1.saveGame(game);
        player2.saveGame(game);
        gameManager.finishGame("test1");

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
        when(request.queryParams("1")).thenReturn(gameKey);

        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        verify(response, atLeast(1)).redirect(WebServer.GAME_URL);
    }

    /**
     * Ensure the player is sent back to the homepage if the opponent is offline.
     */
    @Test
    public void testOfflineOpponent() {
        playerLobby.addPlayer("test1");
        playerLobby.addPlayer("test2");
        Player player1 = playerLobby.getPlayer("test1");
        Player player2 = playerLobby.getPlayer("test2");
        // create and save game
        Game game = gameManager.createGame("test1", "test2");
        String gameKey = player1.saveGame(game);
        player2.saveGame(game);

        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
        when(request.queryParams("1")).thenReturn(gameKey);
        // Remove the other player.
        gameManager.finishGame("test1");
        playerLobby.removePlayer("test2");
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify(response).redirect(WebServer.HOME_URL);
    }
}