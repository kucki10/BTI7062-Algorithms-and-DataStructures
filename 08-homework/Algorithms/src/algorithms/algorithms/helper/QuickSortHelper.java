package algorithms.algorithms.helper;

import java.util.Comparator;

public class QuickSortHelper {

    public static void swap(SortWrapper data, int first, int second) {

        Object cache = data.getData()[first];
        data.getData()[first] = data.getData()[second];
        data.getData()[second] = cache;
    }

    public static int getMedianOfThree(SortWrapper data) {
        int left = data.getLeft();
        int right = data.getRight();
        Comparator sorter = data.getComparator();

        if (right - left + 1 >= 3) {
            int mid = (left + right)/2;
            Object leftObject = data.getData()[left];
            Object midObject = data.getData()[mid];
            Object rightObject = data.getData()[right];
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

    public static int partition(SortWrapper data) {
        int left = data.getLeft();
        int right = data.getRight();
        Comparator sorter = data.getComparator();

        Object pivot = data.getData()[right];
        int i = left;
        int j = right;
        while(i<j) {
            while (i<j && sorter.compare(data.getData()[i], pivot) < 0) {
                i++;
            }
            while (j>i && sorter.compare(data.getData()[j], pivot) >= 0) {
                j--;
            }
            if (i<j) {
                swap(data, i, j);
            }
        }
        swap(data, i, right);
        return i;
    }
}
