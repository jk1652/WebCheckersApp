package com.webcheckers.model;

public class Single extends Piece{

    //
    // Constructor
    //

    public Single(Type type, Color color) {
        super(type, color);
    }

    //
    // Methods
    //

    public void setKing(){

    }

    @Override
    public boolean isValidMove(Move move) {
        int initRow = move.getStart().getRow();
        int finalRow = move.getEnd().getRow();

        //check if move is only forward
        return move.isMove() && (finalRow - initRow == 1);
    }

    @Override
    public boolean isValidJump(Move move) {
        int initRow = move.getStart().getRow();
        int finalRow = move.getEnd().getRow();

        // check if surrounding piece is opposite color

        //
        return false;
    }
}
