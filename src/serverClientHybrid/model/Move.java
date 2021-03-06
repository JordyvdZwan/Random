package serverClientHybrid.model;

/**
 * Created by Jordy van der Zwan on 17-Nov-16.
 */
public class Move {
    private String type;
    private int x;
    private int y;
    private int z;

    public Move(String type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Move(String type, int x, int z) {
        this.type = type;
        this.x = x;
        this.z = z;
    }

    public Move() {

    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return "type= " + type + " x= " + x + " y= " + y + " z= " + z;
    }
}
