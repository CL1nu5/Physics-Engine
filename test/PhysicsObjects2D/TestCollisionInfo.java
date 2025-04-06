package PhysicsObjects2D;

import PhysicObjects2D.Objects.Circle2D;
import PhysicObjects2D.Objects.Polygon2D;
import PhysicObjects2D.Vector2D;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestCollisionInfo extends TestCase {

    /* setup */

    public TestCollisionInfo(String testName) {
        super(testName);
    }

    public static TestSuite suite() {
        return new TestSuite(TestCollisionInfo.class);
    }

    /* tests */

    //tests if the getContainedObject method, returns the correct object
    public void testGetContainedObject() {
        //test1: no containment
        Polygon2D poly1 = new Polygon2D(4, 100, new Vector2D(600, 330), new Vector2D(), 1, 30);
        Polygon2D poly2 = new Polygon2D(10, 200, new Vector2D(400, 400), new Vector2D(), 1, 0);

        assertNull(poly1.checkCollisions(poly2).getContainedObject());

        //test2: shapeA contained
        Circle2D circle1 = new Circle2D(153, new Vector2D(292, 626), new Vector2D());
        Circle2D circle2 = new Circle2D(321, new Vector2D(182, 532), new Vector2D());

        assertSame(circle1, circle1.checkCollisions(circle2).getContainedObject());

        //test3: shapeB contained
        Circle2D circle = new Circle2D(250, new Vector2D(612,378), new Vector2D());
        Polygon2D polygon = new Polygon2D(3, 123, new Vector2D(523,434), new Vector2D());

        assertEquals(polygon, circle.checkCollisions(polygon).getContainedObject());
    }
}
