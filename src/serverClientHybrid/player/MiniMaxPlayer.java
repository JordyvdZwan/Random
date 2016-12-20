//package serverClientHybrid.player;
//
//import serverClientHybrid.Exception.InvalidMoveException;
//import serverClientHybrid.model.Board;
//import serverClientHybrid.model.MiniMaxTreeLink;
//import serverClientHybrid.model.Move;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static serverClientHybrid.model.Board.DIM;
//
///**
// * Created by Gebruiker on 21-11-2016.
// */
//public class MiniMaxPlayer implements Player {
//    private MiniMaxTreeLink beginTreelink;
//    private MiniMaxTreeLink secondTreelink;
//    private String type;
//    private String otherType;
//    private Board board;
//
//    public MiniMaxPlayer(String type, Board board) {
//        this.board = board;
//        this.type = type;
//        otherType = type.equals(Board.RED) ? Board.YELLOW : Board.RED;
//        beginTreelink = generateTree("start");
//        secondTreelink = generateTree("second");
//    }
//
//    private MiniMaxTreeLink generateBeginTree(Board board) {
//        System.out.println("Starting begin tree generation.");
//        long start = System.currentTimeMillis();
//        MiniMaxTreeLink tree = new MiniMaxTreeLink(null);
//        for (int x = 0; x < DIM; x++) {
//            for (int z = 0; z < DIM; z++) {
//                System.out.println("Time since start: " + (System.currentTimeMillis() - start));
//                Board copy = board.deepCopy();
//                try {
//                    Move move = copy.setMove(x, z, type);
//                    MiniMaxTreeLink child = generateBeginTree(copy, tree, move);
//                    System.out.println(copy.toString());
//                    tree.putNext(move, child);
//                } catch (InvalidMoveException e) {
//                    //ignore...
//                }
//            }
//        }
//        System.out.println("[DONE] Time since start: " + (System.currentTimeMillis() - start));
//        return tree;
//    }
//
//    private MiniMaxTreeLink generateBeginTree(Board board, MiniMaxTreeLink parent, Move lastMove) {
//        MiniMaxTreeLink link = new MiniMaxTreeLink(parent);
//        if (board.playerWin(lastMove)) {
//            link.setScore(lastMove.getType().equals(type) ? 1 : -1);
//        } else {
//            for (int x = 0; x < DIM; x++) {
//                for (int z = 0; z < DIM; z++) {
//                    Board copy = board.deepCopy();
//
//                    try {
//                        Move move = copy.setMove(x, z, lastMove.getType().equals(Board.RED) ? Board.YELLOW : Board.RED);
//                        MiniMaxTreeLink child = generateBeginTree(copy, link, move);
//                        link.putNext(move, child);
//                    } catch (InvalidMoveException e) {
//                        //ignore...
//                    }
//                }
//            }
//            if (link.hasNoChildren()) {
//                link.setScore(0);
//            } else {
//                if (lastMove.getType().equals(type)) {
//                    //max
//                    int max = -1;
//                    for (Move key: link.getKeyset()) {
//                        max = link.getNext(key).getScore() > max ? link.getNext(key).getScore() : max;
//                    }
//                    link.setScore(max);
//                } else {
//                    //min
//                    int min = 1;
//                    for (Move key: link.getKeyset()) {
//                        min = link.getNext(key).getScore() < min ? link.getNext(key).getScore() : min;
//                    }
//                    link.setScore(min);
//                }
//            }
//        }
//        return link;
//    }
//
//    private MiniMaxTreeLink generateSecondTree(Board board, MiniMaxTreeLink parent, Move lastMove) {
//        MiniMaxTreeLink link = new MiniMaxTreeLink(parent);
//        if (board.playerWin(lastMove)) {
//            link.setScore(lastMove.getType().equals(type) ? 1 : -1);
//        } else {
//            for (int x = 0; x < DIM; x++) {
//                for (int z = 0; z < DIM; z++) {
//                    Board copy = board.deepCopy();
//                    try {
//                        Move move = copy.setMove(x, z, lastMove.getType().equals(Board.RED) ? Board.YELLOW : Board.RED);
//                        MiniMaxTreeLink child = generateSecondTree(copy, link, move);
//                        link.putNext(move, child);
//                    } catch (InvalidMoveException e) {
//                        //ignore...
//                    }
//                }
//            }
//            if (link.hasNoChildren()) {
//                link.setScore(0);
//            } else {
//                if (lastMove.getType().equals(otherType)) {
//                    int max = -1;
//                    for (Move key: link.getKeyset()) {
//                        max = link.getNext(key).getScore() > max ? link.getNext(key).getScore() : max;
//                    }
//                    link.setScore(max);
//                } else {
//                    //min
//                    int min = 1;
//                    for (Move key: link.getKeyset()) {
//                        min = link.getNext(key).getScore() < min ? link.getNext(key).getScore() : min;
//                    }
//                    link.setScore(min);
//                }
//            }
//        }
//        return link;
//    }
//
//    private MiniMaxTreeLink generateSecondTree(Board board) {
//        System.out.println("Starting begin tree generation.");
//        MiniMaxTreeLink tree = new MiniMaxTreeLink(null);
//        long start = System.currentTimeMillis();
//        for (int x = 0; x < DIM; x++) {
//            for (int z = 0; z < DIM; z++) {
//                System.out.println("Time since start: " + (System.currentTimeMillis() - start));
//                Board copy = board.deepCopy();
//                try {
//                    Move move = copy.setMove(x, z, otherType);
//                    MiniMaxTreeLink child = generateSecondTree(copy, tree, move);
//                    tree.putNext(move, child);
//                } catch (InvalidMoveException e) {
//                    //ignore...
//                }
//            }
//        }
//        System.out.println("[DONE] Time since start: " + (System.currentTimeMillis() - start));
//        return tree;
//    }
//
//    private MiniMaxTreeLink generateTree(String startStatus) {
//        if (startStatus.equals("start")) {
//            return generateBeginTree(new Board());
//        } else {
//            return generateSecondTree(new Board());
//        }
//    }
//
//    List<Move> moveHistory = new ArrayList<>();
//
//    @Override
//    public Move getMove(Board board) {
//        Move move;
//        if (lastMove == null) {
//            //first move
//            move = beginTreelink.getHighestScoreNextMove();
//            moveHistory.add(move);
//        } else if (moveHistory.isEmpty()) {
//            //second move
//            move = secondTreelink.getNext(lastMove).getHighestScoreNextMove();
//            moveHistory.add(lastMove);
//            moveHistory.add(move);
//        } else {
//            MiniMaxTreeLink link = moveHistory.get(0).getType().equals(type) ? beginTreelink :secondTreelink;
//            for (Move subMove: moveHistory) {
//                link.getNext(subMove);
//            }
//            move = link.getHighestScoreNextMove();
//            moveHistory.add(lastMove);
//            moveHistory.add(move);
//        }
//        try {
//            return board.setMove(move.getX(), move.getY(), move.getType());
//        } catch (InvalidMoveException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public String getName() {
//        return "peop!";
//    }
//}
//
