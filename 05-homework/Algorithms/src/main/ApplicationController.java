package main;

import algorithms.algorithms.helper.ExecutionTimer;
import algorithms.algorithms.helper.IntComparator;
import algorithms.algorithms.helper.SortWrapper;
import algorithms.examples.MergeSort;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

	
	private final XYChart.Series<String, Long> standardSeries;
	private final XYChart.Series<String, Long> threadedSeries;
    private final XYChart.Series<String, Long> insertionSortAsBaseSeries;


	public ApplicationController() {
        standardSeries = new XYChart.Series<>();
        threadedSeries = new XYChart.Series<>();
        insertionSortAsBaseSeries = new XYChart.Series<>();
    }
	
	@FXML
	private void initialize() {
        lineChart.getXAxis().setLabel("Desired number Fib(n) - Result is logged in console");
        lineChart.getYAxis().setLabel("time in [ns]");

		lineChart.getData().add(standardSeries);
		lineChart.getData().add(threadedSeries);
        lineChart.getData().add(insertionSortAsBaseSeries);

		standardSeries.setName("Standard");
        threadedSeries.setName("Multi-threaded");
        insertionSortAsBaseSeries.setName("InsertionSort as Base function");

		cbStandard.setSelected(true);
		cbMultiThreaded.setSelected(true);
		cbInsertionSortAsBase.setSelected(true);
	}
	
	@FXML
	private void onCalculate() {
	    //Clear the plot
        standardSeries.getData().clear();
        threadedSeries.getData().clear();
        insertionSortAsBaseSeries.getData().clear();


		//Save the options, which calculation method should run
        //We need to save them, because they could change while calculating inside thread
        boolean isStandardEnabled = cbStandard.isSelected();
        boolean isMultiThreadedEnabled = cbMultiThreaded.isSelected();
        boolean isInsertionSortAsBaseEnabled = cbInsertionSortAsBase.isSelected();


        //Check that at least one technique is enabled
        if (!isStandardEnabled && !isMultiThreadedEnabled && !isInsertionSortAsBaseEnabled) {
            System.err.println("Either one checkbox for Techniques must be enabled");
            return;
        }

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                //Threaded stuff
                    if (isStandardEnabled) {
                        standardMergeSort();
                    }

                    /*
                    if (isMemoizedEnabled) {
                        getFibonacciInMemoizedWay(i);
                    }

                    if (isParallelizedEnabled) {
                        getFibonacciInParalellisedWay(i);
                    }
                    */
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

	private void standardMergeSort() {
        Object[] data = getRandomIntegerArray(100);
        Comparator sorter = new IntComparator();

        System.out.println("Unsorted data  : " + Arrays.toString(data) + "");

        MergeSort sort = new MergeSort(new SortWrapper(data, new Object[data.length], 0, data.length - 1, sorter));
        SortWrapper result = sort.divideAndConquer();

        System.out.println("Sorted data    : " + Arrays.toString(result.getData()) + "");
        try {
            verifyOrder(result.getData(), sorter);
        } catch (Exception ex) {
            System.err.println(String.format("Algorithm failed!\n%s", ex.getMessage()));
        }
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
