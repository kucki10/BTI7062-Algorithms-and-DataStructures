package algorithms.examples;

public class SquareRootByIterationCount {

    private int iterationCount;

    public SquareRootByIterationCount(int iterationCount) {
        iterationCount = iterationCount;
    }

    public double calculate(double x) {
        return calculate(x, x);
    }

    private double calculate(final double originalValue, double currentVal) {
        if (iterationCount <= 0) {
            return currentVal;
        }
        iterationCount--;

        double half = currentVal / 2;
        double squaredHalfs = half * half;

        if (squaredHalfs < originalValue) {
            //Half is still too large (half again)
            double diff = (currentVal + half) / 2;
            return calculate(diff);

        } else if (squaredHalfs > originalValue) {
            //Half is still too large (half again)
            return calculate(half);
        }

        //Half is exactly the square root of originalValue
        return half;
    }

}
