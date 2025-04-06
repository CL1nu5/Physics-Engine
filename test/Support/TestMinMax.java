package Support;

import com.google.common.collect.Lists;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

public class TestMinMax extends TestCase {

    /* setup */

    public TestMinMax(String testName) {
        super(testName);
    }

    public static TestSuite suite() {
        return new TestSuite(TestMinMax.class);
    }

    /* Tests */

    //tests if the class correctly identifies the min and max value of an ArrayList
    public void testArrayListConstructor() {
        //get arrayList with double values
        ArrayList<Double> list = Lists.newArrayList(
                23.45, -167.89, 3.14, -56.78, 290.12,
                -0.56, 478.90, -12.34, 7.08, -390.12,
                134.56, -8.90, 0.34, -256.78, 90.12,
                -434.56, 1078.90, -2.34, 856.78, -0.12
        );

        MinMax minMax = new MinMax(list);

        //min needs to be smaller/equal than max
        assertTrue(minMax.min <= minMax.max);
    }
}
