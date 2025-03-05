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
        super();
        this.vertices = vertices;
    }

    //creates polygon with set values
    public Polygon2D(Vector2D[] vertices, Vector2D position, Vector2D velocity, double scale, double rotation) {
        super(position, velocity, scale, rotation);
        this.vertices = vertices;
    }

    //creates a standard polygon shape with a set number of sides
    public Polygon2D(int sides, double radius) {
        this(sides, radius, new Vector2D(), new Vector2D(), 1, 0);
    }

    //creates a standard polygon shape with a set number of sides
    public Polygon2D(int sides, double radius, Vector2D position, Vector2D velocity) {
        this(sides, radius, position, velocity, 1, 0);
    }

    //creates a standard polygon shape with a set number of sides
    public Polygon2D(int sides, double radius, Vector2D position, Vector2D velocity, double scale, double rotation) {
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

        else if (collisionObject instanceof Circle2D) {
            Circle2D that = (Circle2D) collisionObject;
            result = checkCircle(that).getOpposite();
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
        Polygon2D poly = (Polygon2D) this.getTransformed();

        g.setColor(Color.BLACK);

        for (int i = 0 ; i < poly.vertices.length; i++){
            int next = i + 1;
            if (next == poly.vertices.length)
                next = 0;

            Vector2D p1 = poly.vertices[i].add(poly.position), p2 = poly.vertices[next].add(poly.position);

            g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        }

        //paint middle point
        g.fillOval((int)(poly.position.x - 4), (int) (poly.position.y - 4), 8, 8);
    }

    /* collision checks */

    //checks the collision between this polygon and another polygon and returns the collision information
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

    //checks the collision between this polygon and a circle
    CollisionInfo checkCircle(Circle2D circle) {
        //create result object
        CollisionInfo result = new CollisionInfo(this, circle);

        //get transformed objects (copies)
        Polygon2D thisT = (Polygon2D) this.getTransformed();
        Circle2D circleT = (Circle2D) circle.getTransformed();

        //find the closest point
        double shortestDistance = Double.MAX_VALUE;
        Vector2D closestVertex = new Vector2D();

        for (Vector2D vertex : thisT.vertices) {
            double distance = Math.pow(circleT.position.x - (thisT.position.x + vertex.x), 2) +
                    Math.pow(circleT.position.y - (thisT.position.y + vertex.y), 2);

            if (distance < shortestDistance) {
                shortestDistance = distance;
                closestVertex.x = thisT.position.x + vertex.x;
                closestVertex.y = thisT.position.y + vertex.y;
            }
        }

        //calculate the axis from the circle to the point
        Vector2D axis = new Vector2D(closestVertex.x - circleT.position.y, closestVertex.y - circle.position.y).normalize();

        //project the polygon onto the axis
        MinMax polyRange = projectVerticesForMinMax(thisT.vertices, axis);

        //get the offset between the two shapes
        Vector2D vOffset = thisT.position.sub(circleT.position);

        //shift the polygon along the axis
        double scalarOffset = axis.mul(vOffset);
        polyRange.min += scalarOffset;
        polyRange.max += scalarOffset;

        //project the circle onto this axis
        MinMax circleRange = circleT.projectCircleForMinMax(axis);

        //check for a gap
        if ((polyRange.min - circleRange.max > 0) || circleRange.min - polyRange.max > 0) {
            return null;
        }

        //calc the separation and store if this is the shortest
        double distMin = (circleRange.max - polyRange.min);
        shortestDistance = Math.abs(distMin);

        result.distance = distMin;
        result.separationDirection = axis;

        this.checkRangesForContainment(polyRange, circleRange, result);

        //loop over the polygon sides
        for (int i = 0; i < thisT.vertices.length; i++){
            //project onto axis
            axis = getPerpendicularAxis(vertices, i);
            polyRange = projectVerticesForMinMax(vertices, axis);

            //shift the first polygons min max along the axis by the amount of offset between them
            scalarOffset = axis.mul(vOffset);
            polyRange.min += scalarOffset;
            polyRange.max += scalarOffset;

            //project the circle onto the axis
            circleRange = circleT.projectCircleForMinMax(axis);
            if ((polyRange.min - circleRange.max > 0) || circleRange.min - polyRange.max > 0) {
                return null;
            }

            //check ranges for containment
            checkRangesForContainment(polyRange, circleRange, result);
            distMin = (circleRange.max - polyRange.min);

            //check if this is the shortest by using the absolute val
            double distMinAbs = Math.abs(distMin);
            if (distMinAbs < shortestDistance) {
                shortestDistance = distMinAbs;

                result.distance = distMin;
                result.separationDirection = axis;
            }
        }

        //calc the final separation
        result.separationDistance = result.separationDirection.mul(result.distance);

        //return the result
        return result;
    }

    // returns the perpendicular axis for a by index selected vertex
    private Vector2D getPerpendicularAxis(Vector2D[] vertices, int index) {
        Vector2D point1 = vertices[index];
        Vector2D point2 = index >= vertices.length - 1 ? vertices[0] : vertices[index + 1];

        Vector2D axis = new Vector2D(-(point2.y - point1.y), point2.x - point1.x);
        axis = axis.normalize();

        return axis;
    }

    //projects the minimum and maximum "shadow" value for the object projected on the axis
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

    //checks if the shape is fully contained by the other object
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

    //returns a string of the polygon with all it's vertices
    public String toString() {
        StringBuilder result = new StringBuilder("Polygon - ");

        result.append(position).append(": {");

        result.append("velocity=").append(velocity)
                .append(",scale=").append(scale)
                .append(",rotation=").append(rotation);

        result.append("} - {");

        for (int i = 0; i <= vertices.length - 1; i++) {
            result.append(vertices[i].add(position).toString());

            if (i != vertices.length - 1) {
                result.append(",");
            }
        }

        return result + "}";
    }
}
