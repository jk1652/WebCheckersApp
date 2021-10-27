package com.webcheckers.model;

import java.util.ArrayList;

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
	private ArrayList<Board>  validatedMoves = new ArrayList<>();
	private ArrayList<Move> pastMoves = new ArrayList<>();
	private String validity;


	
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

	/**
	 * Checks the validity of movement
	 * @param move the move the player made
	 * @return true if move is valid / False if move isn't
	 */
	public boolean validateMove(Move move){
		//Retrieve the starting position
		Position start_pos = move.getStart();
		int initRow = start_pos.getRow();
		int initCol = start_pos.getCol();

		//Get the space according to the position
		Space start_space = board.getRow(initRow).getSpace(initCol);

		//Get the piece on that start position
		Piece piece = start_space.getPiece();

		//Check if the piece is doing a valid move
		if(move.isMove()){ return piece.isValidMove(move); }

		//check if there's a piece between start and end

		//get final position of piece
		Position final_pos = move.getEnd();
		int finalRow = final_pos.getRow();
		int finalCol = final_pos.getCol();

		//get space where piece should be
		int midRow = ((initRow + finalRow)/2);
		int midCol = ((initCol + finalCol)/2);
		Space mid_space = board.getRow(midRow).getSpace(midCol);

		//check if there's a piece of opposite color
		if(mid_space.getPiece()!= null && mid_space.getPiece().getColor() != activeColor){
			return piece.isValidJump(move);
		}
		return false;
	}

	public Boolean undoMove(){
		if(validatedMoves.size() != 0){
			board = validatedMoves.remove(validatedMoves.size() - 1);
			return true;
		}
		return false;
	}

	public void submitMove(){
		board = new Board(board);
		validatedMoves = new ArrayList<Board>();
		pastMoves = new ArrayList<Move>();
		swapActiveColor();
	}

	public void makeMove(Move move){
		validatedMoves.add(board);
		Board copyBoard = new Board(board);
		Piece temp = copyBoard.getRow(move.getStart().getRow()).getSpace(move.getStart().getCol()).getPiece();
		copyBoard.getRow(move.getStart().getRow()).getSpace(move.getStart().getCol()).setPiece(null);
		copyBoard.getRow(move.getEnd().getRow()).getSpace(move.getEnd().getCol()).setPiece(temp);
		if(move.isJump()){
			copyBoard.getRow((move.getStart().getRow() + move.getEnd().getRow()) / 2).getSpace((move.getStart().getCol() + move.getEnd().getCol() / 2)).setPiece(null);
		}
		board = copyBoard;
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
	
	public Piece.Color getWinner() {
		return board.getWinner();
	}

	public String getOpponentName(String playerName) {
		if (playerName.equals(redPlayer.getName()))
			return whitePlayer.getName();
		else
			return redPlayer.getName();
	}
	
	public Piece.Color getUserColor(String username) {
		if (username.equals(redPlayer.getName()))
			return Piece.Color.RED;
		else
			return Piece.Color.WHITE;
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

	public void swapActiveColor(){
		if (this.activeColor == Piece.Color.RED) {
			this.activeColor = Piece.Color.WHITE;
		}
		else {
			this.activeColor = Piece.Color.RED;
		}
	}


}
