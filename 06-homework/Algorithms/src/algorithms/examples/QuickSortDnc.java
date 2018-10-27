package algorithms.examples;

import algorithms.algorithms.helper.QuickSortHelper;
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
        int median = QuickSortHelper.getMedianOfThree(this.data);
        QuickSortHelper.swap(this.data, median, this.data.getRight());
        int mid = QuickSortHelper.partition(this.data);
        ArrayList<QuickSortDnc> halfs = new ArrayList<>();
        halfs.add(new QuickSortDnc(new SortWrapper(this.data.getData(), this.data.getLeft(), mid - 1, this.data.getComparator())));
        halfs.add(new QuickSortDnc(new SortWrapper(this.data.getData(), mid + 1, this.data.getRight(), this.data.getComparator())));

        return halfs;
    }

    @Override
    public SortWrapper recombine(List<SortWrapper> intermediateResults) {
        return this.data;
    }
}
