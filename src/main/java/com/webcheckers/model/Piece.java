package com.webcheckers.model;

/**
 * @author: Quentin Ramos II
 */

public class Piece {

    //
    // Attributes
    //

    public enum Type {SINGLE, KING}
    public enum Color {RED, WHITE}

    private Type type;
    private final Color color;

    //
    // Constructor
    //

    public Piece(Type type, Color color){
        this.type = type;
        this.color = color;
    }

    //
    // Copy Constructor
    //

    public Piece(Piece piece){	
        this.type = piece.getType();
        this.color = piece.getColor();
    }


    //
    // Public Methods
    //

    /**
     * gets the type of a piece
     * @return type
     */
    public Type getType(){
        return type;
    }

    /**
     * sets the type of the piece to a king piece
     */
    public void setKing(){
        this.type = Type.KING;
    }

    /**
     * gets the color of a piece
     * @return color
     */
    public Color getColor(){
        return color;
    }

    /**
     * Checks the validity of a simple move according to the piece type
     * @param move the move a user implements
     * @return whether or not the move is valid
     */
    public boolean isValidMove(Move move){
        if(type==Type.SINGLE){
            int initRow = move.getStart().getRow();
            int finalRow = move.getEnd().getRow();

            //check if move is only forward for red
            if(color == Color.RED){return move.isMove() && (finalRow - initRow == 1);}

            //check if move is only forward for white
            else{return move.isMove() && (initRow - finalRow == 1);}
        }
        else{return move.isMove();}
    }

    /**
     * Checks the validity of a jump according to the piece type
     * @param move the move a user implements
     * @return whether or not the move is valid
     */
    public boolean isValidJump(Move move){
        if(type==Type.SINGLE){
            int initRow = move.getStart().getRow();
            int finalRow = move.getEnd().getRow();

            //check if jump is only forward for red
            if(color == Color.RED){return move.isJump() && (finalRow - initRow == 2);}

            //check if jump is only forward for white
            else{return move.isJump() && (initRow - finalRow == 2);}
        }
        else{return move.isJump();}
    }
}
