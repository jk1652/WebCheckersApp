package com.webcheckers.model;

/**
 * @author Spencer Creveling
 * @author Quentin Ramos II
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board implements Iterable<Row> {
    private ArrayList<Row> rows = new ArrayList<>();

    /**
     * Board constructer when called creates a new array of
     * rows to be used, calles create even and create odd to make code
     * more ledgable
     */
    public Board(){
        Piece.Color color = Piece.Color.RED;
        for(int row = 0; row < 8; row++) {
            boolean place = true;
            if( row == 3 || row == 4){place = false;}
            if(row > 4){ color = Piece.Color.WHITE;}
            if(row % 2 == 0){
                rows.add(new Row(row,CreateEven(color,place)));
            } else {
                rows.add(new Row(row,CreateOdd(color,place)));
            }
        }
    }

    public Piece.Color getWinner() {
        boolean[] won = new boolean[] {false, false};
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
    /**
     *
     * @param color color of peices to be placed
     * @param starter should there be peices in this row
     * @return a array list of squares that can be imidetly put into a row instance
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
     * @param color color of peices to be placed
     * @param starter should there be peices in this row
     * @return a array list of squares that can be imidetly put into a row instance
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
     * @param color should the board be flipped.
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
    
    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }
}
