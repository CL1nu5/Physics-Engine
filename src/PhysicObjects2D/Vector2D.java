package PhysicObjects2D;

public class Vector2D implements Cloneable {

    /* values */

    public double x, y;

    /* constructors */

    //creates vector with both x and y being zero
    public Vector2D() {
        this(0, 0);
    }

    //initiates values of x and y
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /* public methods */

    //returns the vector normalized
    public Vector2D normalize() {
        double magnitude = getDistance();

        if (magnitude == 0) {
            return new Vector2D(0, 0);
        }

        return new Vector2D(x / magnitude, y / magnitude);
    }

    //returns distance/magnitude
    public double getDistance() {
        return Math.sqrt(x * x + y * y);
    }


    /* basic methods */

    //clones the vector
    @Override
    public Vector2D clone() {
        try {
            return (Vector2D) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Vector2D(this.x, this.y);
        }
    }

    // returns true, if the other vector is the same object, or has the same x and y values
    @Override
    public boolean equals(Object that) {
        //same object
        if (this == that)
            return true;

        // can't be null or not the same class
        if (that == null || getClass() != that.getClass())
            return false;

        //checks x and y values
        Vector2D vector = (Vector2D) that;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
    }

    //returns a string of the vector: [x="x-value";y="y-value"]
    @Override
    public String toString() {
        return "[x=" + x + ";y=" + y + "]";
    }
}
