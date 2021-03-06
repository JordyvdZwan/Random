package serverClientHybrid.player;

import serverClientHybrid.model.Board;
import serverClientHybrid.model.Move;

/**
 * Created by Jordy van der Zwan on 17-Nov-16.
 */
public interface Player {

    Move getMove(Board board);
    String getName();
}
