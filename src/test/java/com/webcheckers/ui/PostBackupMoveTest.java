package com.webcheckers.ui;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Position;
import com.webcheckers.util.Message;
import spark.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class PostBackupMoveTest {
    private TemplateEngine templateEngine;
    private GameManager gameManager;

    private PostBackupMove CuT;

	private Request request;
	private Session session;
	private Response response;
    private Gson gson;

    @BeforeEach
    public void setup() {
		request = mock(Request.class);
		session = mock(Session.class);
		when(request.session()).thenReturn(session);
		response = mock(Response.class);
		gameManager = new GameManager();
        gson = new GsonBuilder().create();
        templateEngine = mock(TemplateEngine.class);
        CuT = new PostBackupMove(templateEngine, gameManager);
    }

    /**
     * Test when there is a move to back up and when there is not.
     */
    @Test
    public void testBackupMove() {
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("a");
        Game game = gameManager.createGame("a", "b");
        try {
            String error = (String) CuT.handle(request, response);
            assertEquals(gson.toJson(Message.error("No Moves in Stack")), error);
            game.makeMove(new Move(new Position(2,1), new Position(3, 2)));
            assertEquals(gson.toJson(Message.info("Success, Backed up move")), CuT.handle(request, response));
        } catch(Exception e) {}
    }
}
