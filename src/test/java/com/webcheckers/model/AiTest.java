package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class AiTest {

    @Test
    public void simpleMoveStupid() {
        Game CuT = new Game(mock(Player.class), AI.difficulty.Easy);
        CuT.makeMove(new Move(new Position(2, 1), new Position(3, 2)));
        CuT.submitMove();
        assertEquals(CuT.getActiveColor(), Piece.Color.RED);
    }

    @Test
    public void simpleMoveDefensive() {
        //runs this multiple times do the ai random decision making
        int x = 100;
        while (x != 0) {
            Game CuT = new Game(mock(Player.class), AI.difficulty.Defensive);
            CuT.makeMove(new Move(new Position(2, 1), new Position(3, 2)));
            CuT.submitMove();
            assertFalse(CuT.forceJump());
            x -= 1;
        }
    }

    @Test
    public void simpleMoveAggressive() {
        //runs this multiple times do the ai random decision making
        int x = 100;
        while (x != 0) {
            Game CuT = new Game(mock(Player.class), AI.difficulty.Aggressive);
            CuT.makeMove(new Move(new Position(2, 1), new Position(3, 2)));
            CuT.submitMove();
            assertTrue(CuT.forceJump());
            x -= 1;
        }
    }

    @Test
    public void forceJump() {
        //runs this multiple times do the ai random decision making
        int x = 5;
        while (x != 0) {
            Game CuT = new Game(mock(Player.class), AI.difficulty.Easy);

            Board custom = new Board("");

            custom.getRow(2).getSpace(1).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.RED));
            custom.getRow(4).getSpace(3).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));
            custom.getRow(7).getSpace(0).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));

            CuT.setBoard(custom);
            CuT.makeMove(new Move(new Position(2, 1), new Position(3, 2)));
            CuT.submitMove();


            custom.getRow(2).getSpace(1).setPiece(null);
            custom.getRow(4).getSpace(3).setPiece(null);
            custom.getRow(2).getSpace(1).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));

            assertEquals(custom.toString(), CuT.getBoardView().toString());
            x -= 1;
        }
    }

    @Test
    public void testdoublejump() {

        for(AI.difficulty dif : AI.difficulty.values()){
            Game CuT = new Game(mock(Player.class), dif);
            Board custom = new Board("");
            custom.getRow(3).getSpace(4).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.RED));
            custom.getRow(2).getSpace(3).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.RED));
            custom.getRow(5).getSpace(6).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));
            CuT.setBoard(custom);
            CuT.makeMove(new Move(new Position(3,4),new Position(4,5)));
            CuT.submitMove();
        }
    }

    @Test
    public void testdoublejumpIntoKing() {
        for (AI.difficulty dif : AI.difficulty.values()) {
            Game CuT = new Game(mock(Player.class), dif);
            Board custom = new Board("");
            custom.getRow(2).getSpace(4).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.RED));
            custom.getRow(1).getSpace(3).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.RED));
            custom.getRow(4).getSpace(6).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));
            CuT.setBoard(custom);
            CuT.makeMove(new Move(new Position(3, 4), new Position(4, 5)));
            CuT.submitMove();
        }
    }

    @Test
    public void testjumpIntoKing() {
        for (AI.difficulty dif : AI.difficulty.values()) {
            Game CuT = new Game(mock(Player.class), dif);
            Board custom = new Board("");
            custom.getRow(0).getSpace(1).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.RED));
            custom.getRow(2).getSpace(3).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));
            CuT.setBoard(custom);
            CuT.makeMove(new Move(new Position(0, 1), new Position(1, 2)));
            CuT.submitMove();
        }
    }
}
