package PhysicsObjects2D.Objects;

import PhysicObjects2D.CollisionInfo;
import PhysicObjects2D.Objects.Polygon2D;
import PhysicObjects2D.Vector2D;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPolygon2D extends TestCase {

    /* setup */

    public TestPolygon2D(String testName) {
        super(testName);
    }

    public static TestSuite suite() {
        return new TestSuite(TestPolygon2D.class);
    }

    /* tests */

    //testing some different interaction scenarios
    public void testPolyWithPolyCollision() {
        //test1: they are intersecting
        Polygon2D poly1 = new Polygon2D(4, 100, new Vector2D(600, 330), new Vector2D(), 1, 30);
        Polygon2D poly2 = new Polygon2D(10, 200, new Vector2D(400, 400), new Vector2D(), 1, 0);

        assertNotNull(poly1.checkCollisions(poly2));

        //test2: no collision
        poly1 = new Polygon2D(4, 100, new Vector2D(700, 330), new Vector2D(), 1, 30);
        poly2 = new Polygon2D(10, 200, new Vector2D(400, 400), new Vector2D(), 1, 0);

        assertNull(poly1.checkCollisions(poly2));

        //test3: one object is fully contained (shape a)
        poly1 = new Polygon2D(4, 100, new Vector2D(400, 330), new Vector2D(), 1, 30);
        poly2 = new Polygon2D(10, 200, new Vector2D(400, 400), new Vector2D(), 1, 0);

        assertTrue(poly1.checkCollisions(poly2).shapeAContained);

        //test4: //test4: one object is fully contained (shape b)
        poly1 = new Polygon2D(7, 250, new Vector2D(400, 330), new Vector2D(), 1, 30);
        poly2 = new Polygon2D(5, 50, new Vector2D(300, 300), new Vector2D(), 1, 0);

        CollisionInfo info = poly1.checkCollisions(poly2);
        assertTrue(info.shapeBContained);

        //test5: move shape by distance and check if it is outside the object
        Polygon2D shapeB = (Polygon2D) info.shapeB;
        shapeB.position = shapeB.position.add(info.separationDistance.mul(1.000001));

        assertNull(poly1.checkCollisions(shapeB));

        //test6: move shape A by opposite direction, for a different test
        poly1 = new Polygon2D(5, 200, new Vector2D(600, 330), new Vector2D(), 1, 30);
        poly2 = new Polygon2D(3, 150, new Vector2D(340, 300), new Vector2D(), 1, 90);

        info = poly1.checkCollisions(poly2);
        assertNotNull(info);

        Polygon2D shapeA = (Polygon2D) info.shapeA;
        shapeA.position = shapeA.position.add(info.separationDistance.getOpposite().mul(1.000001)); // removing small division error

        assertNull(poly1.checkCollisions(poly2));
    }

    // tests the transform method for correctly transforming the polygon
    public void testTransform() {
        //test1: tests that the vertices length doesn't change:
        Polygon2D polygon = new Polygon2D(7, 150, new Vector2D(718, 52), new Vector2D(), 2, 0);
        Polygon2D transformed = (Polygon2D) polygon.getTransformed();

        assertEquals(polygon.vertices.length, transformed.vertices.length);

        //test2: checks, that the vertices do not change when scale is 1 and rotation 0
        polygon = new Polygon2D(3, 34, new Vector2D(352, 1623), new Vector2D(), 1, 0);
        transformed = (Polygon2D) polygon.getTransformed();

        assertTrue(polygon.vertices[0].equals(transformed.vertices[0]));

        //test3: checks that the polygon gets scaled
        polygon = new Polygon2D(5, 200, new Vector2D(250, 700), new Vector2D(), 2, 0);
        transformed = (Polygon2D) polygon.getTransformed();

        assertFalse(polygon.vertices[0].equals(transformed.vertices[0]));

        //test4: checks, that the polygon gets rotated
        polygon = new Polygon2D(9, 324, new Vector2D(523, 114), new Vector2D(), 1, 1);
        transformed = (Polygon2D) polygon.getTransformed();

        assertFalse(polygon.vertices[0].equals(transformed.vertices[0]));

        //test5: checks, that if the quader gets rotated by 90 degrees, the vertices change places
        polygon = new Polygon2D(4, 123, new Vector2D(1233, 342), new Vector2D(), 1, 90);
        transformed = (Polygon2D) polygon.getTransformed();

        assertTrue(polygon.vertices[2].equals(transformed.vertices[1]));

        //test6: check, that their values are the same, if it gets rotated by 360 degrees
        polygon = new Polygon2D(8, 341, new Vector2D(13, 332), new Vector2D(), 1, 360);
        transformed = (Polygon2D) polygon.getTransformed();

        for (int i = 0; i < polygon.vertices.length; i++) {
            assertEquals(polygon.vertices[i].round(5), transformed.vertices[i].round(5));
        }

        //test7: checks, that the polygon, can be transformed with digit numbers
        polygon = new Polygon2D(6, 289, new Vector2D(713, 922), new Vector2D(), 0.5, 23.7);
        transformed = (Polygon2D) polygon.getTransformed();

        assertFalse(polygon.vertices[0].equals(transformed.vertices[0]));

        //test8: check, that transforming an object twice has no effect
        Polygon2D transformedTransformed = (Polygon2D) transformed.getTransformed();

        for (int i = 0; i < transformed.vertices.length; i++) {
            assertEquals(transformed.vertices[i], transformedTransformed.vertices[i]);
        }
    }

    //tests if cloning the polygon actually crates a completely new object
    public void testClone() {
        //creating a new polygon
        Polygon2D polygon = new Polygon2D(100, 200, new Vector2D(250, 700), new Vector2D());

        //cloning the polygon
        Polygon2D clone = polygon.clone();

        //checking that they are not the same
        assertNotSame(polygon, clone);

        //assert, that the individual components are not the same
        assertNotSame(polygon.position, clone.position);
        assertNotSame(polygon.velocity, clone.velocity);

        //assert, that the individual components are still equal
        assertEquals(polygon.position, clone.position);
        assertEquals(polygon.velocity, clone.velocity);

        //assert, that the vertices are cloned correctly
        assertNotSame(polygon.vertices, clone.vertices);

        //check that the vertices have the same length
        assertEquals(polygon.vertices.length, clone.vertices.length);

        //assert, that the individual vertices, are not the same, but still equal
        for(int i = 0; i < polygon.vertices.length; i++) {
            assertNotSame(polygon.vertices[i], clone.vertices[i]);
            assertEquals(polygon.vertices[i], clone.vertices[i]);
        }
    }
}
