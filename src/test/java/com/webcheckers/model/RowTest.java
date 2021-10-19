package com.webcheckers.model;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("Model-tier")
public class RowTest {
    private final ArrayList<Space> spaces = new ArrayList<>();
    private final ArrayList<Space> spacesreversed = new ArrayList<>(8);
    @Test
    public void Test_Row(){
        for(int x = 0; x < 8; x++){
            spaces.add(mock(Space.class));
        }
        Row CuT = new Row(0, spaces);

        assertEquals(0,CuT.getIndex());

        for(int x = 0; x < 8; x++) {
            assertEquals(spaces.iterator().next(),CuT.iterator(false).next());
            spacesreversed.add(spaces.get(7-x));
        }

        for(int x = 0; x < 8; x++) {
            assertEquals(spacesreversed.iterator().next(),CuT.iterator(true).next());
        }


    }
}