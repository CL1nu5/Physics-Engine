package PhysicsObjects2D;

import PhysicObjects2D.Vector2D;


import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;

public class TestVector2D extends TestCase {

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

    //tests cloning an array of vectors
    public void testCloningMultiple() {
        //test setup
        Vector2D[] vectors = {new Vector2D(2,5), new Vector2D(-1.4, 1.3), new Vector2D(100.32, -129836.3)};

        //cloning
        Vector2D[] clone = Arrays.stream(vectors).map(Vector2D::clone).toArray(Vector2D[]::new);

        //test array not being the same reference
        assertNotSame(vectors, clone);

        //test values not being the same reference, but still having the same value
        for (int i = 0; i < vectors.length; i++){
            assertNotSame(vectors[i], clone[i]);
            assertEquals(vectors[i], clone[i]);
        }
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

    //test the addition method
    public void testAdd() {
        //test setup
        Vector2D vector1 = new Vector2D(3, 5);
        Vector2D vector2 = new Vector2D(2, 1);

        //test
        assertEquals(new Vector2D(5, 6), vector1.add(vector2));
    }

    //test the subtraction method
    public void testSub() {
        //test setup
        Vector2D vector1 = new Vector2D(3, 5);
        Vector2D vector2 = new Vector2D(2, 1);

        //test1
        assertEquals(new Vector2D(1, 4), vector1.sub(vector2));

        //test2 (other order)
        assertEquals(new Vector2D(-1, -4), vector2.sub(vector1));
    }
}
