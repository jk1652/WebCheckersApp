package com.webcheckers.model;

/*
  @author Spencer Creveling
 * @author Quentin Ramos II
 */

import java.util.ArrayList;
import java.util.Iterator;

public class Board implements Iterable<Row> {
    private ArrayList<Row> rows = new ArrayList<>();

    private Piece.Color winner = null;
    private boolean resign = Boolean.FALSE;

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

    public Board (Board board){
        for(int x = 0; x < 8; x++){
            rows.add(new Row(board.iterator().next()));
        }
    }

    public void setWinner(Piece.Color color) {
        winner = color;
        resign = Boolean.TRUE;
    }

    public boolean getResign() {
        return  resign;
    }

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
            return Piece.Color.values()[won[0] ? 0 : 1]; // return the value that is true
        }
        return winner;
    }
    /**
     *
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
     *
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
	String s = "";
	for (Row row : this)
		s += row.toString() + "\n";
	return s;
    }
}
