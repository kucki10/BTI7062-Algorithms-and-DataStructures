package algorithms.examples;

import algorithms.algorithms.helper.Counter;

public class SquareRootByPrecision {

    private Counter counter;
    private double delta;

    public SquareRootByPrecision(double delta) {
        this.counter = new Counter();
        this.delta = delta;
    }

    public double calculate(double n) {
        // 0 is the left bound and x is the right bound
        return calculate(n, 0, n);
    }

    private double calculate(final double n, double left, double right) {
        //Increment Counter
        this.counter.increment();
        // Calculate the mid of left and right
        double mid = (left + right) / 2;
        // Square the mid
        double squaredMid = mid * mid;

        if (Math.abs(n - squaredMid) <= this.delta) {
            return mid;
        } else if (squaredMid < n) {
            //Mid is too small (search further in the mid of mid and right)
            return this.calculate(n, mid, right);
        } else {
            //Mid is too small (search further in the mid of left and mid)
            return this.calculate(n, left, mid);
        }
    }

    public int getIterationCount() {
        return this.counter.value();
    }
}
