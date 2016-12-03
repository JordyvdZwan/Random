package serverClientHybrid.player;

import serverClientHybrid.Exception.InvalidMoveException;
import serverClientHybrid.model.Board;
import serverClientHybrid.model.Move;
import serverClientHybrid.view.View;

/**
 * Created by Jordy van der Zwan on 17-Nov-16.
 */
public class HumanPlayer implements Player {
    Board board;
    View view;
    String type;
    String name;

    public HumanPlayer(Board board, View view, String type, String name) {
        this.board = board;
        this.view = view;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Move getMove(Move lastMove, Board board) {
        String move = view.askMove(name);
        String alphabet = "ABCDEFGHIJ";
        int z = Integer.parseInt(move.split("")[0]) - 1;
        int x = alphabet.indexOf(move.split("")[1]);
        Board copy = board.deepCopy();
        int y = 0;
        try {
            y = copy.setMove(x, z, type).getY();
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
        Move move1 = new Move(type, x, y, z);
        return move1;
    }

    private Move getValidMove(String turn, String message) {
        String move = view.askValidMove(name, message);
        String alphabet = "ABCDEFGHIJ";
        int z = Integer.parseInt(move.split("")[0]) - 1;
        int x = alphabet.indexOf(move.split("")[1]);
        try {
            return board.setMove(x, z, turn);
        } catch (InvalidMoveException e) {
            return getValidMove(turn, e.getMessage());
        }
    }
}
