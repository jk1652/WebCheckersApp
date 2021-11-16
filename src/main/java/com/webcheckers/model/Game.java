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
	private ArrayList<Move> possibleJumps = new ArrayList<>();
	private Boolean SinglePlayer = false;
	private AI.difficulty difficulty;
	private boolean promotion = false;


	
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

	public Game(Player redPlayer,AI.difficulty dif ){
		this.redPlayer = redPlayer;
		this.whitePlayer = new AI(dif);
		difficulty = dif;
		SinglePlayer = true;
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
		if (move == null)
			return false;
		//Retrieve the starting position
		Position start_pos = move.getStart();
		int initRow = start_pos.getRow();
		int initCol = start_pos.getCol();

		if(promotion){
			validity = "you can not move after a promotion";
			return false;
		}

		//Get the space according to the position
		Space start_space = board.getRow(initRow).getSpace(initCol);

		//Get the piece on that start position
		Piece piece = start_space.getPiece();

		//get final position of piece
		Position final_pos = move.getEnd();
		int finalRow = final_pos.getRow();
		int finalCol = final_pos.getCol();

		//get middle space where piece should be for jump
		int midRow = ((initRow + finalRow)/2);
		int midCol = ((initCol + finalCol)/2);
		Space mid_space = board.getRow(midRow).getSpace(midCol);

		//Makes sure you can't do a simple move again
		if(pastMoves.size() > 0 && pastMoves.get(pastMoves.size() - 1).isMove()){
			validity = "you can not move again";
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

		//If a simple move is tried while a jump is available
		if(forceJump() && move.isMove()) {
			validity = "There is a jump available";
			return false;
		}
		//Check piece type and validity of move
		else if(move.isMove()){
			//Check piece type and validity of move
			if(piece.isValidMove(move)){
				validity = "Valid simple move performed.";
				return true;
			}
			//Single tried moving backwards
			else{
				validity = "Your piece can't move backwards.";
				return false;
			}
		}
		//If a jump is tried
		else if(move.isJump()){
			//check if jumped over piece exists
			if(mid_space.getPiece() == null) {
				validity = "You can't jump over nothing.";
			}
			//check if jumped over piece is the same color
			else if(mid_space.getPiece().getColor() == activeColor){
				validity = "You can't jump over your own piece.";
			}

			else{
				//check if jump was successful
				if(piece.isValidJump(move)){
					validity = "Valid jump move performed.";
					return true;
				}
				//Single tried jumping back
				validity = "You can't jump backwards.";
			}
		}
		//If move is not jump or move
		else{
			validity = "Invalid movement, do a valid checkers move";
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
			if(promotion){ promotion = false; }
			return true;
		}
		return false;
	}

	/**
	 * clears the validatedMoves and pastMoves ensures the current board is
	 * a deep copy and not a pointer then calls swapActiveColor();
	 */
	public void submitMove(){
		if(!checkStalemate()){
			promotion = false;
			board = new Board(board);
			validatedMoves = new ArrayList<>();
			pastMoves = new ArrayList<>();
			possibleJumps = new ArrayList<>();
			swapActiveColor();
			if(activeColor.equals(Piece.Color.WHITE) && SinglePlayer){
				if(whitePlayer instanceof AI){
					AI ai = (AI)whitePlayer;
					ai.AIMakeMove(this);
				}
			}
		}
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
		else if(move.isJump()){
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
	 * checks all pieces of active color and checks if
	 * there is a capture that can be made
	 * @return if there is a forced jump
	 */
	public boolean forceJump() {
		ArrayList<Boolean> booleans = new ArrayList<>();
		for (int x = 0; x < 8; x++) {for (int y = 0; y < 8; y++) {
			Piece target = board.getRow(x).getSpace(y).getPiece();
			if(target != null && target.getColor() == activeColor){booleans.add(canJump(new Position(x,y)));}
		}}
		for (Boolean temp : booleans) {if (temp) {return true;}}
		return false;
	}

	/**
	 * checks to see if a piece can jump | adds jump to list for CPU if possible
	 * @param start starting position of a piece
	 * @return true if piece can jump, false if it can't
	 */
	public boolean canJump(Position start){
		Piece current = board.getRow(start.getRow()).getSpace(start.getCol()).getPiece();
		ArrayList<Boolean> booleans = new ArrayList<>();
		//red singles or king pieces
		if(start.getRow() + 2 <= 7){
			if(start.getCol() + 2 <= 7){
				Position end = new Position(start.getRow()+2,start.getCol()+2);
				Space endRightRow = board.getRow(start.getRow()+2).getSpace(start.getCol()+2);
				Space midRightRow = board.getRow(start.getRow()+1).getSpace(start.getCol()+1);
				if(midRightRow.getPiece()!=null && midRightRow.getPiece().getColor() != current.getColor() &&
						endRightRow.isValid()){
					if(current.getColor() == Piece.Color.RED || current.getType()== Piece.Type.KING){
						booleans.add(true); possibleJumps.add(new Move(start,end));
				}}
			}
			if(start.getCol() - 2 >= 0){
				Position end = new Position(start.getRow()+2,start.getCol()-2);
				Space endLeftRow = board.getRow(start.getRow()+2).getSpace(start.getCol()-2);
				Space midLeftRow = board.getRow(start.getRow()+1).getSpace(start.getCol()-1);
				if(midLeftRow.getPiece()!=null && midLeftRow.getPiece().getColor() != current.getColor() &&
						endLeftRow.isValid()){
					if(current.getColor() == Piece.Color.RED || current.getType()== Piece.Type.KING){
						booleans.add(true); possibleJumps.add(new Move(start,end));
				}}
			}
		}
		//white singles or king pieces
		if(start.getRow() - 2 >= 0){
			if(start.getCol() + 2 <= 7){
				Position end = new Position(start.getRow()-2,start.getCol()+2);
				Space endBRightRow = board.getRow(start.getRow()-2).getSpace(start.getCol()+2);
				Space midBRightRow = board.getRow(start.getRow()-1).getSpace(start.getCol()+1);
				if(midBRightRow.getPiece()!=null && midBRightRow.getPiece().getColor() != current.getColor() &&
						endBRightRow.isValid()){
					if(current.getColor() == Piece.Color.WHITE || current.getType()== Piece.Type.KING){
						booleans.add(true); possibleJumps.add(new Move(start,end));
				}}
			}
			if(start.getCol() - 2 >= 0){
				Position end = new Position(start.getRow()-2,start.getCol()-2);
				Space endBLeftRow = board.getRow(start.getRow()-2).getSpace(start.getCol()-2);
				Space midBLeftRow = board.getRow(start.getRow()-1).getSpace(start.getCol()-1);
				if(midBLeftRow.getPiece()!=null && midBLeftRow.getPiece().getColor() != current.getColor() &&
						endBLeftRow.isValid()){
					if(current.getColor() == Piece.Color.WHITE || current.getType()== Piece.Type.KING){
						booleans.add(true); possibleJumps.add(new Move(start,end));
				}}
			}
		}
		for (Boolean temp : booleans) {return temp;}
		return false;
	}

	/**
	 * gets a list of moves from one position
	 * @param start starting position
	 * @return list of moves from that position
	 */
	public ArrayList<Move> canMove(Position start){
		ArrayList<Move> moves = new ArrayList<>();
		Piece piece = board.getRow(start.getRow()).getSpace(start.getCol()).getPiece();
		if(piece != null){
			//right move
			if(start.getRow() + 1 <= 7){
				if(start.getCol() + 1 <= 7){
					Position end = new Position(start.getRow()+1, start.getCol()+1);
					Space last = board.getRow(start.getRow()+1).getSpace(start.getCol()+1);
					Move move = new Move(start,end);
					if(last.isValid() && piece.isValidMove(move)){moves.add(move);}
				}
				if(start.getCol() - 1 >= 0){
					Position end = new Position(start.getRow()+1, start.getCol()-1);
					Space last = board.getRow(start.getRow()+1).getSpace(start.getCol()-1);
					Move move = new Move(start,end);
					if(last.isValid() && piece.isValidMove(move)){moves.add(move);}
				}
			}
			if(start.getRow() - 1 >= 0){
				if(start.getCol() + 1 <= 7){
					Position end = new Position(start.getRow()-1, start.getCol()+1);
					Space last = board.getRow(start.getRow()-1).getSpace(start.getCol()+1);
					Move move = new Move(start,end);
					if(last.isValid() && piece.isValidMove(move)){moves.add(move);}
				}
				if(start.getCol() - 1 >= 0){
					Position end = new Position(start.getRow()-1, start.getCol()-1);
					Space last = board.getRow(start.getRow()-1).getSpace(start.getCol()-1);
					Move move = new Move(start,end);
					if(last.isValid() && piece.isValidMove(move)){moves.add(move);}
				}
			}
		}
		return moves;
	}

	/**
	 * checks if a piece has made it to the end row and if so
	 * if becomes a king
	 * @param move where the piece is moving too
	 */
	public boolean setKing(Move move){
		Piece piece = board.getRow(move.getEnd().getRow()).getSpace(move.getEnd().getCol()).getPiece();
		if(activeColor == Piece.Color.RED && move.getEnd().getRow() == 7){
			piece.setKing();
			promotion = true;
			return promotion;
		}
		if(activeColor == Piece.Color.WHITE && move.getEnd().getRow() == 0){
			piece.setKing();
			promotion = true;
			return promotion;
		}
		return false;
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
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * checks if there is a stalemate
	 */
	public boolean checkStalemate(){
		ArrayList<Move> moves = new ArrayList<>();
		for (int x = 0; x < 8; x++) {for (int y = 0; y < 8; y++) {
			Piece target = board.getRow(x).getSpace(y).getPiece();
			if(target != null && target.getColor() == activeColor){moves.addAll(canMove(new Position(x,y)));}
		}}
		return moves.isEmpty() && possibleJumps.isEmpty();
	}

	/**
	 * @return single player boolean
	 */
	public Boolean getSinglePlayer() {
		return SinglePlayer;
	}

	/**
	 * @return the color of the winner
	 */
	public Piece.Color getWinner() {
		return board.getWinner();
	}

	/**
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
	 * @return AI difficulty
	 */
	public String getAIOpponentDifficulty(){
		return String.valueOf(difficulty);
	}

	/**
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
	 * @return which color is currently allowed to move
	 */
	public Piece.Color getActiveColor() {
		return activeColor;
	}

	/**
	 * @return the red player
	 */
	public Player getRedPlayer() {
		return redPlayer;
	}

	/**
	 * @return the white player
	 */
	public Player getWhitePlayer() {
		return whitePlayer;
	}

	/**
	 * @return the current most valid state of the board
	 */
	public Board getBoardView() {
		return board;
	}

	/**
	 * @return the string message about the validity of the last submitted move
	 */
	public String getValidity(){
		return validity;
	}

	/**
	 * @return size of validated moves
	 */
	public int getMoveSize(){
		return validatedMoves.size();
	}

	/**
	 * @return arraylist of past moves
	 */
	public ArrayList<Move> getPastMoves() {
		return pastMoves;
	}

	/**
	 * @return arraylist of possible jump moves
	 * resets the possible jumps list to make sure
	 * duplicate jumps aren't recorded
	 */
	public ArrayList<Move> getPossibleJumps() {
		ArrayList<Move> temp = possibleJumps;
		possibleJumps = new ArrayList<>();
		return temp;
	}

	/**
	 * Set the active color to the passed value.
	 * @param color what the active color should be
	 */
	public void setActiveColor(Piece.Color color) {
		activeColor = color;
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

	public boolean getPromotion(){
		return promotion;
	}

	/**
	 * this is use for junit tests to create custom states.
	 * @param board board to be imported
	 */
	public void setBoard(Board board){
		this.board = board;
	}




}
