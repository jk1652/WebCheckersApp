package com.webcheckers.model;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * gets the row value at a position
     * @return row
     */
    public int getRow(){
        return row;
    }

    /**
     * gets the column value at a position
     * @return column
     */
    public int getCol(){
        return col;
    }
}
