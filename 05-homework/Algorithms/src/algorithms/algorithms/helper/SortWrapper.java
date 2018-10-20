package algorithms.algorithms.helper;

import java.util.Comparator;

public class SortWrapper {

    private Object[] data;
    private Object[] aux;
    private int left;
    private int right;
    private final Comparator comparator;

    public SortWrapper(Object[] data, Object[] aux, int left, int right, Comparator comparator) {
        this.data = data;
        this.aux = aux;
        this.left = left;
        this.right = right;
        this.comparator = comparator;
    }


    public Object[] getData() {
        return data;
    }

    public Object[] getAux() {
        return aux;
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
}
