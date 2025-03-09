package PhysicsObjects2D.Objects;

import PhysicObjects2D.CollisionInfo;
import PhysicObjects2D.Objects.Circle2D;
import PhysicObjects2D.Vector2D;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestCircle2D extends TestCase {

    /* setup */

    public TestCircle2D (String testName) {
        super(testName);
    }

    public static TestSuite suite() {
        return new TestSuite(TestCircle2D.class);
    }

    /* tests */

    //testing some different interaction scenarios
    public void testCircleWithCircleCollision() {
        //test1: they are intersecting
        Circle2D circle1 = new Circle2D(200, new Vector2D(346,212), new Vector2D());
        Circle2D circle2 = new Circle2D(150, new Vector2D(619,352), new Vector2D());

        assertNotNull(circle1.checkCollisions(circle2));

        //test2: no collision
        circle1 = new Circle2D(300, new Vector2D(346,212), new Vector2D());
        circle2 = new Circle2D(150, new Vector2D(842,126), new Vector2D());

        assertNull(circle1.checkCollisions(circle2));

        //test3: one object is fully contained (shape a)
        circle1 = new Circle2D(150, new Vector2D(292,626), new Vector2D());
        circle2 = new Circle2D(300, new Vector2D(182,532), new Vector2D());

        assertTrue(circle1.checkCollisions(circle2).shapeAContained);

        //test4: one object is fully contained (shape b)

        circle1 = new Circle2D(240, new Vector2D(232,222), new Vector2D());
        circle2 = new Circle2D(100, new Vector2D(320,291), new Vector2D());

        CollisionInfo info = circle1.checkCollisions(circle2);
        assertTrue(info.shapeBContained);

        //test5: move shape by distance and check if it is outside the object
        Circle2D shapeB = (Circle2D) info.shapeB;
        shapeB.position = shapeB.position.add(info.separationDistance.mul(1.000001));

        assertNull(circle1.checkCollisions(circle2));

        //test6: move shape A by opposite direction, for a different test
        circle1 = new Circle2D(150, new Vector2D(292,626), new Vector2D());
        circle2 = new Circle2D(300, new Vector2D(182,532), new Vector2D());

        info = circle1.checkCollisions(circle2);
        assertNotNull(info);

        Circle2D shapeA = (Circle2D) info.shapeA;
        shapeA.position = shapeA.position.add(info.separationDistance.getOpposite().mul(1.000001));

        assertNull(circle1.checkCollisions(circle2));
    }
}
