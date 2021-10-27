package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckTurnTest {
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private GameManager gameManager;

    private PostCheckTurn CuT;
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameManager = new GameManager();
        CuT = new PostCheckTurn(engine, gameManager);
    }

    /**
     * When logged in and a existing game is in view
     */
    @Test
    public void GameExists() {
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test");

        Game game = gameManager.createGame("test", "test2");

        gameManager.findPlayerGame("test");

        try {
            CuT.handle(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Active player test
     */
    @Test
    public void Active() {
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test");

        Game game = gameManager.createGame("test", "test2");

        gameManager.findPlayerGame("test");

        Piece.Color activecolor = game.getActiveColor();

        game.submitMove();

        try {
            CuT.handle(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Board has been exited test
     */
    @Test
    public void BoardTest() {
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test");

        Game game = gameManager.createGame("test", "test2");

        gameManager.findPlayerGame("test");

        Piece.Color activecolor = game.getActiveColor();

        Board board = game.getBoardView();

        board.setWinner(Piece.Color.RED);
        board.getWinner();
        assertTrue(board.getExitState());

        try {
            CuT.handle(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
