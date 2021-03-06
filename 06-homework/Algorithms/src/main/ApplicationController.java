package main;

import algorithms.algorithms.helper.ConsoleWriteWrapper;
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
    private CheckBox cbEnableLogs;

	private int verificationCount = 20;

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
        lineChart.getXAxis().setLabel("Array size of unsorted array - Result is logged in console");
        lineChart.getYAxis().setLabel("time used to sort in [ns]");

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
            System.err.println("Either one checkbox for techniques must be enabled");
            return;
        }

        ConsoleWriteWrapper consoleWriter = new ConsoleWriteWrapper(cbEnableLogs.isSelected());

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                //Threaded stuff
                Comparator comparator = new IntComparator();

                for (int i = 10000; i <= 40000 ; i += 2000) {
                    consoleWriter.log(String.format("New Sort run with %d elements\n", i));

                    Object[] unsortedData = getRandomIntegerArray(i);

                    if (isStandardEnabled) {
                        standardQuickSort(unsortedData, comparator, consoleWriter);
                    }

                    if (isMultiThreadedEnabled) {
                        multiThreadedQuickSort(unsortedData, comparator, consoleWriter);
                    }

                    if (isInsertionSortAsBaseEnabled) {
                        insertionSortAsBaseQuickSort(unsortedData, comparator, consoleWriter);
                    }

                    consoleWriter.log(String.format("\nSort run with %d elements finished!\n\n", i));
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
	
    private void updateSeries(long n, long time, XYChart.Series<String, Long> series) {
        Platform.runLater(() -> {
            XYChart.Data<String, Long> newPoint = new XYChart.Data<>(Long.toString(n), time);
            series.getData().add(newPoint);
        });
    }

	private void standardQuickSort(Object[] data, Comparator sorter, ConsoleWriteWrapper consoleWriter) {
        long[] times = new long[verificationCount];

        for (int i = 0; i < verificationCount; i++) {

            Object[] unsortedData = new Object[data.length];
            System.arraycopy(data, 0, unsortedData, 0, data.length);
            consoleWriter.log(" Unsorted data \n " + Arrays.toString(unsortedData));

            QuickSortDnc sort = new QuickSortDnc(new SortWrapper(unsortedData, sorter));
            ExecutionTimer<SortWrapper> timer = new ExecutionTimer<>(sort::divideAndConquer);

            SortWrapper result = timer.result;
            consoleWriter.log(" Sorted data (took " + timer.time + "ns) \n " + Arrays.toString(result.getData()));

            try {
                verifyOrder(result.getData(), sorter);
            } catch (Exception ex) {
                consoleWriter.error(String.format("Standard  Algorithm failed on sorting with %d elements!\n%s", unsortedData.length, ex.getMessage()));
            }

            times[i] = timer.time;
        }

        long avgTime = (Arrays.stream(times).sum() / times.length);
        updateSeries(data.length, avgTime, standardSeries);

        consoleWriter.log(" QuickSort data sorting (took in average " + avgTime + "ns) ");
    }

    private void multiThreadedQuickSort(Object[] data, Comparator sorter, ConsoleWriteWrapper consoleWriter) {
	    long[] times = new long[verificationCount];

	    for (int i = 0; i < verificationCount; i++) {

            Object[] unsortedData = new Object[data.length];
            System.arraycopy(data, 0, unsortedData, 0, data.length);
            consoleWriter.log(" Unsorted data \n " + Arrays.toString(unsortedData));

            ParallelisedQuickSortDnc sort = new ParallelisedQuickSortDnc(new SortWrapper(unsortedData, sorter));
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 30, TimeUnit.SECONDS, new SynchronousQueue<>());

            ExecutionTimer<SortWrapper> timer = new ExecutionTimer<>(() -> sort.divideAndConquer(threadPoolExecutor));

            SortWrapper result = timer.result;
            consoleWriter.log(" Sorted data (took " + timer.time + "ns) \n " + Arrays.toString(result.getData()));

            try {
                verifyOrder(result.getData(), sorter);
            } catch (Exception ex) {
                consoleWriter.error(String.format(" Multithreaded Algorithm failed on sorting with %d elements!\n%s", unsortedData.length, ex.getMessage()));
            }

            times[i] = timer.time;
        }

        long avgTime = (Arrays.stream(times).sum() / times.length);
        updateSeries(data.length, avgTime, threadedSeries);

        consoleWriter.log(" Multithreaded data sorting (took in average " + avgTime + "ns) ");
    }

    private void insertionSortAsBaseQuickSort(Object[] data, Comparator sorter, ConsoleWriteWrapper consoleWriter) {
        long[] times = new long[verificationCount];

        for (int i = 0; i < verificationCount; i++) {

            Object[] unsortedData = new Object[data.length];
            System.arraycopy(data, 0, unsortedData, 0, data.length);
            consoleWriter.log(" Unsorted data \n " + Arrays.toString(unsortedData));

            QuickSortWithBaseInsertionSortDnc sort = new QuickSortWithBaseInsertionSortDnc(new SortWrapper(unsortedData, sorter));
            ExecutionTimer<SortWrapper> timer = new ExecutionTimer<>(sort::divideAndConquer);

            SortWrapper result = timer.result;
            consoleWriter.log(" Sorted data (took " + timer.time + "ns) \n " + Arrays.toString(result.getData()));

            try {
                verifyOrder(result.getData(), sorter);
            } catch (Exception ex) {
                consoleWriter.error(String.format(" QuickSortWithBaseInsertionSortDnc Algorithm failed on sorting with %d elements!\n%s", unsortedData.length, ex.getMessage()));
            }

            times[i] = timer.time;
        }

        long avgTime = (Arrays.stream(times).sum() / times.length);
        updateSeries(data.length, avgTime, insertionSortAsBaseSeries);

        consoleWriter.log(" BaseInsertionSort data sorting (took in average " + avgTime + "ns) ");
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
