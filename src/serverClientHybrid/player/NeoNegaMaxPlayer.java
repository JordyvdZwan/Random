package serverClientHybrid.player;

import serverClientHybrid.Exception.InvalidMoveException;
import serverClientHybrid.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static serverClientHybrid.model.Board.DIM;

public class NeoNegaMaxPlayer implements Player {

    private static final int WIN = 2000;
    private static final int DEPTH = 4;
    private static final int TRANSPOSITIONDEPTH = 1;
    private String type;
    private String name;
    private String otherType;
    private Map<String[][][], Integer> transPositionTable;
    private int alphabeta = 0;
    private int transposition = 0;

    public NeoNegaMaxPlayer(String type, String name) {
        this.type = type;
        this.name = name;
        otherType = type.equals(Board.RED) ? Board.YELLOW : Board.RED;
    }


    @Override
    public Move getMove(Board board) {
        alphabeta = 0;
        transposition = 0;
        transPositionTable = new HashMap<>();

        Move optimalMove = new Move();
        int maxScore = Integer.MIN_VALUE;
        for (Move move : allMoves(board, 1)) {
            try {
                board.setMove(move);
                int score = neonegamax(move, board, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
                if (score > maxScore) {
                    optimalMove = move;
                    maxScore = score;
                }
                board.removeMove(move);
//                System.out.println(move.toString() + " score= " + score);
            } catch (InvalidMoveException e) {
                e.printStackTrace();
            }
        }
        System.out.println("alpha beta = " + alphabeta);
        System.out.println("transposition = " + transposition);
        return optimalMove;
    }

    private int neonegamax(Move parent, Board board, int depth, int alpha, int beta, int type) {
        if (board.playerWin(parent)) {
            return -type * WIN * depth;
        } else if (depth == 0) {
            board.removeMove(parent);
            return -type * board.getScore(parent);
        } else {
            if (depth > TRANSPOSITIONDEPTH) {
                for (String[][][] key : transPositionTable.keySet()) {
                    if (Arrays.deepEquals(board.board, key)) {
                        transposition++;
//                        System.out.println(board);
                        return transPositionTable.get(key);
                    }
                }
            }
            if (type == 1) {
                int v;
                int result = Integer.MIN_VALUE;
                ArrayList<Move> allMoves = allMoves(board, type);
                for (Move newMove : allMoves) {
                    try {
                        board.setMove(newMove);
                        v = neonegamax(newMove, board, depth - 1, alpha, beta, -type);
                        result = Math.max(v, result);
                        alpha = Math.max(alpha, v);
                        board.removeMove(newMove);
                        if (alpha >= beta) {
                            alphabeta++;
                            break;
                        }
                    } catch (InvalidMoveException e) {
                        e.printStackTrace();
                    }
                }
                if (depth > TRANSPOSITIONDEPTH) {
//                    String[][][] node = new String[board.DIM][board.DIM][board.DIM];
                    transPositionTable.put(myClone(board.board, board.DIM), result);
                }
                return result;
            } else {
                int v;
                int result = Integer.MAX_VALUE;
                ArrayList<Move> allMoves = allMoves(board, type);
                for (Move newMove : allMoves) {
                    try {
                        board.setMove(newMove);
                        v = neonegamax(newMove, board, depth - 1, alpha, beta, -type);
                        result = Math.min(v, result);
                        beta = Math.min(beta, v);
                        board.removeMove(newMove);
                        if (alpha >= beta) {
                            alphabeta++;
                            break;
                        }
                    } catch (InvalidMoveException e) {
                        e.printStackTrace();
                    }
                }
                if (depth > TRANSPOSITIONDEPTH) {
//                    String[][][] node = new String[board.DIM][board.DIM][board.DIM];
                    transPositionTable.put(myClone(board.board, board.DIM), result);
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
                    result.add(new Move((type == 1) ? this.type : otherType, x, z));
                }
            }
        }
        return result;
    }

    private String[][][] myClone(String [][][] source, int size) {
        String[][][] result = new String[size][size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j][k] = source[i][j][k];
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
