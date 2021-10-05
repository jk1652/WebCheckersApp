package com.webcheckers.appl;

import java.util.ArrayList;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
/**
 * @author David Pritchard
 */
public class GameManager {
	private ArrayList<Game> active, inactive;

	public GameManager() {
		active = new ArrayList<>();
		inactive = new ArrayList<>();
	}

	/**
	 * Checks if the player is in any active games.
	 * @return the Game object the player is a participant in and null if the player is not a participant.
	 */
	public Game findPlayerGame(Player player) {
		for (Game game : active) {
			if (game.isParticipant(player))
				return game; // Player is in this game.
		}
		return null; //Not in any game.
	}

	public Game findPlayerGame(String playerName) {
		return findPlayerGame(new Player(playerName));
	}
	
	public Game createGame(Player redPlayer, Player whitePlayer) {
		Game game = new Game(redPlayer, whitePlayer);
		active.add(game);
		return game;
	}
	
	public Game createGame(String redPlayer, String whitePlayer) {
		return createGame(new Player(redPlayer), new Player(whitePlayer));
	}
	
	private Game getGame(int gameID, ArrayList<Game> collection) {
		for (Game game : collection) {
			if (game.getGameID() == gameID)
				return game;
		}
		return null;
	}
	
	public Game getActiveGame(int gameID) {
		return getGame(gameID, active);
	}
	
	public Game getInactiveGame(int gameID) {
		return getGame(gameID, inactive);
	}
}
