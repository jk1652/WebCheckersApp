package com.webcheckers.model;

import java.util.ArrayList;

public class AI extends Player{

    public AI(){
        super("CPU");
    }

    /**
     * returns a specific arraylist of moves
     * @param game the game it's in
     * @return if there's a jump, returns list of jumps
     * else it returns a list of simple moves
     */
    public ArrayList<Move> AIMoves(Game game){
        Board board = game.getBoardView();

        //get a list of all valid simple moves
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<Move> jumps = new ArrayList<>();
        for (int a = 0; a < 8; a++) {for (int b = 0; b < 8; b++) {
            Piece piece = board.getRow(a).getSpace(b).getPiece();
            Position start = new Position(a,b);
            if(piece.getColor() == Piece.Color.WHITE){
                //If there is a jump add the jump
                if(game.canJump(start)){jumps.addAll(game.getPossibleJumps());}
                //else add valid moves a piece can make
                ArrayList<Move> tempMoves = game.canMove(start);
                moves.addAll(tempMoves);
        }}}

        if(game.forceJump()){return jumps;}
        else{return moves;}
    }
}
