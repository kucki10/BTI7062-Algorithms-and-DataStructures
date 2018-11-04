package algorithms.algorithms.helper;

public class Counter {

    private int count;

    public Counter() {
        this.count = 0;
    }

    public void increment() {
        this.count++;
    }

    public  int value() {
        return this.count;
    }
}
