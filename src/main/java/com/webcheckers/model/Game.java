package com.webcheckers.model;

import java.util.ArrayList;

/**
 * @author David Pritchard
 * @author Quentin Ramos
 * @author Spencer Creveling
 * @author Zane Kitchen Lipski
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

		//Makes sure you can't move again
		if(pastMoves.size() > 0 && pastMoves.get(pastMoves.size() - 1).isMove()){
			validity = "you cant move again";
			return false;
		}

		//Makes sure you can't perform a simple move after a jump
		if(pastMoves.size() > 0 && pastMoves.get(pastMoves.size() - 1).isJump() && move.isMove()){
			validity = "you can not simple move after a jump";
			return false;
		}

		//Ensures you are moving the same piece
		if(pastMoves.size() > 0 && !move.getStart().equals(pastMoves.get(pastMoves.size() - 1).getEnd())){
			validity = "you can not move two pieces";
			return false;
		}

		//Check if the piece is doing a jump move
		if(forceJump() && move.isMove()) {
			validity = "There is a jump available!";
			return false;
		}
		else{
			if(move.isMove()){
				if(piece.isValidMove(move)){
					validity = "Simple move is valid.";
					return true;
				}
				else{
					validity = "Your piece can't move backwards.";
					return false;}
			}
		}

		//check if there's a piece between start and end

		//get final position of piece
		Position final_pos = move.getEnd();
		int finalRow = final_pos.getRow();
		int finalCol = final_pos.getCol();

		//get space where piece should be
		int midRow = ((initRow + finalRow)/2);
		int midCol = ((initCol + finalCol)/2);
		Space mid_space = board.getRow(midRow).getSpace(midCol);

		//check if move is a jump and if another jump is available
		if(move.isJump() && forceJump()){
			//check if jumped over piece exists
			if(mid_space.getPiece()== null) {
				validity = "You can't jump over nothing.";
			}
			else{
				//check if jumped over piece is the same color
				if (mid_space.getPiece().getColor() == activeColor){
					validity = "You can't jump over your own piece.";
				}
				else{
					//check if jump was successful
					if(piece.isValidJump(move)){
						validity = "Jump was successful.";
						return true;
					}
					validity = "You can't jump backwards.";
				}
			}
			return false;
		}
		return false;
	}

	/**
	 * rolls back the board state to a previous position and
	 * rolls back the last move in pastMove
	 * @return if the undo was successful
	 */
	public Boolean undoMove(){
		if(validatedMoves.size() != 0){
			board = validatedMoves.remove(validatedMoves.size() - 1);
			pastMoves.remove(pastMoves.size()-1);
			return true;
		}
		return false;
	}

	/**
	 * clears the validatedMoves and pastMoves ensures the current board is
	 * a deep copy and not a pointer then calls swapActiveColor();
	 */
	public void submitMove(){
		board = new Board(board);
		validatedMoves = new ArrayList<>();
		pastMoves = new ArrayList<>();
		swapActiveColor();
	}

	/**
	 * assumes any move given is legal and execute the move
	 * as well as removing any pieces that would be captured.
	 * @param move A valid move to be executed
	 */
	public void makeMove(Move move){
		pastMoves.add(move);
		validatedMoves.add(board);
		Board copyBoard = new Board(board);

		//Get initial and final spaces
		Space initSpace = copyBoard.getRow(move.getStart().getRow()).getSpace(move.getStart().getCol());
		Space endSpace = copyBoard.getRow(move.getEnd().getRow()).getSpace(move.getEnd().getCol());

		//Get the piece on the initial space
		Piece initPiece = initSpace.getPiece();

		if(move.isMove()){
			//set original space to null
			initSpace.setPiece(null);

			//set end space to initial piece
			endSpace.setPiece(initPiece);
		}
		if(move.isJump()){
			//set original space to null
			initSpace.setPiece(null);

			//set space in between to null
			int midRow = ((move.getStart().getRow() + move.getEnd().getRow())/2);
			int midCol = ((move.getStart().getCol() + move.getEnd().getCol())/2);
			Space midSpace = copyBoard.getRow(midRow).getSpace(midCol);
			midSpace.setPiece(null);

			//set end space to initial piece
			endSpace.setPiece(initPiece);
		}
		board = copyBoard;
	}

	/**
	 * checks all peices of active color and check if
	 * there is a capture that can be made
	 * @return if there is a forced jump
	 */
	public boolean forceJump() {

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Piece target = board.getRow(x).getSpace(y).getPiece();
				// all these are holders to make code more readable
				Space topRight = null;
				Space bottomRight = null;
				Space topLeft = null;
				Space bottomLeft = null;
				//checks to make sure there is a piece
				if(target != null){
					//checks to make sure the jump in this direction wont lead out of bounds
					if(x < 6 && y < 6){
						bottomRight = board.getRow(x+1).getSpace(y+1);
					}
					//checks to make sure the jump in this direction wont lead out of bounds
					if(x < 6 && y > 1){
						bottomLeft = board.getRow(x+1).getSpace(y-1);
					}
					//checks to make sure the jump in this direction wont lead out of bounds
					if(x > 1 && y < 6){
						topRight = board.getRow(x-1).getSpace(y+1);
					}
					//checks to make sure the jump in this direction wont lead out of bounds
					if(x > 1 && y > 1){
						topLeft = board.getRow(x-1).getSpace(y-1);
					}
					//checks see if the piece can jump down ie red pieces and kings
					if(target.getColor() == Piece.Color.RED || (target.getType() == Piece.Type.KING && target.getColor() == activeColor)){
						//checks to see that the peice in the jump path exists and is not the same color
						if(bottomLeft!= null && bottomLeft.getPiece() != null && bottomLeft.getPiece().getColor() != target.getColor()){
							//checks to see that the landing square is not occupied
							if(board.getRow(x+2).getSpace(y-2).isValid()){
								return true;
							}
						}
						//checks to see that the peice in the jump path exists and is not the same color
						if(bottomRight!= null && bottomRight.getPiece() != null && bottomRight.getPiece().getColor() != target.getColor()){
							//checks to see that the landing square is not occupied
							if(board.getRow(x+2).getSpace(y+2).isValid()){
								return true;
							}
						}
					}
					//checks see if the piece can jump down ie white pieces and kings
					if(target.getColor() == Piece.Color.WHITE || (target.getType() == Piece.Type.KING && target.getColor() == activeColor)){
						//checks to see that the peice in the jump path exists and is not the same color
						if(topRight!= null && topRight.getPiece() != null && topRight.getPiece().getColor() != target.getColor()){
							if(board.getRow(x-2).getSpace(y+2).isValid()){
								return true;
							}
						}
						//checks to see that the peice in the jump path exists and is not the same color
						if(topLeft!= null && topLeft.getPiece() != null && topLeft.getPiece().getColor() != target.getColor()){
							//checks to see that the landing square is not occupied
							if(board.getRow(x-2).getSpace(y-2).isValid()){
								return true;
							}
						}
					}

				}
			}
		}
		return false;
	}

	/**
	 * checks if a peice has made it to the end row and if so
	 * if becomes a king
	 * @param move where the piece is moving too
	 */
	public void setKing(Move move){
		Piece piece = board.getRow(move.getEnd().getRow()).getSpace(move.getEnd().getCol()).getPiece();
		if(activeColor == Piece.Color.RED && move.getEnd().getRow() == 7){piece.setKing();}
		if(activeColor == Piece.Color.WHITE && move.getEnd().getRow() == 0){piece.setKing();}
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

	/**
	 *
	 * @return the color of the winner
	 */
	public Piece.Color getWinner() {
		return board.getWinner();
	}

	/**
	 *
	 * @param playerName the name of the player of interest
	 * @return the name of the other player in this game
	 */
	public String getOpponentName(String playerName) {
		if (playerName.equals(redPlayer.getName()))
			return whitePlayer.getName();
		else
			return redPlayer.getName();
	}

	/**
	 *
	 * @param username the player of interest
	 * @return the color of said player
	 */
	public Piece.Color getUserColor(String username) {
		if (username.equals(redPlayer.getName()))
			return Piece.Color.RED;
		else
			return Piece.Color.WHITE;
	}

	/**
	 *
	 * @return which color is currently allowed to move
	 */
	public Piece.Color getActiveColor() {
		return activeColor;
	}

	/**
	 *
	 * @return the red player
	 */
	public Player getRedPlayer() {
		return redPlayer;
	}

	/**
	 *
	 * @return the white player
	 */
	public Player getWhitePlayer() {
		return whitePlayer;
	}

	/**
	 *
	 * @return the current most valid state of the board
	 */
	public Board getBoardView() {
		return board;
	}

	/**
	 *
	 * @return the string message about the validity of the last submitted move
	 */
	public String getValidity(){
		return validity;
	}

	/**
	 * switches which color is active
	 */
	public void swapActiveColor(){
		if (this.activeColor == Piece.Color.RED) {
			this.activeColor = Piece.Color.WHITE;
		}
		else {
			this.activeColor = Piece.Color.RED;
		}
	}


}
