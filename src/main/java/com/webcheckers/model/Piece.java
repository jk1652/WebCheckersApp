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

    public Color getColor(){
        return color;
    }

}
