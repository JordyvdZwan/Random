package serverClientHybrid.Exception;

/**
 * Created by Jordy van der Zwan on 16-Nov-16.
 * TODO add more info to the getMessage
 */
public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Invalid move!";
    }
}
