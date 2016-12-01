package serverClientHybrid.model;

import org.jetbrains.annotations.Contract;
import serverClientHybrid.Exception.InvalidMoveException;

import java.util.Arrays;

/**
 * Created by Jordy van der Zwan on 16-Nov-16.
 *
 * board in configuration x, y, z
 * Y is on the vertical axis.
 * X and Z are on the horizontal axis.
 */
public class Board {
    public static final int DIM = 4; //Max 10 because of move notation 0-9 No 10
    private static final int WINROW = DIM + 1;
    public static final String RED = "Red";
    public static final String YELLOW = "Yellow";
    private static final int[][][] score = {{{6,4,4,6}, {4,3,3,4}, {4,3,3,4}, {6,4,4,6}},
                                            {{3,3,3,3}, {3,6,6,3}, {3,6,6,3}, {3,3,3,3}},
                                            {{3,3,3,3}, {3,6,6,3}, {3,6,6,3}, {3,3,3,3}},
                                            {{6,4,4,6}, {4,3,3,4}, {4,3,3,4}, {6,4,4,6}}};


    //Board in the configuration X, Y, Z
    private String[][][] board;

    public Board(String[][][] board) {
        this.board = board;
    }

    public int getScore(Move move) {
        int result = score[move.getX()][move.getY()][move.getZ()];
        int count = 1;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    count += countType(move, move.getX(), move.getY(), move.getZ()) > -1 ? countType(move, move.getX(), move.getY(), move.getZ()) : -1;
                }
            }
        }
        result *= count;
        return result;
    }

    private int countType(Move move, int diffX, int diffY, int diffZ) {
        String type = move.getType();
        int tempX = move.getX();
        int tempY = move.getY();
        int tempZ = move.getZ();
        int plus = 0;
        int minus = 0;
        int result = 1;
        while (inBorder(tempX, tempY, tempZ)) {
            if (board[tempX][tempY][tempZ] == null || !board[tempX][tempY][tempZ].equals(type)) break;
            tempX -= diffX;
            tempY -= diffY;
            tempZ -= diffZ;
            minus++;
        }
        tempX = move.getX();
        tempY = move.getY();
        tempZ = move.getZ();
        while (inBorder(tempX, tempY, tempZ)) {
            if (board[tempX][tempY][tempZ] == null || !board[tempX][tempY][tempZ].equals(type)) break;
            tempX += diffX;
            tempY += diffY;
            tempZ += diffZ;
            plus++;
        }
        result += (plus == -1 || minus == -1) ? -1 : plus + minus;
        return result;
    }

    public Board deepCopy() {
        String[][][] board = new String[DIM][DIM][DIM];
        for (int x = 0; x < DIM; x++) {
            for (int y = 0; y < DIM; y++) {
                for (int z = 0; z < DIM; z++) {
                    board[x][y][z] = this.board[x][y][z];
                }
            }
        }
        return new Board(board);
    }

    public void setBoard(String[][][] board) {
        this.board = board;
    }

    public Board() {
        board = new String[DIM][DIM][DIM];
    }

    public boolean validMove(int x, int z) {
        return (board[x][DIM - 1][z] == null);
    }

    public Move setMove(int x, int z, String type) throws InvalidMoveException {
        if (!validMove(x, z)) throw new InvalidMoveException("INVALID MOVE");
        int y = DIM - 1;
        while (y >= 0) {
            if (y == 0 || board[x][y - 1][z] != null) {
                board[x][y][z] = type;
                return new Move(type, x, y, z);
            }
            y--;
        }
        throw new InvalidMoveException("Below Playing field");
    }

    @Contract(pure = true)
    private boolean inBorder(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < DIM && y < DIM && z < DIM;
    }

    private boolean isRow(Move move, int diffX, int diffY, int diffZ) {
        String type = move.getType();
        int tempX = move.getX();
        int tempY = move.getY();
        int tempZ = move.getZ();
        int plus = 0;
        int minus = 0;
        while (inBorder(tempX, tempY, tempZ)) {
            if (board[tempX][tempY][tempZ] == null || !board[tempX][tempY][tempZ].equals(type)) break;
            tempX -= diffX;
            tempY -= diffY;
            tempZ -= diffZ;
            minus++;
        }
        tempX = move.getX();
        tempY = move.getY();
        tempZ = move.getZ();
        while (inBorder(tempX, tempY, tempZ)) {
            if (board[tempX][tempY][tempZ] == null || !board[tempX][tempY][tempZ].equals(type)) break;
            tempX += diffX;
            tempY += diffY;
            tempZ += diffZ;
            plus++;
        }
        return (minus + plus >= WINROW);
    }

    public boolean playerWin(Move move) {
        return isRow(move, 1, 0, 0) || isRow(move, 0, 1, 0) || isRow(move, 0, 0, 1)
                || isRow(move, 1, 1, 0) || isRow(move, 0, 1, 1) || isRow(move, 1, 0, 1)
                || isRow(move, 1, -1, 0) || isRow(move, 0, 1, -1) || isRow(move, 1, 0, -1)
                || isRow(move, 1, 1, 1) || isRow(move, 1, -1, 1) || isRow(move, 1, 1, -1)
                || isRow(move, -1, 1, 1);
    }

    private String[][][] getBoard() {
        return board;
    }

    public String toString() {

        String boardString = "";
        for (int z = 0; z < DIM; z++) {
            for (int y = 0; y < DIM; y++) {
                for (int x = 0; x < DIM; x++) {
                    if (this.board[x][y][z] != null && this.board[x][y][z].equals(RED)) {
                        boardString = boardString.concat("|XX|");
                    } else if (this.board[x][y][z] != null && this.board[x][y][z].equals(YELLOW)){
                        boardString = boardString.concat("|OO|");
                    } else {
                        boardString = boardString.concat("|  |");
                    }
                }
                boardString = boardString.concat("|<Y=" + y + "|");
            }
            boardString = boardString.concat("\n");
//            for (int y = 0; y < DIM && z != DIM - 1; y++) {
//                for (int x = 0; x < DIM; x++) {
//                    boardString = boardString.concat("----");
//                }
//                boardString = boardString.concat(" |====| ");
//            }
//            boardString = boardString.concat("\n");
        }
        return boardString;
    }
}
