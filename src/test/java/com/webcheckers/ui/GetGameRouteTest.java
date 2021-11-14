package com.webcheckers.ui;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import com.webcheckers.model.Game;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class GetGameRouteTest {
	private Request request;
	private Session session;
	private Response response;
	private TemplateEngine engine;
	
	private PlayerLobby playerLobby;
	private GameManager gameManager;

	private GetGameRoute CuT;
	@BeforeEach
	public void setup(){
		request = mock(Request.class);
		session = mock(Session.class);
		when(request.session()).thenReturn(session);
		response = mock(Response.class);
		engine = mock(TemplateEngine.class);
		playerLobby = new PlayerLobby();
		gameManager = new GameManager();
		CuT = new GetGameRoute(engine, playerLobby, gameManager);
	}

	/**
	 * Tests redirect to homepage when a game doesn't exist.
	 */
	@Test
	public void testNoGame() {
		playerLobby.addPlayer("notplaying");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("notplaying");
		CuT.handle(request, response);
		verify(response).redirect(WebServer.HOME_URL);
	}
    /**
	 * Sets the exit flag to true and tests whether the game is closed.
	 */
	@Test
	public void testExitState() {
		playerLobby.addPlayer("test3");
		playerLobby.addPlayer("test4");
		Game game = gameManager.createGame("test3", "test4");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test3");
		game.getBoardView().setExitState(true);
		CuT.handle(request, response);
		assertNull(gameManager.findPlayerGame("test3"));
		assertNull(gameManager.findPlayerGame("test4"));
	}

	@Test
	public void testWithGame() {
		playerLobby.addPlayer("test1");
		playerLobby.addPlayer("test2");
		gameManager.createGame("test1", "test2");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
		final TemplateEngineTester testHelper = new TemplateEngineTester();
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

		CuT.handle(request, response);
		testHelper.assertViewModelExists();
		testHelper.assertViewModelIsaMap();
	}


}
