package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board implements Iterable<Row> {
    private ArrayList<Row> rows = new ArrayList<>();

    public Board(){
        Piece.Color color = Piece.Color.RED;
        for(int row = 0; row < 8; row++){

            if(row > 3){color = Piece.Color.WHITE;}

            ArrayList<Space> tempSpaces = new ArrayList<>();
            for(int space = 0; space < 8; space++){
                if(row % 2 == 0 && row != 4){
                    if(space % 2 == 0){
                        tempSpaces.add(new Space(space,false,null));
                    } else {
                        tempSpaces.add(new Space(space,true, new Piece(Piece.Type.SINGLE, color)));
                    }
                } else if(row != 3) {
                    if (space % 2 == 0) {
                        tempSpaces.add(new Space(space, true, new Piece(Piece.Type.SINGLE, color)));
                    } else {
                        tempSpaces.add(new Space(space,false,null));
                    }
                }
            }
            rows.add(new Row(row,tempSpaces));
        }
    }

    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }
}
