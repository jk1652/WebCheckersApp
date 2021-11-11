package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class AiTest {

    @Test
    public void simpleMoveStupid(){
        Game CuT = new Game(mock(Player.class), AI.difficulty.stupid);
        CuT.makeMove(new Move(new Position(2,1),new Position(3,2)));
        CuT.submitMove();
        assertEquals(CuT.getActiveColor(), Piece.Color.RED);
    }

    @Test
    public void forceJump(){
        //runs this multiple times do the ai random decision making
        int x = 5;
        while(x != 0) {
            Game CuT = new Game(mock(Player.class), AI.difficulty.stupid);

            Board custome = new Board("");

            custome.getRow(2).getSpace(1).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.RED));
            custome.getRow(4).getSpace(3).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));
            custome.getRow(7).getSpace(0).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));

            CuT.setBoard(custome);
            CuT.makeMove(new Move(new Position(2, 1), new Position(3, 2)));
            CuT.submitMove();


            custome.getRow(2).getSpace(1).setPiece(null);
            custome.getRow(4).getSpace(3).setPiece(null);
            custome.getRow(2).getSpace(1).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));

            assertEquals(custome.toString(), CuT.getBoardView().toString() );
            x -= 1;
        }

    }

}
