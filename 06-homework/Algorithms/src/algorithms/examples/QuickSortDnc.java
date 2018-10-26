package algorithms.examples;

import algorithms.algorithms.helper.SortWrapper;
import algorithms.templates.DivideAndConquerable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuickSortDnc implements DivideAndConquerable<SortWrapper> {

    private SortWrapper data;

    public QuickSortDnc(SortWrapper data) {
        this.data = data;
    }

    @Override
    public boolean isBasic() {
        return this.data.getRight() - this.data.getLeft() < 1;
    }

    @Override
    public SortWrapper baseFun() {
        return this.data;
    }

    @Override
    public List<? extends DivideAndConquerable<SortWrapper>> decompose() {
        swap(getMedianOfThree(this.data.getLeft(), this.data.getRight(), this.data.getComparator()), this.data.getRight());
        int mid = partition(this.data.getLeft(), this.data.getRight(), this.data.getComparator());
        ArrayList<QuickSortDnc> halfs = new ArrayList<>();
        halfs.add(new QuickSortDnc(new SortWrapper(this.data.getData(), this.data.getLeft(), mid - 1, this.data.getComparator())));
        halfs.add(new QuickSortDnc(new SortWrapper(this.data.getData(), mid + 1, this.data.getRight(), this.data.getComparator())));

        return halfs;
    }

    @Override
    public SortWrapper recombine(List<SortWrapper> intermediateResults) {
        //SortWrapper leftPart = intermediateResults.get(0);
        //SortWrapper rightPart = intermediateResults.get(1);
        //Object pivot = this.data.getRight();
        //for (int i = rightPart.getLength(); i > 0; i--) {
        //    rightPart.getData()[rightPart.getRight()+i-1] = rightPart.getData()[rightPart.getRight()+i];
        //}
        return this.data;
    }

    private void swap(int first, int second) {
        Object cache = this.data.getData()[first];
        this.data.getData()[first] = this.data.getData()[second];
        this.data.getData()[second] = cache;
    }

    private int getMedianOfThree(int left, int right, Comparator sorter) {
        if (right - left + 1 >= 3) {
            int mid = (left + right)/2;
            Object leftObject = this.data.getData()[left];
            Object midObject = this.data.getData()[mid];
            Object rightObject = this.data.getData()[right];
            if (sorter.compare(leftObject, midObject) <= 0) {
                if (sorter.compare(midObject, rightObject) <= 0) {
                    return mid;
                } else if (sorter.compare(rightObject, leftObject) <= 0) {
                    return left;
                } else if (sorter.compare(midObject, rightObject) > 0) {
                    return mid;
                }
            }
        }
        return right;
    }

    private int partition(int left, int right, Comparator sorter) {
        Object pivot = this.data.getData()[right];
        int i = left;
        int j = right;
        while(i<j) {
            while (i<j && sorter.compare(this.data.getData()[i], pivot) < 0) {
                i++;
            }
            while (j>i && sorter.compare(this.data.getData()[j], pivot) >= 0) {
                j--;
            }
            if (i<j) {
                swap(i,j);
            }
        }
        swap(i, right);
        return i;
    }
}
