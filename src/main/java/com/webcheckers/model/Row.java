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
    private final ArrayList<Space> Spaces;

    //
    // Constructor
    //

    public Row(int index, ArrayList<Space> Spaces){
        this.index = index;
        this.Spaces= Spaces;
    }

    //
    // Public Methods
    //

    public int getIndex() {
        return index;
    }

    @Override
    public Iterator<Space> iterator() {
        return Spaces.iterator();
    }
}
