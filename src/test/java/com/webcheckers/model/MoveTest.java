package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
class MoveTest {

    @Test
    public void test_toString(){
        final Position Start = new Position(2,2);
        final Position End = new Position(3,1);
        final Move move = new Move(Start,End);

        assertEquals("Start " + Start + "\nEnd " + End, move.toString());
    }

    @Test
    public void test_isMove(){
        //Tests invalid movement that piece doesn't test
        final Position Start = new Position(2,2);
        final Position invalidEnd = new Position(4,0);
        final Position invalidEnd2 = new Position(3,2);
        final Position invalidEnd3 = new Position(2,0);

        final Move invalidMove = new Move(Start,invalidEnd);
        final Move invalidMove2 = new Move(Start,invalidEnd2);
        final Move invalidMove3 = new Move(Start,invalidEnd3);

        assertFalse(invalidMove.isMove());
        assertFalse(invalidMove2.isMove());
        assertFalse(invalidMove3.isMove());
    }

    @Test
    public void test_isJump(){
        //Tests invalid Jumps that piece doesn't test
        final Position Start = new Position(2,2);
        final Position invalidEnd = new Position(3,3);
        final Position invalidEnd2 = new Position(4,2);
        final Position invalidEnd3 = new Position(4,2);

        final Move invalidMove = new Move(Start,invalidEnd);
        final Move invalidMove2 = new Move(Start,invalidEnd2);
        final Move invalidMove3 = new Move(Start,invalidEnd3);

        assertFalse(invalidMove.isJump());
        assertFalse(invalidMove2.isJump());
        assertFalse(invalidMove3.isJump());
    }
}