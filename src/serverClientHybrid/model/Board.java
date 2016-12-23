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
    public String[][][] board;

    public Board(String[][][] board) {
        this.board = board;
    }

    public int getScore(Move move) {
        int result = score[move.getX()][move.getY()][move.getZ()];
        int count = 1;
        if (board[move.getX()][move.getY()][move.getZ()] != null) {
            return 0;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    k = (i == 0 && j == 0 && k == 0) ? 1 : k;
                    if (countType(move, i, j, k) == -1) {
                        result--;
                    } else {
                        count += countType(move, i, j, k);
                    }
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
        int rowCount = 0;
        int result = 0;
        while (inBorder(tempX, tempY, tempZ)) {
            if (board[tempX][tempY][tempZ] != null) {
                if (board[tempX][tempY][tempZ].equals(type)) {
                    plus++;
                } else if (!board[tempX][tempY][tempZ].equals(type)) {
                    minus++;
                }
            }
            rowCount++;
            tempX -= diffX;
            tempY -= diffY;
            tempZ -= diffZ;
        }
        tempX = move.getX();
        tempY = move.getY();
        tempZ = move.getZ();
        while (inBorder(tempX, tempY, tempZ)) {
            if (board[tempX][tempY][tempZ] != null) {
                if (board[tempX][tempY][tempZ].equals(type)) {
                    plus++;
                } else if (!board[tempX][tempY][tempZ].equals(type)) {
                    minus++;
                }
            }
            tempX += diffX;
            tempY += diffY;
            tempZ += diffZ;
            rowCount++;
        }
        if (rowCount == 5) {
            result += (minus > 0 && minus < 3) ? -1 : plus;
            result += (minus >= 3) ? 20 : 0;
            result += (plus == 3) ? 50 : 0;
        }
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

    public void removeMove(Move move) {
//        int x = move.getX();
//        int z = move.getZ();
//        int y = 0;
//        while (y <= DIM - 1) {
//            if (y == DIM - 1 || board[x][y + 1][z] == null) {
//                board[x][y][z] = null;
//                break;
//            }
//            y++;
//        }
        board[move.getX()][move.getY()][move.getZ()] = null;
    }

    public Move setMove(Move move) throws InvalidMoveException {
        Move set = setMove(move.getX(), move.getZ(), move.getType());
        move.setY(set.getY());
        return move;
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

    public boolean myEquals(String[][][] node) {
        for (int x = 0; x < DIM; x++) {
            for (int y = 0; y < DIM; y++) {
                for (int z = 0; z < DIM; z++) {
                    if (board[x][y][z] != null && node[x][y][z] != null) {
                        if (!board[x][y][z].equals(node[x][y][z])) {
                            return false;
                        }
                    } else if (board[x][y][z] != null ^ node[x][y][z] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean myEquals(Board node) {
        for (int x = 0; x < DIM; x++) {
            for (int y = 0; y < DIM; y++) {
                for (int z = 0; z < DIM; z++) {
                    if (board[x][y][z] != null && node.getBoard()[x][y][z] != null) {
                        if (!board[x][y][z].equals(node.getBoard()[x][y][z])) {
                            return false;
                        }
                    } else if (board[x][y][z] != null ^ node.getBoard()[x][y][z] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Board rotateCounterClock() {
        Board result = new Board();
        String[][][] rotate = new String[DIM][DIM][DIM];
        for (int y = 0; y < DIM; y++) {
            for (int x = 0; x < DIM; x++) {
                for (int z = DIM - 1; z >= 0; z--) {
                    rotate[x][y][DIM - 1 - z] = board[z][y][x];
                }
            }
        }
        result.setBoard(rotate);
        return result;
    }

    public Board rotateClockWise() {
        Board result = new Board();
        String[][][] rotate = new String[DIM][DIM][DIM];
        for (int y = 0; y < DIM; y++) {
            for (int x = DIM - 1; x >= 0; x--) {
                for (int z = 0; z < DIM; z++) {
                    rotate[DIM - 1 - x][y][z] = board[z][y][x];
                }
            }
        }
        result.setBoard(rotate);
        return result;
    }

    public String printScore(String type, Board board1) {
        String boardString = "";
        for (int z = 0; z < DIM; z++) {
            for (int y = 0; y < DIM; y++) {
                for (int x = 0; x < DIM; x++) {
                    int score = board1.getScore(new Move(type, x, y ,z));
                    String stringScore = "||" + score;
                    boardString = boardString + stringScore;
                }
                boardString = boardString.concat("|<Y=" + y + "|");
            }
            boardString = boardString.concat("\n");
        }
        return boardString;
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
