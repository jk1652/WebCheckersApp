package com.webcheckers.model;

public class Piece {

    //
    // Attributes
    //

    private enum Type {SINGLE, KING}
    private final Type type;

    private enum Color {RED, WHITE}
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

    public Color getColor(){
        return color;
    }

}
