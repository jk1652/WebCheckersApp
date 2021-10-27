package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostSignInRouteTest {
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private PlayerLobby playerLobby;

    private PostSignInRoute CuT;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = new PlayerLobby();
        CuT = new PostSignInRoute(playerLobby, engine);
    }

    /**
     * Lobby test
     */
    @Test
    public void Lobby() {
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test");
        when(request.queryParams(PostSignInRoute.USERNAME)).thenReturn("test");

        final String name = request.queryParams(PostSignInRoute.USERNAME);

        playerLobby.addPlayer(name);


        try {
            CuT.handle(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test when user puts in invalid name
     */
    @Test
    public void InvalidNameLobby() {
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("*");
        when(request.queryParams(PostSignInRoute.USERNAME)).thenReturn("*");

        final String name = request.queryParams(PostSignInRoute.USERNAME);

        playerLobby.addPlayer(name);


        try {
            CuT.handle(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
