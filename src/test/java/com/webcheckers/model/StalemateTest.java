package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class StalemateTest {
    private GameManager gameManager;
    private PlayerLobby playerLobby;    

    /**
     * Instantiate needed objects.
     */
    @BeforeEach
    public void setup() {
        gameManager = new GameManager();
        playerLobby = new PlayerLobby();
    }

    /**
	 * Create a stalemate condition in the game.
	 * The white piece can't move anywhere in this condition.
	 */
	@Test
	public void testStalemateSingle() { 
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
	}

    /**
     * Create a stalemate in the game.
     * The king can't move forwards or backwards.
     */
    @Test
	public void testStalemateKing() { 
		playerLobby.addPlayer("test1");
		playerLobby.addPlayer("test2");
		Game game = gameManager.createGame("test1", "test2");
		Board board = game.getBoardView();
		board.clearBoard();
        // upper movement
		board.placePiece(0, 0, new Piece(Piece.Type.SINGLE, Piece.Color.RED));
		board.placePiece(0, 2, new Piece(Piece.Type.SINGLE, Piece.Color.RED));
        // moving piece
		board.placePiece(1, 1, new Piece(Piece.Type.KING, Piece.Color.WHITE));
        // lower movement
        board.placePiece(2, 0, new Piece(Piece.Type.SINGLE, Piece.Color.RED));
        board.placePiece(2, 2, new Piece(Piece.Type.SINGLE, Piece.Color.RED));
        board.placePiece(3, 3, new Piece(Piece.Type.SINGLE, Piece.Color.RED));
		game.setActiveColor(Piece.Color.WHITE);
		assertTrue(game.checkStalemate());
	}

    @Test
    public void testNoStalemate() {
		playerLobby.addPlayer("test1");
		playerLobby.addPlayer("test2");
		Game game = gameManager.createGame("test1", "test2");
		assertFalse(game.checkStalemate());
    }
}
