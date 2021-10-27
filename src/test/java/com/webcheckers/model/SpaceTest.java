package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("Model-tier")
public class SpaceTest {
    private Space CuT;

    @Test
    public void InvalidSquare(){

        CuT = new Space(0,false,null);

        //test to see if space is invalid
        assertFalse(CuT.isValid());

        //test cell index
        assertEquals(0,CuT.getCellIdx());

        //test piece
        assertNull(CuT.getPiece());
    }

    @Test
    public void OccupiedSquare(){
        Piece piece = mock(Piece.class);
        CuT = new Space(1,true,piece);

        //test to see if space is invalid
        assertFalse(CuT.isValid());

        //test cell index
        assertEquals(1,CuT.getCellIdx());

        //test piece
        assertEquals(piece,CuT.getPiece());
    }
    @Test
    public void UnoccupiedSquare(){
        CuT = new Space(3,true,null);

        //test to see if space is invalid
        assertTrue(CuT.isValid());

        //test cell index
        assertEquals(3,CuT.getCellIdx());

        //test piece
        assertNull(CuT.getPiece());
    }

    @Test
    public void test_SetPiece(){
        final Space empty = new Space(3,true,null);
        Piece piece = mock(Piece.class);
        empty.setPiece(piece);
        assertFalse(empty.isValid());
    }

    @Test
    public void test_copy(){
        Space primary = new Space(3,true,null);
        Space copy = new Space(primary);

        assertEquals(copy.getCellIdx(),primary.getCellIdx());
        assertEquals(copy.isValid(),primary.isValid());
        assertEquals(copy.getPiece(),primary.getPiece());

        Piece piece = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
        primary.setPiece(piece);
        Space copy2 = new Space(primary);

        assertEquals(copy2.getCellIdx(),primary.getCellIdx());
        assertEquals(copy2.isValid(),primary.isValid());
        assertNotNull(copy2.getPiece());
    }
}
