package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class PieceTest {

    @Test
    public void test_Piece() {
        final Piece CuT = new Piece(Piece.Type.SINGLE, Piece.Color.RED);

        // check if color is red
        assertEquals(Piece.Color.RED, CuT.getColor());

        // check if piece is single
        assertEquals(Piece.Type.SINGLE,CuT.getType());

        //Promote to King and check
        CuT.setKing(); assertEquals(Piece.Type.KING,CuT.getType());

        //Test Copy Constructor
        final Piece copy = new Piece(CuT);
        assertEquals(copy.getType(),CuT.getType());
        assertEquals(copy.getColor(),CuT.getColor());
    }

    @Test
    public void test_ValidMove(){
        final Piece red = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
        final Position redStart = new Position(2,2);
        final Position redValidEnd = new Position(3,1);
        final Position redInvalidEnd = new Position(1,1);
        final Position redInvalidEnd2 = new Position(4,0);

        final Move redValidMove = new Move(redStart,redValidEnd);
        final Move redInvalidMove = new Move(redStart,redInvalidEnd);
        final Move redInvalidMove2 = new Move(redStart,redInvalidEnd2);


        //Test Single Red movement
        assertTrue(red.isValidMove(redValidMove));
        assertFalse(red.isValidMove(redInvalidMove));
        assertFalse(red.isValidMove(redInvalidMove2));


        final Piece white = new Piece(Piece.Type.SINGLE, Piece.Color.WHITE);
        final Position whiteStart = new Position(5,1);
        final Position whiteValidEnd = new Position(4,2);

        final Position whiteInvalidEnd = new Position(6,2);
        final Position whiteInvalidEnd2 = new Position(7,2);

        final Move whiteInvalidMove = new Move(whiteStart,whiteInvalidEnd);
        final Move whiteInvalidMove2 = new Move(whiteStart,whiteInvalidEnd2);
        final Move whiteValidMove = new Move(whiteStart,whiteValidEnd);

        //Test Single White movement
        assertTrue(white.isValidMove(whiteValidMove));
        assertFalse(white.isValidMove(whiteInvalidMove));
        assertFalse(white.isValidMove(whiteInvalidMove2));

        //Test King movement
        white.setKing();
        assertTrue(white.isValidMove(whiteInvalidMove));
        assertTrue(white.isValidMove(whiteValidMove));
        assertFalse(white.isValidMove(whiteInvalidMove2));
    }

    @Test
    public void test_ValidJump(){
        final Piece red = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
        final Position redStart = new Position(2,2);
        final Position redValidEnd = new Position(4,4);
        final Position redInvalidEnd = new Position(0,4);
        final Position redInvalidEnd2 = new Position(5,5);

        final Move redValidMove = new Move(redStart,redValidEnd);
        final Move redInvalidMove = new Move(redStart,redInvalidEnd);
        final Move redInvalidMove2 = new Move(redStart,redInvalidEnd2);


        //Test Single Red Jump
        assertTrue(red.isValidJump(redValidMove));
        assertFalse(red.isValidJump(redInvalidMove));
        assertFalse(red.isValidJump(redInvalidMove2));


        final Piece white = new Piece(Piece.Type.SINGLE, Piece.Color.WHITE);
        final Position whiteStart = new Position(5,3);
        final Position whiteValidEnd = new Position(3,5);

        final Position whiteInvalidEnd = new Position(7,1);
        final Position whiteInvalidEnd2 = new Position(2,6);

        final Move whiteInvalidMove = new Move(whiteStart,whiteInvalidEnd);
        final Move whiteInvalidMove2 = new Move(whiteStart,whiteInvalidEnd2);
        final Move whiteValidMove = new Move(whiteStart,whiteValidEnd);

        //Test Single White Jump
        assertTrue(white.isValidJump(whiteValidMove));
        assertFalse(white.isValidJump(whiteInvalidMove));
        assertFalse(white.isValidJump(whiteInvalidMove2));

        //Test King Jump
        white.setKing();
        assertTrue(white.isValidJump(whiteInvalidMove));
        assertTrue(white.isValidJump(whiteValidMove));
        assertFalse(white.isValidJump(whiteInvalidMove2));
    }
}