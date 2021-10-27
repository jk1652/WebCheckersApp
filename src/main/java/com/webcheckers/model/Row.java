package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author: Spencer Creveling
 * @author: Quentin Ramos II
 */

public class Row implements Iterable<Space>{

    //
    // Attributes
    //

    private final int index;
    private ArrayList<Space> spaces = new ArrayList<>();

    //
    // Constructor
    //

    /**
     *
     * @param index the index of the row
     * @param spaces an iterable of spaces
     */
    public Row(int index, ArrayList<Space> spaces){
        this.index = index;
        this.spaces = spaces;
    }

    /**
     *
     * @param row the row object to be deep copied
     */
    public Row(Row row){
        this.index = row.index;
        for(int x = 0; x < 8; x++){
            spaces.add(row.iterator().next());
        }	
    }


    //
    // Public Methods
    //

    /**
     * gets the index
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * gets a specific space at a specific index
     * @param col the column number where that space is at
     * @return the actual space itself
     */
    public Space getSpace(int col){
        return spaces.get(col);
    }

    /**
     *
     * @return the proper iterator
     */
    @Override
    public Iterator<Space> iterator() {
        return spaces.iterator();
    }

    /**
     *
     * @param flip if the iterator should be forwards or nackwards
     * @return the appropriate iterator
     */
    public Iterator<Space> iterator(boolean flip) {
    	if (flip) {
    		ArrayList<Space> flipped = new ArrayList<>();
    		for (int i = 0; i < 8; i++)
    			flipped.add(spaces.get(7 - i));
    		return flipped.iterator();
    	} else
    		return iterator();
    }

    /**
     * for debug purposes only
     * @return the stat of the row in string format
     */
    @Override
    public String toString() {
	StringBuilder s = new StringBuilder();
	for (Space space : this) {
		Piece piece = space.getPiece();
		s.append(piece == null ? "_" : piece.getColor().ordinal());
	}
	return s.toString();
    }
}
