package com.webcheckers.model;

public class Move {
    private final Position start;
    private final Position end;

    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position getStart(){
        return start;
    }

    public Position getEnd(){
        return end;
    }

    /**
     * Is the performed action a simple move?
     * @return true if the action is moved diagonally one space
     */
    public boolean isMove(){
        //Start Coordinates
        int initRow = start.getRow();
        int initCol = start.getCol();
        //End Coordinates
        int finalRow = end.getRow();
        int finalCol = end.getCol();

        //Track movement:
        int row = Math.abs(initRow-finalRow);
        int col = Math.abs(initCol-finalCol);
        return row==1 && col==1;
    }

    /**
     * Is the performed action a jump move?
     * @return true if the action is moved
     * diagonally two spaces.
     */

    public boolean isJump(){
        //Start Coordinates
        int initRow = start.getRow();
        int initCol = start.getCol();
        //End Coordinates
        int finalRow = end.getRow();
        int finalCol = end.getCol();

        //Track movement:
        int row = Math.abs(initRow-finalRow);
        int col = Math.abs(initCol-finalCol);
        return row==2 && col==2;
    }
}
