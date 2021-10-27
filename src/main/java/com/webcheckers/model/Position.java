package com.webcheckers.model;

public class Position {
    private final int row;
    private final int cell;

    public Position(int row, int cell){
        this.row = row;
        this.cell = cell;
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
        return cell;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Position){
            Position other = (Position) object;
            return row == other.row && cell == other.cell;
        }
        return false;
    }

    @Override
    public String toString(){
        return "row " + row + "col " + cell;
    }
}
