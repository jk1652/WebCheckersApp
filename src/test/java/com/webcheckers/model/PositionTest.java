package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
class PositionTest {

    @Test
    public void test_equals(){
        //Equal case
        final Position pos1 = new Position(2,2);
        final Position pos2 = new Position(2,2);

        //Not Equal cases
        final Position pos3 = new Position(3,2);
        final Position pos4 = new Position(2,3);
        final Object notPos = new Object();

        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
        assertNotEquals(pos1, pos4);
        assertNotEquals(pos1, notPos);
    }
}