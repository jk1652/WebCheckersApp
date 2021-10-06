package com.webcheckers.model;

/**
 * @author Spencer Creveling
 */

public class Space {
    private final int cellIdx;
    private final Boolean isValid;
    private Piece piece;

    public Space(int cellIdx, boolean isValid, Piece piece){
        this.cellIdx = cellIdx;
        this.isValid = isValid;
        this.piece = piece;
    }

    public int getCellIdx(){
        return cellIdx;
    }

    public boolean isValid(){
        return isValid && piece == null;
    }

    public Piece getPiece(){
        return piece;
    }
}
