package com.webcheckers.ui;

import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.AI;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.Move;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("UI-tier")
public class PostSaveGameRouteTest {
    private GameManager gameManager;
    private PlayerLobby playerLobby;
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;
    private PostSaveGameRoute CuT;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameManager = new GameManager();
        playerLobby = new PlayerLobby();
        GsonBuilder builder = new GsonBuilder();
        CuT = new PostSaveGameRoute(builder.create(), engine, playerLobby, gameManager);
        
        playerLobby.addPlayer("test1");
        playerLobby.addPlayer("test2");
    }

    /**
     * Test that saving does not happen when the player isn't the active player.
     */
    @Test
    public void testInactiveSave() {
        Game game = gameManager.createGame("test1", "test2");
        game.setActiveColor(Piece.Color.WHITE);
        Player player = playerLobby.getPlayer("test2");
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
        try {
            assertNull(CuT.handle(request, response));
        } catch (Exception e) { }
        System.out.println("\n\n\n\n" + player.getSaved() + "\n" + player.getSaved().entrySet().size() + "\n\n\n\n");
        assertEquals(0, player.getSaved().entrySet().size());
        verify(response).redirect(WebServer.GAME_URL);   
    }

    /**
     * Test that saving works when a player is active and hasn't won.
     */
    @Test
    public void testActivePlayerSave() {
        Game game = gameManager.createGame("test1", "test2");
        game.setActiveColor(Piece.Color.RED);
        Player player = playerLobby.getPlayer("test1");
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
        try {
            assertNull(CuT.handle(request, response));
        } catch (Exception e) { }
        assertEquals(1, player.getSaved().size());
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Test that the game isn't saved when there is a winner.
     */
    @Test
    public void testPlayerWonSave() {
        Game game = gameManager.createGame("test1", "test2");
        game.setActiveColor(Piece.Color.RED);
        Board board = game.getBoardView();
        // remove all but one piece
        board.clearBoard();
        board.placePiece(1, 1, new Piece(Piece.Type.SINGLE, Piece.Color.RED));
        Player player = playerLobby.getPlayer("test1");
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
        try {
            assertNull(CuT.handle(request, response));
        } catch (Exception e) { }
        assertEquals(0, player.getSaved().size());
        verify(response).redirect(WebServer.GAME_URL);
    }

    /**
     * Test that moves are undone.
     */
    @Test
    public void testUndoMove() {
        Game game = gameManager.createGame("test1", "test2");
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
        game.makeMove(new Move(new Position(2,1), new Position(3, 2)));
        game.setActiveColor(Piece.Color.RED);
        try {
            assertNull(CuT.handle(request, response));
        } catch (Exception e) { }
        assertEquals(0, game.getMoveSize()); // check moves are undone
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Test that saving is succesful when opponent is an AI.
     */
    @Test
    public void testSaveAI() {
        Game game = gameManager.createGame("test1", AI.difficulty.Easy);
        Player player = playerLobby.getPlayer("test1");
        game.setActiveColor(Piece.Color.RED);
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
        try {
            CuT.handle(request, response);
        } catch(Exception e) { }
        assertEquals(1, player.getSaved().size());
        verify(response).redirect(WebServer.HOME_URL);
    }
}
