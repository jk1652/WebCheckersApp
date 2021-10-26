package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Model-tier")
class PieceTest {

    @Test
    public void test_redPiece() {
        final Piece CuT = new Piece(Piece.Type.SINGLE, Piece.Color.RED);

        // check if color is red
        assertEquals(Piece.Color.RED, CuT.getColor());

        // check if piece is single
        assertEquals(Piece.Type.SINGLE,CuT.getType());
    }

    @Test
    public void test_whitePiece() {
        final Piece CuT = new Piece(Piece.Type.SINGLE, Piece.Color.WHITE);

        // check if color is red
        assertEquals(Piece.Color.WHITE, CuT.getColor());

        // check if piece is single
        assertEquals(Piece.Type.SINGLE,CuT.getType());
    }

    @Test
    public void test_redKing() {
        final Piece CuT = new Piece(Piece.Type.KING, Piece.Color.RED);

        // check if color is red
        assertEquals(Piece.Color.RED, CuT.getColor());

        // check if piece is king
        assertEquals(Piece.Type.KING,CuT.getType());
    }

    @Test
    public void test_whiteKing() {
        final Piece CuT = new Piece(Piece.Type.KING, Piece.Color.WHITE);

        // check if color is red
        assertEquals(Piece.Color.WHITE, CuT.getColor());

        // check if piece is king
        assertEquals(Piece.Type.KING,CuT.getType());
    }
}