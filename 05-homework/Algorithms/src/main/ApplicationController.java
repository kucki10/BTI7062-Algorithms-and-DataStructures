package main;

import algorithms.algorithms.helper.ExecutionTimer;
import algorithms.algorithms.helper.IntComparator;
import algorithms.algorithms.helper.SortWrapper;
import algorithms.examples.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ApplicationController {

	@FXML
	private LineChart<String, Long> lineChart;

	@FXML
	private Button btnCalculate;

	@FXML
    private CheckBox cbStandard;

	@FXML
    private CheckBox cbMultiThreaded;

	@FXML
    private CheckBox cbInsertionSortAsBase;

    @FXML
    private CheckBox cbMergeSortWithDynamicMerger;
	
	private final XYChart.Series<String, Long> standardSeries;
	private final XYChart.Series<String, Long> threadedSeries;
    private final XYChart.Series<String, Long> insertionSortAsBaseSeries;
    private final XYChart.Series<String, Long> mergeSortWithDynamicMergerSeries;


	public ApplicationController() {
        standardSeries = new XYChart.Series<>();
        threadedSeries = new XYChart.Series<>();
        insertionSortAsBaseSeries = new XYChart.Series<>();
        mergeSortWithDynamicMergerSeries = new XYChart.Series<>();
    }
	
	@FXML
	private void initialize() {
        lineChart.getXAxis().setLabel("Array size of unsorted array - Result is logged in console");
        lineChart.getYAxis().setLabel("time used to sort in [ns]");

		lineChart.getData().add(standardSeries);
		lineChart.getData().add(threadedSeries);
        lineChart.getData().add(insertionSortAsBaseSeries);
        lineChart.getData().add(mergeSortWithDynamicMergerSeries);

		standardSeries.setName("Standard");
        threadedSeries.setName("Multi-threaded");
        insertionSortAsBaseSeries.setName("InsertionSort as Base function");
        mergeSortWithDynamicMergerSeries.setName("MergeSort with dynamic merger");


		cbStandard.setSelected(true);
		cbMultiThreaded.setSelected(true);
		cbInsertionSortAsBase.setSelected(true);
		cbMergeSortWithDynamicMerger.setSelected(true);
	}
	
	@FXML
	private void onCalculate() {
	    //Clear the plot
        standardSeries.getData().clear();
        threadedSeries.getData().clear();
        insertionSortAsBaseSeries.getData().clear();
        mergeSortWithDynamicMergerSeries.getData().clear();


		//Save the options, which calculation method should run
        //We need to save them, because they could change while calculating inside thread
        boolean isStandardEnabled = cbStandard.isSelected();
        boolean isMultiThreadedEnabled = cbMultiThreaded.isSelected();
        boolean isInsertionSortAsBaseEnabled = cbInsertionSortAsBase.isSelected();
        boolean isMergeSortWithDynamicMergerEnabled = cbMergeSortWithDynamicMerger.isSelected();


        //Check that at least one technique is enabled
        if (!isStandardEnabled && !isMultiThreadedEnabled && !isInsertionSortAsBaseEnabled && !isMergeSortWithDynamicMergerEnabled) {
            System.err.println("Either one checkbox for techniques must be enabled");
            return;
        }

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                //Threaded stuff
                Comparator comparator = new IntComparator();

                for (int i = 20; i <= 200 ; i += 10) {

                    System.out.println(String.format("New Sort run with %d elements\n", i));
                    Object[] unsortedData = getRandomIntegerArray(i);

                    if (isStandardEnabled) {
                        Object[] unsortedDataForStandard = new Object[i];
                        System.arraycopy(unsortedData, 0, unsortedDataForStandard, 0, i);
                        standardMergeSort(unsortedDataForStandard, comparator);
                    }

                    if (isMultiThreadedEnabled) {
                        Object[] unsortedDataForMultiThreaded = new Object[i];
                        System.arraycopy(unsortedData, 0, unsortedDataForMultiThreaded, 0, i);
                        multiThreadedMergeSort(unsortedDataForMultiThreaded, comparator);
                    }

                    if (isInsertionSortAsBaseEnabled) {
                        Object[] unsortedDataForInsertionSortAsBase = new Object[i];
                        System.arraycopy(unsortedData, 0, unsortedDataForInsertionSortAsBase, 0, i);
                        insertionSortAsBaseMergeSort(unsortedDataForInsertionSortAsBase, comparator);
                    }

                    if (isMergeSortWithDynamicMergerEnabled) {
                        Object[] unsortedDataForDynamicMerger = new Object[i];
                        System.arraycopy(unsortedData, 0, unsortedDataForDynamicMerger, 0, i);
                        mergeSortWithDynamicMerger(unsortedDataForDynamicMerger, comparator);
                    }

                    System.out.println(String.format("\nSort run with %d elements finished!\n\n", i));

                }

                return null;
            }

            @Override
            protected void running() {
                //Thread safe UI operations in here
                btnCalculate.setDisable(true);
            }

            @Override
            protected void succeeded() {
                //Thread safe UI operations in here
                btnCalculate.setDisable(false);
            }
        }).start();
	}
	
    private void updateSeries(long n, ExecutionTimer timer, XYChart.Series<String, Long> series) {
        Platform.runLater(() -> {
            XYChart.Data<String, Long> newPoint = new XYChart.Data<>(Long.toString(n), timer.time);
            series.getData().add(newPoint);
        });
    }

	private void standardMergeSort(Object[] data, Comparator sorter) {
        System.out.println(" Unsorted data \n " + Arrays.toString(data));

        MergeSortDnc sort = new MergeSortDnc(new SortWrapper(data, sorter));
        ExecutionTimer<SortWrapper> timer = new ExecutionTimer<>(sort::divideAndConquer);

        SortWrapper result = timer.result;
        System.out.println(" Sorted data (took " + timer.time + "ns) \n " + Arrays.toString(result.getData()));

        try {
            verifyOrder(result.getData(), sorter);
        } catch (Exception ex) {
            System.err.println(String.format("Standard  Algorithm failed on sorting with %d elements!\n%s", data.length, ex.getMessage()));
        }

        updateSeries(data.length, timer, standardSeries);
    }

    private void multiThreadedMergeSort(Object[] data, Comparator sorter) {
        System.out.println(" Unsorted data \n " + Arrays.toString(data));

        ParallelisedMergeSortDnc sort = new ParallelisedMergeSortDnc(new SortWrapper(data, sorter));
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS, new SynchronousQueue<>());

        ExecutionTimer<SortWrapper> timer = new ExecutionTimer<>(() -> sort.divideAndConquer(threadPoolExecutor));

        SortWrapper result = timer.result;
        System.out.println(" Sorted data (took " + timer.time + "ns) \n " + Arrays.toString(result.getData()));

        try {
            verifyOrder(result.getData(), sorter);
        } catch (Exception ex) {
            System.err.println(String.format(" Multithreaded Algorithm failed on sorting with %d elements!\n%s", data.length, ex.getMessage()));
        }

        updateSeries(data.length, timer, threadedSeries);
    }

    private void insertionSortAsBaseMergeSort(Object[] data, Comparator sorter) {
        System.out.println(" Unsorted data \n " + Arrays.toString(data));

        MergeSortWithBaseInsertionSortDnc sort = new MergeSortWithBaseInsertionSortDnc(new SortWrapper(data, sorter));
        ExecutionTimer<SortWrapper> timer = new ExecutionTimer<>(sort::divideAndConquer);

        SortWrapper result = timer.result;
        System.out.println(" Sorted data (took " + timer.time + "ns) \n " + Arrays.toString(result.getData()));

        try {
            verifyOrder(result.getData(), sorter);
        } catch (Exception ex) {
            System.err.println(String.format(" MergeSortWithBaseInsertionSort Algorithm failed on sorting with %d elements!\n%s", data.length, ex.getMessage()));
        }

        updateSeries(data.length, timer, insertionSortAsBaseSeries);
    }

        private void mergeSortWithDynamicMerger(Object[] data, Comparator sorter) {
        System.out.println(" Unsorted data \n " + Arrays.toString(data));

        InsertionSort merger = new InsertionSort();
        MergeSortWithDynamicMergerDnc sort = new MergeSortWithDynamicMergerDnc(new SortWrapper(data, sorter), merger::sort);
        ExecutionTimer<SortWrapper> timer = new ExecutionTimer<>(sort::divideAndConquer);

        SortWrapper result = timer.result;
        System.out.println(" Sorted data (took " + timer.time + "ns) \n " + Arrays.toString(result.getData()));

        try {
            verifyOrder(result.getData(), sorter);
        } catch (Exception ex) {
            System.err.println(String.format(" MergeSortWithDynamicMerger Algorithm failed on sorting with %d elements!\n%s", data.length, ex.getMessage()));
        }

        updateSeries(data.length, timer, mergeSortWithDynamicMergerSeries);
    }


    private Object[] getRandomIntegerArray(int arraySize) {
	    Object[] data = new Object[arraySize];
	    Random rand = new Random();

	    for (int i = 0; i < arraySize; i++) {
	        data[i] = rand.nextInt(1000);
        }

	    return data;
    }


    private void verifyOrder(Object[] sorted, Comparator comparator) throws Exception {
	    for (int i = 0; i < sorted.length - 1; i++) {
	        if (comparator.compare(sorted[i], sorted[i+1]) > 0) {
	            String msg = String.format("This array is not sorted at postion [%d] '%s' AND [%d] '{%s}' \n{%s}", i, sorted[i], i+1, sorted[i+1], Arrays.toString(sorted));
	            throw new Exception(msg);
            }
        }
    }
}
