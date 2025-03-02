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

    //returns the vector in the opposite direction
    public Vector2D getOpposite() {
        return new Vector2D(-1 * this.x, -1 * this.y);
    }

    //returns distance/magnitude
    public double getDistance() {
        return Math.sqrt(x * x + y * y);
    }

    //returns the distance to another vector
    public double getDistance(Vector2D that) {
        return Math.sqrt(Math.pow(that.x - this.x, 2) + Math.pow(that.y - this.y, 2));
    }

    //returns a new vector based on the addition of both vectors
    public Vector2D add(Vector2D that) {
        return new Vector2D(this.x + that.x, this.y + that.y);
    }

    //returns a new vector based on the subtraction of the second vector from this vector
    public Vector2D sub(Vector2D that) {
        return new Vector2D(this.x - that.x, this.y - that.y);
    }

    //makes the dot product of two vectors
    public double mul(Vector2D that) {
        return (this.x * that.x) + (this.y * that.y);
    }

    //multiplies the vector by a given multiplier
    public Vector2D mul(double multiplier) {
        return new Vector2D(this.x * multiplier, this.y * multiplier);
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
