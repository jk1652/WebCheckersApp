package com.webcheckers.model;

/**
 * @author: Quentin Ramos II
 */

public class Piece {

    //
    // Attributes
    //

    public enum Type {SINGLE, KING}
    private Type type;

    public enum Color {RED, WHITE}
    private final Color color;

    //
    // Constructor
    //

    public Piece(Type type, Color color){
        this.type = type;
        this.color = color;
    }

    //
    // Public Methods
    //

    public Type getType(){
        return type;
    }

    public void setType(Type type){
        this.type = type;
    }

    public void setKing(){
        setType(Type.KING);
    }

    public Color getColor(){
        return color;
    }

    public boolean isValidMove(Move move){
        if(type==Type.SINGLE){
            int initRow = move.getStart().getRow();
            int finalRow = move.getEnd().getRow();

            //check if move is only forward
            return move.isMove() && (finalRow - initRow == 1);
        }
        else{return move.isMove();}
    }

    public boolean isValidJump(Move move){
        if(type==Type.SINGLE){
            int initRow = move.getStart().getRow();
            int finalRow = move.getEnd().getRow();

            //check if jump is only forward
            return move.isJump() && (finalRow - initRow == 2);
        }
        else{return move.isJump();}
    }
}
