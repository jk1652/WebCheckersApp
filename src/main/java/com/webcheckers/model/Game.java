package com.webcheckers.model;

public class Game {
	private static int GAME_COUNTER = 0;
	public enum View {PLAY, SPECTATOR, REPLAY}
	private Player currentUser, redPlayer, whitePlayer;
	private Piece.Color activeColor;
	public View viewMode;
	private Board board;
	private int gameID;
	
	public Game(Player currentUser, Player redPlayer, Player whitePlayer, Board board) {
		this.currentUser = currentUser;
		this.redPlayer = redPlayer;
		this.whitePlayer = whitePlayer;
		viewMode = View.PLAY; // always PLAY until a later story is added.
		synchronized(this) { // protect shared resource
			gameID = GAME_COUNTER;
			GAME_COUNTER++;
		}
		
		
	}
	
	public View getViewMode() {
		return viewMode;
	}
	
	public int getGameID() {
		return gameID;
	}
	
	public Piece.Color getActiveColor() {
		return activeColor;
	}
	
	public Player getCurrentUser() {
		return currentUser;
	}
	
	public Player getRedPlayer() {
		return redPlayer;
	}
	
	public Player getWhitePlayer() {
		return whitePlayer;
	}
	
	public Board getBoard() {
		return board;
	}
}
