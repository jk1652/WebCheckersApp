package com.webcheckers.ui;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

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

@Tag("UI-tier")
class PostGameRouteTest {
	private Request request;
	private Session session;
	private Response response;
	private TemplateEngine engine;
	
	private PlayerLobby playerLobby;
	private GameManager gameManager;

	private PostGameRoute CuT;
	@BeforeEach
	public void setup(){
		request = mock(Request.class);
		session = mock(Session.class);
		when(request.session()).thenReturn(session);
		response = mock(Response.class);
		engine = mock(TemplateEngine.class);
		playerLobby = new PlayerLobby();
		gameManager = new GameManager();
		CuT = new PostGameRoute(engine, playerLobby, gameManager);
	}

	@Test
	public void testNonexistentPlayer() {
		playerLobby.addPlayer("test1");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
		when(session.attribute(PostGameRoute.OPPONENT_NAME)).thenReturn("error");
		try {
			assertNull(CuT.handle(request, response));
		} catch (Exception e) {}
		verify(response).redirect(WebServer.HOME_URL);
	}
	
	@Test
	public void testOccupiedPlayer() {
		playerLobby.addPlayer("test2");
		playerLobby.addPlayer("test3");
		playerLobby.addPlayer("test4");
		gameManager.createGame("test3", "test4");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
		when(session.attribute(PostGameRoute.OPPONENT_NAME)).thenReturn("test2");
				
		try {
			assertNull(CuT.handle(request, response));
		} catch (Exception e) {}
		verify(response).redirect(WebServer.HOME_URL);
	}

	@Test
	public void testSamePlayer() {
		playerLobby.addPlayer("test5");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test5");
		when(request.queryParams(PostGameRoute.OPPONENT_NAME)).thenReturn("test5");
				
		try {
			assertNull(CuT.handle(request, response));
		} catch (Exception e) {}
		verify(response).redirect(WebServer.HOME_URL);
	}

	@Test
	public void testCreateGame() {

		playerLobby.addPlayer("test6");
		playerLobby.addPlayer("test7");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test6");
		when(request.queryParams(PostGameRoute.OPPONENT_NAME)).thenReturn("test7");
		when(session.attribute(PostGameRoute.GAME_ID_ATTRIBUTE)).thenReturn(null);
		final TemplateEngineTester testHelper = new TemplateEngineTester();
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
		try {
			CuT.handle(request, response);
		} catch(Exception e) { }
		
		testHelper.assertViewModelExists();
		testHelper.assertViewModelIsaMap();
	}

	@Test
	public void testInvalidGame() {
		playerLobby.addPlayer("test6");
		playerLobby.addPlayer("test7");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test6");
		when(request.queryParams(PostGameRoute.OPPONENT_NAME)).thenReturn("test7");
		when(session.attribute(PostGameRoute.GAME_ID_ATTRIBUTE)).thenReturn(999);
				
		try {
			assertNull(CuT.handle(request, response));
		} catch (Exception e) {}
		verify(response).redirect(WebServer.HOME_URL);
	}

	@Test
	public void testBusyPlayer() {
		playerLobby.addPlayer("test8");
		playerLobby.addPlayer("test9");
		playerLobby.addPlayer("test0");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test0");
		when(request.queryParams(PostGameRoute.OPPONENT_NAME)).thenReturn("test9");
		gameManager.createGame("test8", "test9");
		try {
			assertNull(CuT.handle(request, response));
		} catch (Exception e) {}
		verify(response).redirect(WebServer.HOME_URL);
	}

	@Test
	public void testEasyAI() {
		testAI("easy");
	}

	@Test
	public void testMediumAI(){
		testAI("med");
	}

	@Test
	public void testHardAI() {
		testAI("hard");
	}
	
	private void testAI(String difficulty) {
		playerLobby.addPlayer("test");
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test");
		when(request.queryParams(difficulty)).thenReturn(difficulty);
		
		try {
			assertNull(CuT.handle(request, response));
		} catch(Exception e) { }
		
	}
}
