package PhysicsObjects2D.Objects;

import PhysicObjects2D.CollisionInfo;
import PhysicObjects2D.Objects.Circle2D;
import PhysicObjects2D.Objects.Polygon2D;
import PhysicObjects2D.Vector2D;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestCircle2D extends TestCase {

    /* setup */

    public TestCircle2D(String testName) {
        super(testName);
    }

    public static TestSuite suite() {
        return new TestSuite(TestCircle2D.class);
    }

    /* tests */

    //testing some different interaction scenarios
    public void testCircleWithCircleCollision() {
        //test1: they are intersecting
        Circle2D circle1 = new Circle2D(200, new Vector2D(346, 212), new Vector2D());
        Circle2D circle2 = new Circle2D(150, new Vector2D(619, 352), new Vector2D());

        assertNotNull(circle1.checkCollisions(circle2));

        //test2: no collision
        circle1 = new Circle2D(321, new Vector2D(346, 212), new Vector2D());
        circle2 = new Circle2D(149, new Vector2D(842, 126), new Vector2D());

        assertNull(circle1.checkCollisions(circle2));

        //test3: one object is fully contained (shape a)
        circle1 = new Circle2D(153, new Vector2D(292, 626), new Vector2D());
        circle2 = new Circle2D(321, new Vector2D(182, 532), new Vector2D());

        assertTrue(circle1.checkCollisions(circle2).shapeAContained);

        //test4: one object is fully contained (shape b)

        circle1 = new Circle2D(243, new Vector2D(232, 222), new Vector2D());
        circle2 = new Circle2D(111, new Vector2D(320, 291), new Vector2D());

        CollisionInfo info = circle1.checkCollisions(circle2);
        assertTrue(info.shapeBContained);

        //test5: move shape by distance and check if it is outside the object
        Circle2D shapeB = (Circle2D) info.shapeB;
        shapeB.position = shapeB.position.add(info.separationDistance.mul(1.000001));

        assertNull(circle1.checkCollisions(circle2));

        //test6: move shape A by opposite direction, for a different test
        circle1 = new Circle2D(161, new Vector2D(292, 626), new Vector2D());
        circle2 = new Circle2D(331, new Vector2D(182, 532), new Vector2D());

        info = circle1.checkCollisions(circle2);
        assertNotNull(info);

        Circle2D shapeA = (Circle2D) info.shapeA;
        shapeA.position = shapeA.position.add(info.separationDistance.getOpposite().mul(1.000001));

        assertNull(circle1.checkCollisions(circle2));
    }

    // tests the transform method for correctly transforming the circle
    public void testTransform() {
        //test1: check, that the radius scales
        Circle2D circle = new Circle2D(100, new Vector2D(), new Vector2D(), 2, 0);
        Circle2D transformed = (Circle2D) circle.getTransformed();

        assertFalse(circle.radius == transformed.radius);

        //test2: check, that transforming an object twice has no effect
        Circle2D transformedTransformed = (Circle2D) transformed.getTransformed();

        assertFalse(transformedTransformed.radius == transformed.radius);
    }

    //tests if the object gets cloned correctly
    public void testClone() {
        //creating a circle object
        Circle2D circle = new Circle2D(273, new Vector2D(152, 432), new Vector2D());

        //cloning the object
        Circle2D clone = circle.clone();

        //checking, that they are not the same object anymore
        assertNotSame(circle, clone);

        //assert, that the individual components are not the same
        assertNotSame(circle.position, clone.position);
        assertNotSame(circle.velocity, clone.velocity);

        //assert, that the individual components are still equal
        assertEquals(circle.position, clone.position);
        assertEquals(circle.velocity, clone.velocity);

        //assert, that the radius is cloned correctly
        assertNotSame(circle.radius, clone.radius);

        //check that the radii equal
        assertEquals(circle.radius, clone.radius);
    }
}
