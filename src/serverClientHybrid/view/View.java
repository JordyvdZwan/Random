package serverClientHybrid.view;

import serverClientHybrid.model.Board;

import java.util.Scanner;

import static serverClientHybrid.model.Board.DIM;

/**
 * Created by Jordy van der Zwan on 16-Nov-16.
 * //([1-4])([a-zA-Z])
 */
public class View {
    private Scanner input;

    public View() {
        input = new Scanner(System.in);
    }

    public void printInputTable() {
        String table = "//|A |B |C |D \n" +
                        "01|  |  |  |  \n" +
                        "02|  |  |  |  \n" +
                        "03|  |  |  |  \n" +
                        "04|  |  |  |  ";
        printMessage(table);
    }

    public String askMove(String player) {
        System.out.print("Please enter a move for "+ player +": ");
        String in = input.nextLine();
        if (validMoveNotation(in)) {
            return in;
        }
        return askValidNotation(player);
    }

    private String askValidNotation(String player) {
        while (true) {
            System.out.print("Invalid notation of move write of form 1A: ");
            String in = input.nextLine();
            if (validMoveNotation(in)) {
                return in;
            }
        }
    }

    public String askValidMove(String player, String message) {
        System.out.print(message + ": Please enter a new move for "+ player +": ");
        String in = input.nextLine();
        if (validMoveNotation(in)) {
            return in;
        }
        return askValidNotation(player);
    }

    private boolean validMoveNotation(String move) {
        String[] parts = move.split("");
        if (parts.length != 2) return false;
        try {
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            if (Integer.parseInt(parts[0]) > 0 && Integer.parseInt(parts[0]) < DIM + 1
                    && alphabet.contains(parts[1]) && alphabet.indexOf(parts[1]) < DIM) return true;
        } catch (Exception e) {
            return false;

        }
        return false;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}
