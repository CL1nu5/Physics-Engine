package Support;

import com.google.common.primitives.Doubles;

import java.util.ArrayList;

public class MinMax {

    /* values */

    public double min, max;

    /* constructors */

    //sets the min and max value
    public MinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    //sets the min and max value based on a double array of values
    public MinMax(double[] array) {
        //array needs to be longer than 1
        if (array == null || array.length == 0) {
            throw new RuntimeException("Array is null or length is 0!");
        }

        //setting start values of min and max
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;

        //looping though all values and applying min and max
        for(double value : array) {
            if (value > max) {
                max = value;
            }
            if (value < min) {
                min = value;
            }
        }
    }

    //sets the min and max value based on a double arrayList of values
    public MinMax(ArrayList<Double> arrayList) {
        this(Doubles.toArray(arrayList));
    }

    /* basic methods */

    public String toString() {
        return "[min:" + min + ";max:" + max + "]";
    }
}
