package PhysicsObjects2D;

import PhysicObjects2D.Vector2D;


import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TestVector2D extends TestCase {

    private static final Logger logger = LogManager.getLogger(TestVector2D.class);

    /* setup */

    public TestVector2D(String testName){
        super(testName);
    }

    public static TestSuite suite(){
        return new TestSuite(TestVector2D.class);
    }

    /* test */

    //tests if the object cloning works
    public void testClone() {
        //test setup
        Vector2D original = new Vector2D(3.5, -2.4);

        //cloning
        Vector2D clone = original.clone();

        //original and clone shouldn't be the same (!=)
        assertNotSame(original, clone);

        //but the still should be equal (hae the same values)
        assertEquals(original, clone);
    }

    //test if the equals function works
    public void testEquals() {
        //test setup
        Vector2D original = new Vector2D(3.5, -2.4);
        Vector2D equal = new Vector2D(3.5, -2.4);
        Vector2D xNotEqual = new Vector2D(1.4, -2.4);
        Vector2D yNotEqual = new Vector2D(3.5, 2.6);

        //testing equal
        assertNotSame(original, equal);
        assertEquals(original, equal);

        //testing xNotEqual
        assertNotSame(original, xNotEqual);
        assertFalse(original.equals(xNotEqual));

        //testing yNotEqual
        assertNotSame(original, yNotEqual);
        assertFalse(original.equals(yNotEqual));
    }

    //tests if the toString output matches the expectations
    public void testToString() {
        //test setup
        Vector2D vector = new Vector2D(2.3, 3.5);

        //test
        assertEquals("[x=2.3;y=3.5]", vector.toString());
    }

    //tests if to normalize outputs match the expectations
    public void testNormalize() {
        //test setup
        Vector2D case1 = new Vector2D(3, 4);
        Vector2D case2 = new Vector2D(3, 0);
        Vector2D case3 = new Vector2D(0, 0);

        //test1
        assertEquals(new Vector2D((double) 3 / 5, (double) 4 / 5), case1.normalize());

        //test2
        assertEquals(new Vector2D(1, 0), case2.normalize());

        //test3
        assertEquals(new Vector2D(0, 0), case3.normalize());
    }

    //tests if the Distance outputs match the expectations
    public void testGetDistance() {
        //test setup
        Vector2D case1 = new Vector2D(3, 4);
        Vector2D case2 = new Vector2D(1, 1);
        Vector2D case3 = new Vector2D(3, 0);

        //test1
        assertEquals(5.0, case1.getDistance());

        //test2
        assertEquals(Math.sqrt(2), case2.getDistance());

        //test 3
        assertEquals(3.0, case3.getDistance());
    }

}
