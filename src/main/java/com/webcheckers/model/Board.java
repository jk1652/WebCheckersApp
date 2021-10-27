package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Spencer Creveling
 * @author Quentin Ramos II
 * @author David Pritchard
 */

public class Board implements Iterable<Row> {
    private ArrayList<Row> rows = new ArrayList<>();

    private Piece.Color winner = null;
    private boolean resign = Boolean.FALSE;
    private boolean exit = Boolean.FALSE;

    /**
     * Board constructor when called creates a new array of
     * rows to be used, calls create even and create odd to make code
     * more legible
     */
    public Board(){
        Piece.Color color = Piece.Color.RED;
        for(int row = 0; row < 8; row++) {
            boolean place = row != 3 && row != 4;
            if(row > 4){ color = Piece.Color.WHITE;}
            if(row % 2 == 0){
                rows.add(new Row(row,CreateEven(color,place)));
            } else {
                rows.add(new Row(row,CreateOdd(color,place)));
            }
        }
    }

    /**
     * this constructer is used to create deep copys of past
     * board states so when the move stack is cleard the board
     * is not renderd null
     * @param board board object to be copied
     */
    public Board (Board board){
     	for (int row = 0; row < 8; row++) {
		ArrayList<Space> spaces = new ArrayList<>();
		for (Space space : board.getRow(row))
			spaces.add(new Space(space));
		rows.add(new Row(row, spaces));
	}
    }

    /**
     * this function is used to set the winner of the game
     * @param color the color of the the winning player
     */
    public void setWinner(Piece.Color color) {
        winner = color;
        resign = Boolean.TRUE;
    }

    /**
     * this function is used to check if a player has resigned.
     * @return if a player has resigned
     */
    public boolean getResign() {
        return  resign;
    }

    /**
     * this function is used to check if a player has won
     *though capturing all pieces
     * @return the color of the winner if their is one
     */
    public Piece.Color getWinner() {
        if (winner == null) {
            boolean[] won = new boolean[]{false, false};
            for (Row row : this) {
                for (Space space : row) {
                    Piece piece = space.getPiece();
                    if (piece != null)
                        won[piece.getColor().ordinal()] = true;
                    if (won[0] && won[1])
                        return null;
                }
            }
            exit = Boolean.TRUE;
            return Piece.Color.values()[won[0] ? 0 : 1]; // return the value that is true
        }
        exit = Boolean.TRUE;
        return winner;
    }

    /**
     * this function is used to check how the game was exited
     * ie via win or forfit
     * @return the exit status
     */
    public boolean getExitState() {
        return exit;
    }

    /**
     *returns an even row on the checkers board
     * @param color color of pieces to be placed
     * @param starter should there be pieces in this row
     * @return a array list of squares that can be immediately put into a row instance
     */
    private ArrayList<Space> CreateEven(Piece.Color color, Boolean starter){
        ArrayList<Space> tempSpaces = new ArrayList<>();
        Piece piece = null;
        if (starter){
            piece = new Piece(Piece.Type.SINGLE, color);
        }


        for(int space = 0; space < 8; space++) {
            if(space % 2 == 0){
                tempSpaces.add(new Space(space,false,null));
            } else {
                tempSpaces.add(new Space(space,true, piece));
            }
        }
        return tempSpaces;
    }
    /**
     *created an odd row on the checker board
     * @param color color of pieces to be placed
     * @param starter should there be pieces in this row
     * @return a array list of squares that can be immediately put into a row instance
     */
    private ArrayList<Space> CreateOdd(Piece.Color color, Boolean starter){
        Piece piece = null;
        if (starter){
            piece = new Piece(Piece.Type.SINGLE, color);
        }
        ArrayList<Space> tempSpaces = new ArrayList<>();
        for(int space = 0; space < 8; space++) {
            if(space % 2 != 0){
                tempSpaces.add(new Space(space,false,null));
            } else {
                tempSpaces.add(new Space(space,true, piece));
            }
        }
        return tempSpaces;
    }
    /**
     * Returns the iterator ordered to display correctly for the player's viewpoints.
     */
    public Iterator<Row> iterator(boolean flip) {
    	if (flip) {
    		ArrayList<Row> flipped = new ArrayList<>();
    		for (int i = 0; i < 8; i++)
    			flipped.add(rows.get(7 - i));
    		return flipped.iterator();
    	} else
    		return iterator();
    }

    /**
     * returns a specific row
     * @param row_num the row number
     * @return specific row
     */
    public Row getRow(int row_num){
        return rows.get(row_num);
    }

    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }

    @Override
    public String toString() {
	StringBuilder s = new StringBuilder();
	for (Row row : this)
		s.append(row.toString()).append("\n");
	return s.toString();
    }
}
