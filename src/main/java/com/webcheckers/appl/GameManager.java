package com.webcheckers.appl;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.webcheckers.model.AI;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.ui.WebServer;

/**
 * Holds the game states running on the web server.
 * @author David Pritchard
 */
public class GameManager {
	private static final Logger LOG = Logger.getLogger(WebServer.class.getName());
	private ArrayList<Game> active, inactive;
	private final Object lock = new Object();

	public GameManager() {
		active = new ArrayList<>();
		inactive = new ArrayList<>();
	}

	/**
	 * Checks if the player is in any active games.
	 * @return the Game object the player is a participant in and null if the player is not a participant.
	 */
	public Game findPlayerGame(Player player) {
		Game result = null;
		synchronized(lock) { // Make sure it isn't modified while searching.
			for (Game game : active) {
				if (game.isParticipant(player)) {
					result = game; // Player is in this game.
					break;
				}
			}
		}
		return result;
	}

	public Game findPlayerGame(String playerName) {
		return findPlayerGame(new Player(playerName));
	}
	
	public Game createGame(Player redPlayer, Player whitePlayer) {
		Game game = new Game(redPlayer, whitePlayer);
		synchronized(lock) { // Protect resource
			active.add(game);
		}
		return game;
	}
	
	public Game createGame(String redPlayer, String whitePlayer) {
		return createGame(new Player(redPlayer), new Player(whitePlayer));
	}

	public Game createGame(String redPlayer, AI.difficulty dif) {
		Game game = new Game(new Player(redPlayer), dif);
		synchronized(lock) { // Protect resource
			active.add(game);
		}
		return game;
	}
	
	private Game getGame(int gameID, ArrayList<Game> collection) {
		Game result = null;
		synchronized(lock) { // Don't let the resource get modified yet.
			for (Game game : collection) {
				if (game.getGameID() == gameID) {
					result = game;
					break;
				}
			}
		}
		return result;
	}

	public void finishGame(String playerName){
		// might need a way to tell if player won game
		LOG.config(playerName + " deleted game");
		Game del = findPlayerGame(playerName);
		active.remove(del);
	}

	public Game LoadGame(Game game){
		LOG.config("load game");
		active.add(game);
		return game;
	}


	/**
	 * @param gameID the ID of the game to return
	 * @return the Game object with that ID in the active games or null if no such game exists.
	 */
	public Game getActiveGame(int gameID) {
		return getGame(gameID, active);
	}
	
	/**
	 * @param gameID the ID of the game to return
	 * @return the Game object with that ID in the inactive games or null if no such game exists.
	 */
	public Game getInactiveGame(int gameID) {
		return getGame(gameID, inactive);
	}
}
