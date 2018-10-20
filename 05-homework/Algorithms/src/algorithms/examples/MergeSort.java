package algorithms.examples;

import algorithms.algorithms.helper.SortWrapper;
import algorithms.templates.DivideAndConquerable;

import java.util.ArrayList;
import java.util.List;

public class MergeSort implements DivideAndConquerable<SortWrapper> {

    private SortWrapper data;

    public MergeSort(SortWrapper data) {
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
        int middle = (this.data.getLeft() + this.data.getRight()) / 2;

        /*
        //FIRST try
        Object[] leftHalf = new Object[middle];
        System.arraycopy(this.data.getData(), 0, leftHalf, 0, middle);

        Object[] rightHalf = new Object[length - middle];
        System.arraycopy(this.data.getData(), middle, rightHalf, 0, length - middle);


        ArrayList<MergeSort> halfs = new ArrayList<>();
        halfs.add(new MergeSort(new SortWrapper(leftHalf, new Object[leftHalf.length], 0, leftHalf.length -1, this.data.getComparator())));
        halfs.add(new MergeSort(new SortWrapper(rightHalf, new Object[rightHalf.length], 0, rightHalf.length - 1, this.data.getComparator())));
        */

        ArrayList<MergeSort> halfs = new ArrayList<>();
        halfs.add(new MergeSort(new SortWrapper(this.data.getData(), this.data.getAux(), this.data.getLeft(), middle, this.data.getComparator())));
        halfs.add(new MergeSort(new SortWrapper(this.data.getData(), this.data.getAux(), middle + 1, this.data.getRight(), this.data.getComparator())));
        return halfs;
    }

    @Override
    public SortWrapper recombine(List<SortWrapper> intermediateResults) {
        SortWrapper leftPart = intermediateResults.get(0);
        SortWrapper rightPart = intermediateResults.get(1);

        int i = leftPart.getLeft();
        int j = rightPart.getLeft();
        int k = leftPart.getLeft();

        while (i <= leftPart.getRight() && j <= rightPart.getRight()) {
            if (this.data.getComparator().compare(this.data.getData()[i], this.data.getData()[j]) < 0) {
                this.data.getAux()[k++] = this.data.getData()[i++];
            } else {
                this.data.getAux()[k++] = this.data.getData()[j++];
            }
        }

        //Copy leftovers into Aux
        System.arraycopy(this.data.getData(), i, this.data.getAux(), k, leftPart.getRight() - i + 1);

        //Copy sorted sub parts back to Data
        System.arraycopy(this.data.getAux(), leftPart.getLeft(), this.data.getData(), leftPart.getLeft(), j-leftPart.getLeft());

        return this.data;
    }
}
