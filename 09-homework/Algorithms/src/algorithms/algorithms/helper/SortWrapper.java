package algorithms.algorithms.helper;

import java.util.Comparator;

public class SortWrapper {

    private Object[] data;
    private int left;
    private int right;
    private final Comparator comparator;

    public SortWrapper(Object[] data, Comparator comparator)
    {
        this(data, 0, data.length - 1, comparator);
    }

    public SortWrapper(Object[] data, int left, int right, Comparator comparator) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.comparator = comparator;
    }



    public Object[] getData() {
        return data;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getLength() {
        return data.length;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public void setLeft(int newLeft){
        this.left = newLeft;
    }

    public void setRight(int newRight) {
        this.right = newRight;
    }
}
