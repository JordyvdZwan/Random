package serverClientHybrid.player;

import com.sun.org.apache.xpath.internal.operations.Neg;
import serverClientHybrid.Exception.InvalidMoveException;
import serverClientHybrid.model.*;

import java.util.ArrayList;

import static serverClientHybrid.model.Board.DIM;

/**
 * Created by reinier on 6-12-2016.
 */
public class NegaMaxPlayer implements Player {

    private static final int WIN = 2000;
    private static final int DEPTH = 3;
    private String type;
    private String name;
    private String othertype;

    public NegaMaxPlayer(String type, String name) {
        this.type = type;
        this.name = name;
        othertype = type.equals(Board.RED) ? Board.YELLOW : Board.RED;
    }


    @Override
    public Move getMove(Move lastmove, Board board){
        ArrayList<Move> possibleMoves = allMoves(board, 1);

        Move optimalMove = new Move();
        int maxScore = Integer.MIN_VALUE;
        for(Move move : possibleMoves){
            Board copy = board.deepCopy();
            try {
                copy.setMove(move);
                int score = negamax(move, copy, board, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
                if (score > maxScore){
                    optimalMove = move;
                    maxScore = score;
                }
                System.out.println(move.toString() + " score= " + score);
            } catch (InvalidMoveException e) {

            }
        }
        return optimalMove;
    }

    private int negamax(Move parent, Board board, Board oldboard, int depth, int alpha, int betha, int type) {
        if (depth == 0) {
            return -type * oldboard.getScore(parent);
        } else if (board.playerWin(parent)) {
            return -type * WIN;
        } else {
            int v;
            int result = Integer.MIN_VALUE;
            ArrayList<Move> moves = allMoves(board, type);
            for(Move newMove : moves) {
                Board copy = board.deepCopy();
                try {
                    copy.setMove(newMove);
                    v = -1 * negamax(newMove, copy, board, depth - 1, -betha, -alpha, -type);
                    result = Math.max(v, result);
                    alpha = Math.max(alpha, v);
                    if (alpha >= betha) {
                        break;
                    }
                } catch (InvalidMoveException e) {
                }
            }
            return result;
        }
    }

    private ArrayList<Move> allMoves(Board board, int type) {
        ArrayList<Move> result = new ArrayList<>();
        for (int x = 0; x < DIM; x++) {
            for (int z = 0; z < DIM; z++) {
                Board copy = board.deepCopy();
                try {
                    Move move = copy.setMove(x, z, (type == 1) ? this.type : othertype);
                    result.add(move);
                } catch (InvalidMoveException e) {
                    //ignore...
                }
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return name;
    }
}
