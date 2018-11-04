package algorithms.examples;

import algorithms.algorithms.helper.Counter;

public class SquareRootByIterationCount {

    private Counter counter;
    private int iterationCount;

    public SquareRootByIterationCount(int iterationCount) {
        this.counter = new Counter();
        this.iterationCount = iterationCount;
    }

    public double calculate(double n) {
        // 0 is the left bound and x is the right bound
        return calculate(n, 0, n);
    }

    private double calculate(final double n, double left, double right) {
        // Calculate the mid of left and right
        double mid = (left + right) / 2;
        // Square the mid
        double squaredMid = mid * mid;

        if (this.counter.value() == this.iterationCount) {
            return mid;
        }
        this.counter.increment();

        if (squaredMid < n) {
            //Mid is too small (search further in the mid of mid and right)
            return this.calculate(n, mid, right);

        } else if (squaredMid > n) {
            //Mid is too small (search further in the mid of left and mid)
            return this.calculate(n, left, mid);
        }

        //Mid is exactly the square root of originalValue
        return mid;
    }
}
