package com.webcheckers.model;

import java.util.ArrayList;

public class AI extends Player{

    public AI(){
        super("CPU");
    }

    /**
     * gets a valid move for CPU to do
     * @param game the game it's in
     * @return move
     */
    public ArrayList<ArrayList<Move>> allAIMoves(Game game){
        Board board = game.getBoardView();

        //get a list of all valid simple moves
        ArrayList<Move> moves = new ArrayList<>();
        for (int a = 0; a < 8; a++) {for (int b = 0; b < 8; b++) {
            Piece piece = board.getRow(a).getSpace(b).getPiece();
            Position start = new Position(a,b);
            if(piece.getColor() == Piece.Color.WHITE){
                ArrayList<Move> tempMoves = game.canMove(start);
                moves.addAll(tempMoves);
        }}}

        //get a list of all valid jumps if there are any
        ArrayList<Move> jumps = new ArrayList<>();
        for (int x = 0; x < 8; x++) {for (int y = 0; y < 8; y++) {
            Piece piece = board.getRow(x).getSpace(y).getPiece();
            Position current = new Position(x,y);
            if(piece.getColor() == Piece.Color.WHITE && game.canJump(current)){
                jumps.addAll(game.getPossibleJumps());
        }}}

        ArrayList<ArrayList<Move>> allMoves = new ArrayList<>();
        allMoves.add(jumps); allMoves.add(moves);
        return allMoves;
    }
}
