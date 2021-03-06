package serverClientHybrid.controller;

import serverClientHybrid.Exception.InvalidMoveException;
import serverClientHybrid.model.Board;
import serverClientHybrid.model.Move;
import serverClientHybrid.player.*;
import serverClientHybrid.view.View;

import static serverClientHybrid.model.Board.RED;
import static serverClientHybrid.model.Board.YELLOW;

/**
 * Created by Jordy van der Zwan on 16-Nov-16.
 */
public class Controller {
    static View view = new View();
    static Board board = new Board();

    static boolean win = false;
    static Player[] players = new Player[2];
    static Player turn;


    public static void main(String[] args) {
        players[0] = new NeoNegaMaxPlayer(YELLOW, "neonegamax1");
        players[1] = new NeoNegaMaxPlayer(RED, "neonegamax2");
//        players[0] = new NegaMaxPlayer(YELLOW, "negamax1");
//        players[1] = new NegaMaxPlayer(RED, "negamax2");
//        players[1] = new HumanPlayer(board, view, RED, "Reinier");
        turn = players[0];

        Move lastMove = null;
        while (!win) {
            view.printMessage(board.toString());
            view.printInputTable();
            Move move = turn.getMove(board);
            try {
                board.setMove(move.getX(), move.getZ(), move.getType());
            } catch (InvalidMoveException e) {

            }

            if (board.playerWin(move)) {
                win = true;
                break;
            }
            nextTurn();
        }
        System.out.println(board.toString());
        System.out.println("Player " + turn.getName() + " has won!!");
    }



    private static void nextTurn() {
        if (turn.equals(players[0])) {
            turn = players[1];
        } else {
            turn = players[0];
        }
    }
}
