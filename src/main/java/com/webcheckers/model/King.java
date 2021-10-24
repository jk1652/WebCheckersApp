package com.webcheckers.model;

public class King extends Piece{

    //
    // Constructor
    //

    public King(Type type, Color color) {
        super(type, color);
    }

    //
    // Methods
    //

    @Override
    public boolean isValidMove(Move move) {
        return move.isMove();
    }

    @Override
    public boolean isValidJump(Move move) {
        // check if surrounding piece is opposite color

        return move.isJump();
    }
}
