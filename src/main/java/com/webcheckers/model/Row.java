package com.webcheckers.model;

import java.util.Iterator;

/**
 * @author: Quentin Ramos II
 */

public class Row implements Iterable<Space>{

    //
    // Attributes
    //

    private final int index;

    //
    // Constructor
    //

    public Row(int index){
        this.index = index;
    }

    //
    // Public Methods
    //

    public int getIndex() {
        return index;
    }

    @Override
    public Iterator<Space> iterator() {
        return null;
    }
}
