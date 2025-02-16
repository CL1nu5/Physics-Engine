package PhysicObjects2D.Objects;

import PhysicObjects2D.CollisionInfo;
import PhysicObjects2D.MinMax;
import PhysicObjects2D.Object2D;
import PhysicObjects2D.Vector2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Arrays;

public class Polygon2D extends Object2D implements Cloneable {

    private static final Logger logger = LogManager.getLogger(Polygon2D.class);

    /* values */

    public Vector2D[] vertices;

    /* constructors */

    //creates polygon with vertices
    public Polygon2D(Vector2D[] vertices) {
        this.vertices = vertices;
    }

    //creates polygon with set values
    public Polygon2D(Vector2D[] vertices, Vector2D position, Vector2D velocity, double scale, double rotation) {
        super(position, velocity, scale, rotation);
        this.vertices = vertices;
    }

    //creates a standard polygon shape with a set number of sides
    public Polygon2D(int sides, double radius) throws Exception {
        this(sides, radius, new Vector2D(), new Vector2D(), 1, 0);
    }


    //creates a standard polygon shape with a set number of sides
    public Polygon2D(int sides, double radius, Vector2D position, Vector2D velocity, double scale, double rotation) throws Exception {
        //sets super values
        super(position, velocity, scale, rotation);

        //creates vertices array
        vertices = new Vector2D[sides];

        //number of sides cant be below 3
        if (sides < 3) {
            logger.error("Number of sides can't be below 3 for a polygon! Number of sides selected: {}! Throwing error ...", sides);
            throw new RuntimeException("Number of sides can't be below 3 for a polygon!");
        }

        //setup for creating the poly
        double rotationPerPoint = (Math.PI * 2) / sides;
        double angle = 0;

        //loop to generate each point
        for (int i = 0; i < sides; i++) {
            angle = (i * rotationPerPoint) + (Math.PI - rotationPerPoint) * 0.5;
            Vector2D point = new Vector2D(Math.cos(angle) * radius, Math.sin(angle) * radius);
            vertices[i] = point;
        }
    }


    /* overridden abstract methods */

    @Override
    public CollisionInfo checkCollisions(Object2D collisionObject) {
        CollisionInfo result = null;

        if (collisionObject instanceof Polygon2D) {
            Polygon2D that = (Polygon2D) collisionObject;

            //check first polygon
            CollisionInfo testAB = this.checkPoly(that);
            if (testAB == null)
                return null;

            //check second polygon
            CollisionInfo testBA = that.checkPoly(this);
            if (testBA == null)
                return null;

            //get the result with the shortest distance
            result = (Math.abs(testAB.distance) < Math.abs(testBA.distance)) ? testAB : testBA;

            //set contained shapes
            result.shapeAContained = testAB.shapeAContained && testBA.shapeBContained;
            result.shapeBContained = testAB.shapeBContained && testBA.shapeAContained;
        }

        return result;
    }

    //returns transformed object based on scale and rotation
    @Override
    public Object2D getTransformed() {
        //cloning all vertices
        Polygon2D transformed = this.clone();

        //transforming each vertex
        for (Vector2D vertex : transformed.vertices) {
            if (this.rotation != 0) {
                double hyp = Math.sqrt(Math.pow(vertex.x, 2) + Math.pow(vertex.y, 2));
                double angle = Math.atan2(vertex.y, vertex.x);
                angle += this.rotation * (Math.PI / 180);

                vertex.x = Math.cos(angle) * hyp;
                vertex.y = Math.sin(angle) * hyp;
            }

            if (this.scale != 0) {
                vertex.x *= this.scale;
                vertex.y *= this.scale;
            }
        }

        //set rotation and scale to 0 and 1
        transformed.rotation = 0;
        transformed.scale = 1;

        //returns transformed polygon
        return transformed;
    }

    //paint polygon
    @Override
    public void paint(Graphics2D g) {
        Polygon2D poly = (Polygon2D) getTransformed();

        g.setColor(Color.BLACK);

        for (int i = 0 ; i < poly.vertices.length; i++){
            int next = i + 1;
            if (next == poly.vertices.length)
                next = 0;

            Vector2D p1 = poly.vertices[i].add(poly.position), p2 = poly.vertices[next].add(poly.position);

            g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);

            g.fillOval((int)(poly.position.x - 4), (int) (poly.position.y - 4), 8, 8);
        }
    }

    /* collision checks */

    private CollisionInfo checkPoly(Polygon2D that) {
        //setup
        double shortestDistance = Double.MAX_VALUE;
        CollisionInfo result = new CollisionInfo(this, that);

        //transform
        Vector2D[] vertices1 = ((Polygon2D) this.getTransformed()).vertices;
        Vector2D[] vertices2 = ((Polygon2D) that.getTransformed()).vertices;

        //get offset
        Vector2D offset = this.position.sub(that.position);

        //loop over all the sides on the first polygon and check the perpendicular axis
        for (int i = 0; i < vertices1.length; i++) {
            //get perpendicular axis to project onto
            Vector2D axis = getPerpendicularAxis(vertices1, i);

            //project each point onto the axis
            MinMax thisRange = projectVerticesForMinMax(vertices1, axis);
            MinMax thatRange = projectVerticesForMinMax(vertices2, axis);

            // shift the first polygons min max along the axis by the amount of offset between them
            double scaleOffset = axis.mul(offset);
            thisRange.min += scaleOffset;
            thisRange.max += scaleOffset;

            //check for a gap between the relative min's and max's
            if ((thisRange.min - thatRange.max > 0) || (thatRange.min - thisRange.max > 0)) {
                //gap found
                return null;
            }

            //check for containment
            checkRangesForContainment(thisRange, thatRange, result);

            //calc the separation and store if this the shortest
            double minDistance = (thatRange.max - thisRange.min) * -1;

            // check if this is the shortest by using absolute value
            double minDistanceAbs = Math.abs(minDistance);
            if (minDistanceAbs < shortestDistance) {
                shortestDistance = minDistanceAbs;

                result.distance = minDistance;
                result.separationDirection = axis;
            }
        }

        //calculate the final separation distance
        result.separationDistance = new Vector2D(result.separationDirection.x * result.distance,
                result.separationDirection.y * result.distance);

        //return result
        return result;
    }

    private Vector2D getPerpendicularAxis(Vector2D[] vertices, int index) {
        Vector2D point1 = vertices[index];
        Vector2D point2 = index >= vertices.length - 1 ? vertices[0] : vertices[index + 1];

        Vector2D axis = new Vector2D(-(point2.y - point1.y), point2.x - point1.x);
        axis = axis.normalize();

        return axis;
    }

    private MinMax projectVerticesForMinMax(Vector2D[] vertices, Vector2D axis) {
        double min = axis.mul(vertices[0]);
        double max = min;

        for (Vector2D vertex : vertices) {
            double temp = axis.mul(vertex);
            if (temp < min) min = temp;
            if (temp > max) max = temp;
        }

        return new MinMax(min, max);
    }

    private void checkRangesForContainment(MinMax rangeA, MinMax rangeB, CollisionInfo info) {
        if (rangeA.max > rangeB.max || rangeA.min < rangeB.min) info.shapeAContained = false;
        if (rangeB.max > rangeA.max || rangeB.min < rangeA.min) info.shapeBContained = false;
    }

    /* basic methods */

    //cloning object and vectors
    @Override
    public Polygon2D clone() {
        try {
            Polygon2D clone = (Polygon2D) super.clone();
            clone.vertices = Arrays.stream(this.vertices).map(Vector2D::clone).toArray(Vector2D[]::new);
            return clone;
        } catch (AssertionError e) {
            throw new AssertionError();
        }
    }
}
