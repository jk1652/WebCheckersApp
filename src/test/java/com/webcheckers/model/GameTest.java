package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class GameTest {

    @Test
    public void test_viewMode(){
        Game CuT = new Game(mock(Player.class),mock(Player.class));
        assertTrue(CuT.isPermittedViewMode(Game.View.PLAY,CuT.getRedPlayer()));

        assertTrue(CuT.isPermittedViewMode(Game.View.PLAY,CuT.getWhitePlayer()));

        assertFalse(CuT.isPermittedViewMode(Game.View.PLAY,mock(Player.class)));
    }


    @Test
    public void test_isParticipant() {
        Game CuT = new Game(mock(Player.class),mock(Player.class));

        assertTrue(CuT.isParticipant(CuT.getWhitePlayer()));

        assertTrue(CuT.isParticipant(CuT.getRedPlayer()));

        assertFalse(CuT.isParticipant(mock(Player.class)));
    }

    @Test
    public void testGetters(){
        Player player1 = new Player("a");
        Player player2 = new Player("b");

        Game CuT = new Game(player1,player2);

        assertEquals(CuT.getUserColor("a"), Piece.Color.RED);
        assertEquals(CuT.getUserColor("b"), Piece.Color.WHITE);

        assertEquals(CuT.getOpponentName("a"),"b");
        assertEquals(CuT.getOpponentName("b"),"a");

        assertNull(CuT.getWinner());


    }


    @Test
    public void test_PieceMovement(){
        Game CuT = new Game(mock(Player.class),mock(Player.class));

        //moves a peice
        assertTrue(CuT.validateMove(new Move(new Position(2,1), new Position(3,2))));
        CuT.makeMove(new Move(new Position(2,1), new Position(3,2)));
        assertEquals("Valid simple move performed.",CuT.getValidity());

        assertEquals(1,CuT.getMoveSize());
        //tries to move again
        assertFalse(CuT.validateMove(new Move(new Position(2,3), new Position(3,4))));
        assertEquals("you can not move again",CuT.getValidity());

        //tries to move the wrong peice
        assertFalse(CuT.validateMove(new Move(new Position(0,7), new Position(1,6))));

        //checks avtive player color
        assertEquals(Piece.Color.RED,CuT.getActiveColor());
        CuT.submitMove();

        assertEquals(Piece.Color.WHITE,CuT.getActiveColor());

        //moves a piece simply to force a jump
        assertTrue(CuT.validateMove(new Move(new Position(5,4), new Position(4,3))));
        assertEquals("Valid simple move performed.",CuT.getValidity());
        CuT.makeMove(new Move(new Position(5,4), new Position(4,3)));

        CuT.submitMove();

        assertTrue(CuT.forceJump());
        assertFalse(CuT.validateMove(new Move(new Position(2,3), new Position(3,4))));
        assertEquals("There is a jump available",CuT.getValidity());

        assertTrue(CuT.validateMove(new Move(new Position(3,2), new Position(5,4))));
        assertEquals("Valid jump move performed.",CuT.getValidity());

        CuT.makeMove(new Move(new Position(3,2), new Position(5,4)));

        //test undo
        assertTrue(CuT.undoMove());
        assertFalse(CuT.undoMove());

        assertEquals(0,CuT.getMoveSize());

        assertFalse(CuT.validateMove(new Move(new Position(2,5), new Position(4,7))));
        assertEquals("You can't jump over nothing.",CuT.getValidity());

        CuT.makeMove(new Move(new Position(3,2), new Position(5,4)));
        assertFalse(CuT.validateMove(new Move(new Position(2,5), new Position(4,7))));
        assertEquals("you can not move two pieces",CuT.getValidity());

        CuT.submitMove();

        assertTrue(CuT.validateMove(new Move(new Position(6,5), new Position(4,3))));
        CuT.makeMove(new Move(new Position(6,5), new Position(4,3)));

        assertFalse(CuT.validateMove(new Move(new Position(4,3), new Position(3,2))));
        assertEquals("you can not simple move after a jump",CuT.getValidity());

        CuT.submitMove();

        assertTrue(CuT.validateMove(new Move(new Position(1,0), new Position(2,1))));
        CuT.makeMove(new Move(new Position(1,0), new Position(2,1)));

        CuT.submitMove();

        assertFalse(CuT.validateMove(new Move(new Position(4,3), new Position(5,4))));
        assertEquals("Your piece can't move backwards.",CuT.getValidity());

        assertFalse(CuT.validateMove(new Move(new Position(4,3), new Position(6,5))));


        System.out.println(CuT.getBoardView().toString());
    }

    @Test
    public void testSetKing(){

    }
}
