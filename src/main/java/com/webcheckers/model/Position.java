package com.webcheckers.model;

/**
 * @author Spencer Creveling
 */

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

    /**
     *
     * @param object the position to be compared too
     * @return if the positions are equal or not
     */
    @Override
    public boolean equals(Object object){
        if(object instanceof Position){
            Position other = (Position) object;
            return row == other.row && cell == other.cell;
        }
        return false;
    }

    /**
     * for debugging only
     * @return the state of the position
     */
    @Override
    public String toString(){
        return "row " + row + "col " + cell;
    }
}
