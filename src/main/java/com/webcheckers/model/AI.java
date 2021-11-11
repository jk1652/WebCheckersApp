package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Random;


public class AI extends Player{
    public enum difficulty {stupid, defensive, agressive}
    private difficulty dif;

    public AI(difficulty dif){
        super("CPU");
        this.dif = dif;
    }

    public void AIMakeMove(Game game){
        if(dif == difficulty.stupid){stupidAIMakeMove(game);}
    }

    private void stupidAIMakeMove(Game game){
        ArrayList<Move> moves = AIMoves(game);
        boolean hasJumped = false;
        while(moves.size() > 0){
            Move move = moves.get((int)(Math.random() * moves.size()));
            if(move.isMove() && !hasJumped){
                game.makeMove(move);
            }
            else if(move.isJump()){
                game.makeMove(move);
                hasJumped = true;
            } else {
                break;
            }
            moves = AIMoves(game);
        }

        game.submitMove();

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
            if( piece != null && piece.getColor() == Piece.Color.WHITE){
                //If there is a jump add the jump
                if(game.canJump(start)){jumps.addAll(game.getPossibleJumps());}
                //else add valid moves a piece can make
                ArrayList<Move> tempMoves = game.canMove(start);
                moves.addAll(tempMoves);
        }}}

        if(game.forceJump()){return jumps;}
        else{return moves;}
    }

    public difficulty getDifficulty(){return dif;}
}
