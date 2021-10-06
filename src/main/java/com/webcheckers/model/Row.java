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
    private final ArrayList<Space> spaces;

    //
    // Constructor
    //

    public Row(int index, ArrayList<Space> spaces){
        this.index = index;
        this.spaces = spaces;
    }

    //
    // Public Methods
    //

    public int getIndex() {
        return index;
    }

    @Override
    public Iterator<Space> iterator() {
        return spaces.iterator();
    }
   
    public Iterator<Space> iterator(boolean flip) {
    	if (flip) {
    		ArrayList<Space> flipped = new ArrayList<>();
    		for (int i = 0; i < 8; i++)
    			flipped.add(spaces.get(7 - i));
    		return flipped.iterator();
    	} else
    		return iterator();
    }
}
