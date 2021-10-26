package com.webcheckers.model;

/*
  @author Spencer Creveling
 * @author Quentin Ramos II
 */

import java.util.ArrayList;
import java.util.Iterator;

public class Board implements Iterable<Row> {
    private ArrayList<Row> rows = new ArrayList<>();

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
}
