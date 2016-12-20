package serverClientHybrid.Tests;

import serverClientHybrid.Exception.InvalidMoveException;
import serverClientHybrid.model.Board;
import org.junit.*;
import serverClientHybrid.model.Move;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static serverClientHybrid.model.Board.RED;


/**
 * Created by reinier on 14-12-2016.
 */
public class BoardTest {

    Board board;


    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testRemoveMove() {
        Board test = new Board();
        try {
            Move move = new Move(RED, 1, 1);
            test.setMove(move);
            assertFalse(test.myEquals(board));
            test.removeMove(move);
            assertTrue(test.myEquals(board));
        } catch (InvalidMoveException e) {

        }
    }
}
