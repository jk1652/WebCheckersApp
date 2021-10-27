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

        //test peice
        assertNull(CuT.getPiece());
    }

    @Test
    public void OcupiedSquare(){
        Piece piece = mock(Piece.class);
        CuT = new Space(1,true,piece);

        //test to see if space is invalid
        assertFalse(CuT.isValid());

        //test cell index
        assertEquals(1,CuT.getCellIdx());

        //test peice
        assertEquals(piece,CuT.getPiece());
    }
    @Test
    public void UnocupiedSquare(){
        CuT = new Space(3,true,null);

        //test to see if space is invalid
        assertTrue(CuT.isValid());

        //test cell index
        assertEquals(3,CuT.getCellIdx());

        //test peice
        assertNull(CuT.getPiece());
    }

}
