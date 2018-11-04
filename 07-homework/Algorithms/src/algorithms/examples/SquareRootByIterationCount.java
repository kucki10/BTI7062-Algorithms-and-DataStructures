package algorithms.examples;

public class SquareRootByIterationCount {

    private int iterationCount;

    public SquareRootByIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public double calculate(double x) {
        return calculate(x, x);
    }

    private double calculate(final double originalValue, double currentVal) {

        System.out.println("\n  [" + iterationCount + "] (" + originalValue + ", " + currentVal + ")");

        double half = currentVal / 2;
        double squaredHalfs = half * half;

        System.out.println("     -> half         = " + half);
        System.out.println("     -> squared Half = " + squaredHalfs);

        if (iterationCount <= 0) {
            return currentVal;
        }
        iterationCount--;

        if (squaredHalfs < originalValue) {
            //Half is still too large (half again)
            //
            // TODO: FAIL Two steps at the time
            System.out.println("     == squaredHalfs < originalValue (" + squaredHalfs + " < " + originalValue + ")");

            double diff = (currentVal + half) / 2;
            return calculate(originalValue, diff);

        } else if (squaredHalfs > originalValue) {
            //Half is still too large (half again)
            System.out.println("     == squaredHalfs > originalValue (" + squaredHalfs + " > " + originalValue + ")");

            return calculate(originalValue, half);
        }

        //Half is exactly the square root of originalValue
        return half;
    }

}
