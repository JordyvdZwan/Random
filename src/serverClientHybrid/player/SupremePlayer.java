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
    public Move getMove(Move lastMove) {
        return null;
    }

    private MiniMaxTreeLink generateBeginTree(Board board) {
        int depth = 5;
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
                    System.out.println(copy.toString());
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
        } else if (board.playerWin(lastMove)) {
            link.setScore(lastMove.getType().equals(type) ? 1 : -1);
        } else {
            for (int x = 0; x < DIM; x++) {
                for (int z = 0; z < DIM; z++) {
                    Board copy = board.deepCopy();

                    try {
                        Move move = copy.setMove(x, z, lastMove.getType().equals(Board.RED) ? Board.YELLOW : Board.RED);
                        MiniMaxTreeLink child = generateBeginTree(copy, link, move, depth - 1);
                        child.setScore(board.getScore(move));
                        link.putNext(move, child);
                    } catch (InvalidMoveException e) {
                        //ignore...
                    }
                }
            }
            if (link.hasNoChildren()) {
                link.setScore(0);
            } else if (link.getNext(link.getKeyset().iterator().next()) != null) {
                if (lastMove.getType().equals(type)) {
                    //max
                    int max = -1;
                    for (Move key : link.getKeyset()) {
                        max = link.getNext(key).getScore() > max ? link.getNext(key).getScore() : max;
                    }
                    link.setScore(max);
                } else {
                    //min
                    int min = 1;
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
