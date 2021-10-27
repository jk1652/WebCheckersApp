package com.webcheckers.model;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("Model-tier")
public class RowTest {
    private final ArrayList<Space> spaces = new ArrayList<>();
    private final ArrayList<Space> spacesReversed = new ArrayList<>(8);

    @Test
    public void Test_Row(){
        for(int x = 0; x < 8; x++){
            spaces.add(mock(Space.class));
        }
        Row CuT = new Row(0, spaces);

        assertEquals(0,CuT.getIndex());

        for(int x = 0; x < 8; x++) {
            assertEquals(spaces.iterator().next(),CuT.iterator(false).next());
            spacesReversed.add(spaces.get(7-x));
        }

        for(int x = 0; x < 8; x++) {
            assertEquals(spacesReversed.iterator().next(),CuT.iterator(true).next());
        }

        //Tests copy constructor through toString
        Row copy = new Row(CuT);
        assertEquals(copy.toString(),CuT.toString());
    }

    @Test
    public void test_getSpace(){

        ArrayList<Space> spaces2 = new ArrayList<>();
        Space space1 = new Space(0,true,null);
        Space space2 = new Space(1,false,null);
        Space space3 = new Space(2,true,null);
        spaces2.add(space1); spaces2.add(space2); spaces2.add(space3);
        Row testRow = new Row(1,spaces2);

        assertEquals(testRow.getSpace(1),space2);
    }

    @Test
    public void test_toString(){
        ArrayList<Space> spaces2 = new ArrayList<>();
        Piece piece = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
        Space space1 = new Space(0,true,null);
        Space space2 = new Space(1,false,piece);
        spaces2.add(space1); spaces2.add(space2);
        Row testRow = new Row(0,spaces2);
        assertEquals("_0",testRow.toString());
    }
}