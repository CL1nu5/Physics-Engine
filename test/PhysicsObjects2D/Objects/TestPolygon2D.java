package PhysicsObjects2D.Objects;

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

    //tests if cloning the polygon actually crates a completely new object
    public void testClone() {
        //creating a new polygon
        Polygon2D polygon = new Polygon2D(5, 200, new Vector2D(250, 700), new Vector2D());

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
    }
}
