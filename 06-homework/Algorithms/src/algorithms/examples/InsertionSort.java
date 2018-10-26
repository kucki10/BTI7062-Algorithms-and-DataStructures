package algorithms.examples;

import algorithms.algorithms.helper.SortWrapper;

public class InsertionSort {

    public void sort(SortWrapper data) {
        int left = data.getLeft();
        int right = data.getRight();
        if(left < right) {

            int size = right - left + 1;

            for(int j = 1; j < size; j++) {

                Object value = data.getData()[left + j];

                int i = j - 1;

                while(i >= 0 && data.getComparator().compare(data.getData()[left + i], value) > 0) {
                    data.getData()[left + i + 1] = data.getData()[left + i];
                    i--;
                }

                data.getData()[left + i + 1] = value;
            }

        }
    }

}
