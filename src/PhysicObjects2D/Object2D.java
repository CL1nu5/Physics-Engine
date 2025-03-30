package PhysicObjects2D;

import java.awt.*;
import java.util.ArrayList;

public abstract class Object2D implements Cloneable {

    /* values */

    public Vector2D position, velocity;
    protected double scale, rotation;

    /* constructors */

    //crates a new object with the position and velocity being 0
    public Object2D() {
        this(new Vector2D(), new Vector2D(), 1, 0);
    }

    //creates a new object with the position based on the x and y values and a velocity of 0
    public Object2D(double x, double y) {
        this(new Vector2D(x, y), new Vector2D(), 1, 0);
    }

    //crestes a new object with the position and velocity being set by the parameters
    public Object2D(Vector2D position, Vector2D velocity, double scale, double rotation) {
        this.position = position;
        this.velocity = velocity;
        this.scale = scale;
        this.rotation = rotation;
    }

    /* public methods */

    //moves the object based on the velocity
    public void move() {
        position = position.add(velocity);
    }

    //moves the object based on the velocity and checks the collision with other objects
    public void move(ArrayList<Object2D> collisionObjects) {
        move();
        checkCollisions(collisionObjects);
    }

    // method checks the collision between this object and other objects and moves the objects depending
    public void checkCollisions(ArrayList<Object2D> collisionObjects) {
        //todo call check Collision an handel the returned values
    }

    /* abstract methods */

    //checks collision
    public abstract CollisionInfo checkCollisions(Object2D collisionObject);

    //abstract method, that returns the object transformed based on scale and rotation
    public abstract Object2D getTransformed();

    //method to check if a vector/point is inside an object
    public abstract boolean contains(Vector2D vector);

    //painting the object
    public abstract void paint(Graphics2D g);

    /* basic methods */

    //cloning object and vectors
    @Override
    public Object2D clone() {
        try {
            Object2D clone = (Object2D) super.clone();
            clone.position = this.position.clone();
            clone.velocity = this.velocity.clone();
            return clone;
        } catch (CloneNotSupportedException | AssertionError e) {
            throw new AssertionError();
        }
    }

    /* getter/setter */

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
