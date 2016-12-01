package serverClientHybrid.model;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Gebruiker on 21-11-2016.
 */
public class MiniMaxTreeLink {
    private HashMap<Move, MiniMaxTreeLink> nextLink = new HashMap<>();
    private MiniMaxTreeLink parent;
    private int score;

    public MiniMaxTreeLink(MiniMaxTreeLink parent) {
        this.parent = parent;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public MiniMaxTreeLink getParent() {
        return parent;
    }

    public void setParent(MiniMaxTreeLink parent) {
        this.parent = parent;
    }

    public void putNext(Move key, MiniMaxTreeLink value) {
        nextLink.put(key,value);
    }

    public MiniMaxTreeLink getNext(Move key) {
        return nextLink.get(key);
    }

    public boolean hasNoChildren() {
        return nextLink.keySet().isEmpty();
    }

    public Set<Move> getKeyset() {
        return nextLink.keySet();
    }

    public Move getHighestScoreNextMove() {
        Move move = nextLink.keySet().iterator().next();
        for (Move subMove : nextLink.keySet()) {
            if (nextLink.get(subMove).getScore() > nextLink.get(move).getScore()) {
                move = subMove;
            }
        }
        return move;
    }
}
