package com.webcheckers.ui;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameManager;
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

class GetGameRouteTest {
    /*
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
	
	//@Test
	//public void testWithoutGame() {
	//	playerLobby.addPlayer("test1");
	//	playerLobby.addPlayer("test2");
	//	assertNull(CuT.handle(request, response));
	//}
	
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
*/

}
