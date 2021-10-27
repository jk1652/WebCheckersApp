package com.webcheckers.model;

/**
 * @author Spencer Creveling
 */

public class Space {
    private final int cellIdx;
    private final Boolean isValid;
    private Piece piece;

    /**
     * @param cellIdx the index of the space
     * @param isValid if it is a white of black square
     * @param piece the piece that is placed on the space
     */
    public Space(int cellIdx, boolean isValid, Piece piece){
        this.cellIdx = cellIdx;
        this.isValid = isValid;
        this.piece = piece;
    }

    /**
     * @param space the space object to be deep copied
     */
    public Space(Space space){
        this.cellIdx = space.cellIdx;
        this.isValid = space.naturalValid(); // h
    	if (space.piece != null)
            this.piece = new Piece(space.piece);
        else
            this.piece = null;
    }

    /**
     * @return the index of the space
     */
    public int getCellIdx(){
        return cellIdx;
    }

    /**
     * @return if this space is valid for a piece to be placed
     */
    public boolean isValid(){
        return isValid && piece == null;
    }

    /**
     * this is valid is used for deep copies because the isValid
     * boolean should be set regardless of piece status
     * @return if the space is valid regardless of contends
     */
    private boolean naturalValid(){
        return isValid;
    }

    /**
     * @return the piece on the space
     */
    public Piece getPiece(){
        return piece;
    }

    /**
     * @param piece sets the current piece on the space
     */
    public void setPiece(Piece piece){
        this.piece = piece;
    }
}
