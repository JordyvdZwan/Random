package serverClientHybrid.player;

import serverClientHybrid.model.Move;

/**
 * Created by Jordy van der Zwan on 17-Nov-16.
 */
public interface Player {

    Move getMove(Move lastMove);
    String getName();
}
