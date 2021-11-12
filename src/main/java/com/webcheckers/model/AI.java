package com.webcheckers.model;

import java.util.ArrayList;


public class AI extends Player{
    public enum difficulty {stupid, defensive, agressive}
    private final difficulty dif;

    public AI(difficulty dif){
        super("CPU");
        this.dif = dif;
    }

    /**
     * this function houses all the main move logic in the game, because we
     * bypass validate move we insted us perrfer moves to get the legal moves
     * that the ai wants to make and dirrectly executed them by calling
     * makeMove and submitTurn
     * @param game the game that the ai is playing in
     */
    public void AIMakeMove(Game game){
        ArrayList<Move> moves;
        boolean hasJumped = false;
        while (true) {
            moves = preferMoves(game);
            Move move = moves.get((int) (Math.random() * moves.size()));

            if (move.isMove() && !hasJumped) {
                game.makeMove(move);
                break;
            } else if (move.isJump()) {
                game.makeMove(move);
                hasJumped = true;
            } else {
                break;
            }
        }
        game.submitMove();
    }

    /**
     * depending on the difficulty type of the AI it will take all legal moves from AImoves
     * and only chose the ones that follow its perferd parameters. because calcPreferMoves can
     * return null the final if is used to just return all moves
     * @param game the game the ai is in
     * @return all the moves that meet the required parameter or all moves if none meet the requirments
     */
    private ArrayList<Move> preferMoves(Game game) {
        ArrayList<Move> moves = new ArrayList<>();
        if(dif == difficulty.defensive){moves = calcPreferMoves(game,false);}
        if(dif == difficulty.agressive){moves = calcPreferMoves(game,true);}
        if(moves.size() == 0){moves = AIMoves(game);}
        return moves;
    }

    /**
     * looks at all possible moves and either avoids creating or forces
     * an force jump by the opponent depending on the agrressive boolean
     * @param game the game the ai in in
     * @param agrressive if the function shoul returrn aggresive moves or defensive moves
     * @return list of moves meeting the paramaters
     */
    private ArrayList<Move> calcPreferMoves(Game game, Boolean agrressive) {
        ArrayList<Move> moves = AIMoves(game);
        Game proxie = new Game(game.getRedPlayer(), game.getWhitePlayer());
        ArrayList<Move> perrferdMoves = new ArrayList<>();
        for(Move canMove : moves){
            proxie.setBoard(game.getBoardView());
            proxie.makeMove(canMove);
            if(agrressive == proxie.forceJump()){
                perrferdMoves.add(canMove);
            }
        }
        return perrferdMoves;
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