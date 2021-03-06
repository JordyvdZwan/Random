package serverClientHybrid.player;

import serverClientHybrid.Exception.InvalidMoveException;
import serverClientHybrid.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static serverClientHybrid.model.Board.DIM;

/**
 * Created by reinier on 6-12-2016.
 */
public class NegaMaxPlayer implements Player {

    private static final int WIN = 2000;
    private static final int DEPTH = 4;
    private static final int TRANSPOSITIONDEPTH = 1;
    private String type;
    private String name;
    private String othertype;
    private Map<Board, Integer> transPositionTable;
    private int alphabeta = 0;
    private int transposition = 0;

    public NegaMaxPlayer(String type, String name) {
        this.type = type;
        this.name = name;
        othertype = type.equals(Board.RED) ? Board.YELLOW : Board.RED;
    }


    @Override
    public Move getMove(Board board) {
        alphabeta = 0;
        transposition = 0;
        transPositionTable = new HashMap<>();

        Move optimalMove = new Move();
        int maxScore = Integer.MIN_VALUE;
        for (Move move : allMoves(board, 1)) {
            Board copy = board.deepCopy();
            try {
                copy.setMove(move);
                int score = negamax(move, copy, board, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
                if (score > maxScore) {
                    optimalMove = move;
                    maxScore = score;
                }
//                System.out.println(move.toString() + " score= " + score);
            } catch (InvalidMoveException e) {

            }
        }
        System.out.println("size = " + transPositionTable.size());
        System.out.println("alpha beta = " + alphabeta);
        System.out.println("transposition = " + transposition);
        return optimalMove;
    }

    private int negamax(Move parent, Board board, Board oldBoard, int depth, int alpha, int betha, int type) {
        if (board.playerWin(parent)) {
            return -type * WIN * depth;
        } else if (depth == 0) {
            return -type * oldBoard.getScore(parent);
        } else {
            if (depth > TRANSPOSITIONDEPTH) {
                for (Board key : transPositionTable.keySet()) {
                    if (key.myEquals(board)) {
                        transposition++;
                        return transPositionTable.get(key);
                    }
                }
            }
            if (type == 1) {
                int v;
                int result = Integer.MIN_VALUE;
                for (Move newMove : allMoves(board, type)) {
                    Board copy = board.deepCopy();
                    try {
                        copy.setMove(newMove);
                        v = negamax(newMove, copy, board, depth - 1, alpha, betha, -type);
                        result = Math.max(v, result);
                        alpha = Math.max(alpha, v);
                        if (alpha >= betha) {
                            alphabeta++;
                            break;
                        }
                    } catch (InvalidMoveException e) {
                    }
                }
                if (depth > TRANSPOSITIONDEPTH) {
                    transPositionTable.put(board, result);
                }
                return result;
            } else {
                int v;
                int result = Integer.MAX_VALUE;
                for (Move newMove : allMoves(board, type)) {
                    Board copy = board.deepCopy();
                    try {
                        copy.setMove(newMove);
                        v = negamax(newMove, copy, board, depth - 1, alpha, betha, -type);
                        result = Math.min(v, result);
                        betha = Math.min(betha, v);
                        if (alpha >= betha) {
                            alphabeta++;
                            break;
                        }
                    } catch (InvalidMoveException e) {
                    }
                }
                if (depth > TRANSPOSITIONDEPTH) {
                    transPositionTable.put(board, result);
                }
                return result;
            }
        }
    }

    private ArrayList<Move> allMoves(Board board, int type) {
        ArrayList<Move> result = new ArrayList<>();
        for (int x = 0; x < DIM; x++) {
            for (int z = 0; z < DIM; z++) {
                if (board.validMove(x, z)) {
                    result.add(new Move((type == 1) ? this.type : othertype, x, z));
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
