package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Position;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Author Zane Kitchen Lipski
 * test PostSubmitTurn
 */
public class PostSubmitTurnTest {
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private GameManager gameManager;

    private PostSubmitTurn CuT;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameManager = new GameManager();
        CuT = new PostSubmitTurn(engine, gameManager);
    }

    /**
     * Active player test
     */
    @Test
    public void Active() {
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test");

        Game game = gameManager.createGame("test", "test2");

        gameManager.findPlayerGame("test");

        game.setActiveColor(Piece.Color.RED);
        game.makeMove(new Move(new Position(2,1), new Position(3, 2)));

        try {
            CuT.handle(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
