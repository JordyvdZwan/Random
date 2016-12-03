package serverClientHybrid.player;

import serverClientHybrid.Exception.InvalidMoveException;
import serverClientHybrid.model.Board;
import serverClientHybrid.model.MiniMaxTreeLink;
import serverClientHybrid.model.Move;

import static serverClientHybrid.model.Board.DIM;

/**
 * Created by Gebruiker on 21-11-2016.
 */
public class SupremePlayer implements Player {
    private String type;
    private String otherType;
    private Board board;

    public SupremePlayer(String type, Board board) {
        this.board = board;
        this.type = type;
        otherType = type.equals(Board.RED) ? Board.YELLOW : Board.RED;
    }

    @Override
    public Move getMove(Move lastMove, Board board) {
        this.board = board;
        Move move;
        MiniMaxTreeLink max = generateBeginTree(board);
        move = max.getHighestScoreNextMove();
        System.out.println(board.printScore(type, board));
        return move;
    }

    private MiniMaxTreeLink generateBeginTree(Board board) {
        int depth = 3;
        System.out.println("Starting begin tree generation.");
        long start = System.currentTimeMillis();
        MiniMaxTreeLink tree = new MiniMaxTreeLink(null);
        for (int x = 0; x < DIM; x++) {
            for (int z = 0; z < DIM; z++) {
                System.out.println("Time since start: " + (System.currentTimeMillis() - start));
                Board copy = board.deepCopy();
                try {
                    Move move = copy.setMove(x, z, type);
                    MiniMaxTreeLink child = generateBeginTree(copy, tree, move, depth);
                    tree.putNext(move, child);
                } catch (InvalidMoveException e) {
                    //ignore...
                }
            }
        }
        System.out.println("[DONE] Time since start: " + (System.currentTimeMillis() - start));
        return tree;
    }

    private MiniMaxTreeLink generateBeginTree(Board board, MiniMaxTreeLink parent, Move lastMove, int depth) {
        MiniMaxTreeLink link = new MiniMaxTreeLink(parent);
        if (depth == 0) {
            return null;
        } else {
            for (int x = 0; x < DIM; x++) {
                for (int z = 0; z < DIM; z++) {
                    Board copy = board.deepCopy();

                    try {
                        Move move = copy.setMove(x, z, lastMove.getType().equals(Board.RED) ? Board.YELLOW : Board.RED);
                        MiniMaxTreeLink child = generateBeginTree(copy, link, move, depth - 1);
                        if (child != null) {
                            child.setScore(board.getScore(move));
                        }
                        link.putNext(move, child);
                    } catch (InvalidMoveException e) {
                    }
                }
            }
            if (link.hasNoChildren()) {
                link.setScore(0);
            } else if (link.getNext(link.getKeyset().iterator().next()) != null) {
                if (lastMove.getType().equals(type)) {
                    //max
                    int max = 0;
                    for (Move key : link.getKeyset()) {
                        max = link.getNext(key).getScore() > max ? link.getNext(key).getScore() : max;
                    }
                    link.setScore(max);
                } else {
                    //min
                    int min = 0;
                    for (Move key : link.getKeyset()) {
                        min = link.getNext(key).getScore() < min ? link.getNext(key).getScore() : min;
                    }
                    link.setScore(min);
                }
            }
        }
        return link;
    }


    @Override
    public String getName() {
        return "Admiral general Alladeen MOTHERFUCKERS!!!!!";
    }

}
