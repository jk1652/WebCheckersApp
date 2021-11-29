package com.webcheckers.ui;
import com.webcheckers.appl.PlayerLobby;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestEngine;

import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class GetGameRouteTest {
	private Request request;
	private Session session;
	private Response response;
	private TemplateEngine engine;
	
	private PlayerLobby playerLobby;
	private GameManager gameManager;

	private TemplateEngineTester testHelper;
	private GetGameRoute CuT;
	private Gson gson;

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
		gson = new GsonBuilder().create();
		testHelper = new TemplateEngineTester();
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
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

		CuT.handle(request, response);
		testHelper.assertViewModelExists();
		testHelper.assertViewModelIsaMap();
	}

	/**
	 * Test that the player is redirected to the homepage after a stalemate.
	 */
	@Test
	public void testStalemate() {
		// create stalemate
		playerLobby.addPlayer("test1");
		playerLobby.addPlayer("test2");
		Game game = gameManager.createGame("test1", "test2");
		Board board = game.getBoardView();
		board.clearBoard();
		board.placePiece(0, 0, new Piece(Piece.Type.SINGLE, Piece.Color.RED));
		board.placePiece(0, 2, new Piece(Piece.Type.SINGLE, Piece.Color.RED));
		board.placePiece(1, 1, new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));
		game.setActiveColor(Piece.Color.WHITE);
		assertTrue(game.checkStalemate());
		// test response
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
		CuT.handle(request, response);
		// Create expected value.
		Map<String, Object> options = new HashMap<>();
		options.put("isGameOver", true);
		options.put("gameOverMessage", "The match has come to a stalemate and cannot proceed. The opposing player will resign and you will be prompted to exit the game again");
		testHelper.assertViewModelAttribute("modeOptionsAsJSON", gson.toJson(options));
	}

	/**
	 * Test that the win message is shown on win.
	 */
	@Test
	public void testWinnerDeclared() {
		testWinning("test1", Piece.Color.RED, "test1 has won and has captured all of the opposing pieces.");
	}

	/**
	 * Test that the loss message is shown on loss.
	 */
	@Test
	public void testLoserDeclared() {
		testWinning("test2", Piece.Color.WHITE, "test2 has won and has captured all of the opposing pieces.");
	}

	private void testWinning(String winner, Piece.Color winningColor, String message) {
		playerLobby.addPlayer("test1");
		playerLobby.addPlayer("test2");
		Game game = gameManager.createGame("test1", "test2");
		// clear board so it's a win and add a red piece.
		Board board = game.getBoardView();
		board.clearBoard();
		board.placePiece(3, 3, new Piece(Piece.Type.SINGLE, winningColor));
		// test response
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
		CuT.handle(request, response);

		Map<String, Object> options = new HashMap<>();
		options.put("isGameOver", true);
		options.put("gameOverMessage", message);
		testHelper.assertViewModelAttribute("modeOptionsAsJSON", gson.toJson(options));
	}

	/**
	 * Test that opponents resigning is handled with the correct output.
	 */
	@Test
	 public void testOpponentResigns() {
		testResign(Piece.Color.WHITE, "test2 has lost by resign.");		
	}

	/**
	 * Test that the player resigning has the correct output.
	 */
	@Test
	public void testPlayerResigns() {
		testResign(Piece.Color.RED, "test1 has lost by resign.");		
	}

	private void testResign(Piece.Color winner, String expectedMessage) {
		playerLobby.addPlayer("test1");
		playerLobby.addPlayer("test2");
		Game game = gameManager.createGame("test1", "test2");
		Board board = game.getBoardView();
		board.setWinner(winner);
		// test response
		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
		CuT.handle(request, response);

		Map<String, Object> options = new HashMap<>();
		options.put("isGameOver", true);
		options.put("gameOverMessage", expectedMessage);
		testHelper.assertViewModelAttribute("modeOptionsAsJSON", gson.toJson(options));
		
	}

	@Test
	public void testNoResign() {
		playerLobby.addPlayer("test1");
		playerLobby.addPlayer("test2");
		Game game = gameManager.createGame("test1", "test2");

	}

	/**
	 * Test that the oppoen
	 */
	@Test
	public void testOpponentSaved() {
		playerLobby.addPlayer("test1");
		playerLobby.addPlayer("test2");
		Game game = gameManager.createGame("test1", "test2");
		playerLobby.getPlayer("test2").saveGame(game);
		playerLobby.getPlayer("test1").savedGamesDidGoUp();
		gameManager.finishGame("test2");
		// test	
		when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test1");
		CuT.handle(request, response);
		verify(session).attribute("message", Message.info("Your opponent has left, but luckily they saved the game against you :)"));
		verify(response).redirect(WebServer.HOME_URL);
	}
}
