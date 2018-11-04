package algorithms.examples;

import algorithms.algorithms.helper.Counter;

public class CubicRootByPrecision {

    private Counter counter;
    private double delta;

    public CubicRootByPrecision(double delta) {
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
        double cubicMid = Math.pow(mid, 3);

        if (Math.abs(n - cubicMid) <= this.delta) {
            return mid;
        } else if (cubicMid < n) {
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
