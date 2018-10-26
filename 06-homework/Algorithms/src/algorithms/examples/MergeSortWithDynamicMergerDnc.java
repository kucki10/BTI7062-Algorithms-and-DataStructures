package algorithms.examples;

import algorithms.algorithms.helper.SortWrapper;
import algorithms.templates.DivideAndConquerable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MergeSortWithDynamicMergerDnc implements DivideAndConquerable<SortWrapper> {

    private SortWrapper data;
    private Consumer<SortWrapper> merger;

    public MergeSortWithDynamicMergerDnc(SortWrapper data, Consumer<SortWrapper> merger) {
        this.data = data;
        this.merger = merger;
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
        int middle = (this.data.getLeft() + this.data.getRight()) / 2;

        ArrayList<MergeSortWithDynamicMergerDnc> halfs = new ArrayList<>();
        halfs.add(new MergeSortWithDynamicMergerDnc(new SortWrapper(this.data.getData(), this.data.getAux(), this.data.getLeft(), middle, this.data.getComparator()), this.merger));
        halfs.add(new MergeSortWithDynamicMergerDnc(new SortWrapper(this.data.getData(), this.data.getAux(), middle + 1, this.data.getRight(), this.data.getComparator()), this.merger));

        return halfs;
    }

    @Override
    public SortWrapper recombine(List<SortWrapper> intermediateResults) {
        this.merger.accept(this.data);
        return this.data;
    }
}
