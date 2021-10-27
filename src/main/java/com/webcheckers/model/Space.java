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

    public Space(Space space){
        this.cellIdx = space.cellIdx;
        this.isValid = space.isValid(); // h
    	if (space.piece != null)
            this.piece = new Piece(space.piece);
        else
            this.piece = null;
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

    public void setPiece(Piece piece){this.piece = piece;}
}
