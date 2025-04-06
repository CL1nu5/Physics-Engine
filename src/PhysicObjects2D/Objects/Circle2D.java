package PhysicObjects2D.Objects;

import PhysicObjects2D.CollisionInfo;
import Support.ColorMix;
import Support.MinMax;
import PhysicObjects2D.Object2D;
import PhysicObjects2D.Vector2D;

import java.awt.*;

public class Circle2D extends Object2D implements Cloneable{

    /* values */

    public double radius;

    /* constructors */

    //creates circle with set radius
    public Circle2D (double radius, Vector2D position, Vector2D velocity) {
        this(radius, position, velocity, 1, 0);
    }

    //creates circle with set radius
    public Circle2D (double radius, Vector2D position, Vector2D velocity, double scale, double rotation) {
        this(radius, position, velocity, scale, rotation, 1, ColorMix.BLACK_RIM);
    }

    //creates circle with set radius
    public Circle2D (double radius, Vector2D position, Vector2D velocity, double scale, double rotation, double mass, ColorMix colorMix) {
        super(position, velocity, scale, rotation, mass, colorMix);
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
    public boolean contains(Vector2D vector) {
        return this.position.getDistance(vector) < this.radius * this.scale;
    }


    @Override
    public void paint(Graphics2D g) {
        //get transformed values
        Circle2D circle = (Circle2D) this.getTransformed();

        //process values
        Vector2D start = circle.position.sub(new Vector2D(circle.radius, circle.radius));
        double diameter = circle.radius * 2;

        //paint fill
        if (colorMix.fill != null) {
            g.setColor(colorMix.fill);
            g.fillOval((int) start.x,(int) start.y,(int) diameter,(int) diameter);
        }

        //paint middle point (radius needs to be bigger than 10 for the middle point to make sense)
        if (colorMix.middlePoint != null && diameter >= 20) {
            g.setColor(colorMix.middlePoint);
            g.fillOval((int)(circle.position.x - 4), (int) (circle.position.y - 4), 8, 8);
        }

        //paint rim
        if (colorMix.border != null) {
            g.setColor(colorMix.border);
            g.drawOval((int) start.x,(int) start.y,(int) diameter,(int) diameter);
        }
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

        //difference = distance between
        double diff = radiusTotal - distanceBetween;
        result.distance = diff;

        //separation distance is based on the vector and the difference
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
