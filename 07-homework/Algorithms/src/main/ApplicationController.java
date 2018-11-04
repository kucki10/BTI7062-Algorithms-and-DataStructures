package main;

import algorithms.algorithms.helper.ConsoleWriteWrapper;
import algorithms.algorithms.helper.ExecutionTimer;
import algorithms.examples.SquareRootByIterationCount;
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

                for (int i = 2; i <= 100 ; i += 1) {
                    consoleWriter.log(String.format("New Sort run with %d elements\n", i));


                    calculateSquareRootByIterationCount(i, consoleWriter);

                    /*
                    if (isStandardEnabled) {
                        standardQuickSort(unsortedData, comparator, consoleWriter);
                    }

                    if (isMultiThreadedEnabled) {
                        multiThreadedQuickSort(unsortedData, comparator, consoleWriter);
                    }

                    if (isInsertionSortAsBaseEnabled) {
                        insertionSortAsBaseQuickSort(unsortedData, comparator, consoleWriter);
                    }
                    */

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


    private void calculateSquareRootByIterationCount(int n, ConsoleWriteWrapper consoleWrapper) {
        SquareRootByIterationCount sgrt = new SquareRootByIterationCount(50);
        double calcRes = sgrt.calculate(n);

        consoleWrapper.log(" Calculated value is : " + calcRes);

        double realRes = Math.sqrt(n);
        consoleWrapper.log(" Real value is : " +  realRes);

        double diff = (calcRes + realRes) / 2;
        consoleWrapper.log(" Diff is : " + diff + "\n");
    }

    /*
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
    */

}
