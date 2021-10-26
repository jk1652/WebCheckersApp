package com.webcheckers.model;

/**
 * @author David Pritchard
 */
public class Game {
	private static int GAME_COUNTER = 0;
	public enum View {PLAY, SPECTATOR, REPLAY}
	private Player currentUser, redPlayer, whitePlayer;
	private Piece.Color activeColor;
	private Board board;
	private int gameID;

	
	/**
	 * A value object for efficient storage of game information.
	 * @param redPlayer the player using the red checker pieces.
	 * @param whitePlayer the player using the white checker pieces.
	 */
	public Game(Player redPlayer, Player whitePlayer) {
		this.redPlayer = redPlayer;
		this.whitePlayer = whitePlayer;
		activeColor = Piece.Color.RED;
		board = new Board();
		synchronized(this) { // protect shared resource
			gameID = GAME_COUNTER;
			GAME_COUNTER++;
		}
		
		
	}
	
	/**
	 * Checks if a player can interact with the game in a certain view mode.
	 * @param viewMode the method in which the player will interact.
	 * @param player the player interacting with the game.
	 */
	public boolean isPermittedViewMode(View viewMode, Player player) {
		// TODO add check for game over? return true if mode = REPLAY
		if (player.equals(redPlayer) || player.equals(whitePlayer))
			return viewMode.equals(View.PLAY);
		else
			return viewMode.equals(View.SPECTATOR);
	}

	public boolean validateMove(Move move){
		return true;
	}

	public Boolean undoMove(){
		return true;
	}

	public void submitMove(){

	}

	public int getMoveSize(){
		return 0;
	}

	
	/**
	 * Check if a player is a participant in this game.
	 * @return true if the player is a participant.
	 */
	public boolean isParticipant(Player player) {
		return player.equals(redPlayer) || player.equals(whitePlayer);
	}
	/**
	 * @return the gameID, a unique integer.
	 * 
	 */
	public int getGameID() {
		return gameID;
	}
	
	public Piece.Color getActiveColor() {
		return activeColor;
	}
	
	public Player getRedPlayer() {
		return redPlayer;
	}
	
	public Player getWhitePlayer() {
		return whitePlayer;
	}
	
	public Board getBoardView() {
		return board;
	}
}
