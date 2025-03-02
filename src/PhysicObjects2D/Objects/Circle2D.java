package PhysicObjects2D.Objects;

import PhysicObjects2D.CollisionInfo;
import PhysicObjects2D.MinMax;
import PhysicObjects2D.Object2D;
import PhysicObjects2D.Vector2D;

import java.awt.*;

public class Circle2D extends Object2D implements Cloneable{

    /* values */

    public double radius;

    /* constructors */

    //creates circle with set radius
    public Circle2D (double radius) {
        super();
        this.radius = radius;
    }

    //creates circle with set radius
    public Circle2D (double radius, Vector2D position, Vector2D velocity) {
        this(radius, position, velocity, 1, 0);
    }

    //creates circle with set radius
    public Circle2D (double radius, Vector2D position, Vector2D velocity, double scale, double rotation) {
        super(position, velocity, scale, rotation);
        this.radius = radius;
    }

    /* overridden abstract methods */

    @Override
    public CollisionInfo checkCollisions(Object2D collisionObject) {
        CollisionInfo result = null;

        if (collisionObject instanceof Circle2D) {
            result = checkCircle((Circle2D) collisionObject);
        }

        else if (collisionObject instanceof Polygon2D) {
            Polygon2D that = (Polygon2D) collisionObject;
            result = that.checkCircle(this);
        }

        return result;
    }

    @Override
    public Object2D getTransformed() {
        Circle2D transformed = this.clone();

        transformed.radius *= transformed.scale;
        transformed.scale = 0;

        return transformed;
    }

    @Override
    public void paint(Graphics2D g) {
        Circle2D circle = (Circle2D) this.getTransformed();

        g.setColor(Color.BLACK);

        Vector2D start = circle.position.sub(new Vector2D(circle.radius, circle.radius));
        double diameter = circle.radius * 2;

        g.drawOval((int) start.x,(int) start.y,(int) diameter,(int) diameter);

        //paint middle point
        g.fillOval((int)(circle.position.x - 4), (int) (circle.position.y - 4), 8, 8);
    }

    /* collision checks */

    //checks collision wit another circle
    private CollisionInfo checkCircle(Circle2D that) {
        //transform both circles
        Circle2D thisTransformed = (Circle2D) this.getTransformed();
        Circle2D thatTransformed = (Circle2D) that.getTransformed();

        //get values
        double radiusTotal = thisTransformed.radius + thatTransformed.radius;
        double distanceBetween = this.position.getDistance(that.position);

        //too far apart
        if (distanceBetween > radiusTotal) {
            return null;
        }

        //there is an overlap
        CollisionInfo result = new CollisionInfo(this, that);

        //vector is based on the center points
        result.separationDirection = that.position.sub(this.position).normalize();

        //distance between
        result.distance = distanceBetween;

        //separation distance is based on the vector and the difference
        double diff = radiusTotal - distanceBetween;
        result.separationDistance = result.separationDirection.mul(diff);

        //calc if they are contained based on if the shape smaller and too close
        double thisRad = thisTransformed.radius;
        double thatRad = thatTransformed.radius;

        result.shapeAContained = thisRad <= thatRad && distanceBetween <= thatRad - thisRad;
        result.shapeBContained = thatRad <= thisRad && distanceBetween <= thisRad - thatRad;

        //return result
        return result;
    }

    MinMax projectCircleForMinMax(Vector2D axis) {
        double proj = axis.mul(new Vector2D()); // 0,0
        return  new MinMax(proj - this.radius, proj + this.radius);
    }

    /* basic methods */
    @Override
    public Circle2D clone() {
        try {
            return (Circle2D) super.clone();
        } catch (AssertionError e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "Circle - " + position + ": {velocity=" + velocity + ",scale=" + scale + ",rotation=" + rotation +"}";
    }
}
