package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @Author Zane Kitchen Lipski
 * test PostValidateMove
 */
public class PostValidateMoveTest {
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private GameManager gameManager;

    private PostValidateMove CuT;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameManager = new GameManager();
        CuT = new PostValidateMove(engine, gameManager);
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
     * Valid Move test
     * TODO
     */
    @Test
    public void ValidMove() {
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test");

        Game game = gameManager.createGame("test", "test2");
        gameManager.findPlayerGame("test");
        Gson gson = new Gson();
        Move move = gson.fromJson(request.queryParams("actionData"),Move.class);

        try {
            CuT.handle(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
