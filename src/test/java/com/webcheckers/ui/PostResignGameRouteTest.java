package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
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
 * test PostResignGameRoute
 */
public class PostResignGameRouteTest {
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private GameManager gameManager;
    private  Gson gson;

    private PostResignGameRoute CuT;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameManager = new GameManager();
        gson = new Gson();
        CuT = new PostResignGameRoute(gson, gameManager);
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
}
