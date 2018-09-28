package main;

import algorithms.algorithms.helper.ExecutionTimer;
import algorithms.examples.DynamicallyFibonacciDnC;
import algorithms.examples.FibonacciDnC;
import algorithms.examples.ParallelisedFibonacciDnC;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ApplicationController {

	@FXML
	private LineChart<String, Long> lineChart;

	@FXML
	private TextField txtNumber;

	@FXML
	private Button btnCalculate;

	@FXML
    private CheckBox cbStandard;

	@FXML
    private CheckBox cbMemoized;

	@FXML
    private CheckBox cbParallelized;

	
	private final XYChart.Series<String, Long> standardSeries;
	private final XYChart.Series<String, Long> memoizedSeries;
    private final XYChart.Series<String, Long> parallelisedSeries;


	public ApplicationController() {
        standardSeries = new XYChart.Series<>();
        memoizedSeries = new XYChart.Series<>();
        parallelisedSeries = new XYChart.Series<>();
    }
	
	@FXML
	private void initialize() {
        lineChart.getXAxis().setLabel("Desired number Fib(n) - Result is logged in console");
        lineChart.getYAxis().setLabel("time in [ns]");

		lineChart.getData().add(standardSeries);
		lineChart.getData().add(memoizedSeries);
        lineChart.getData().add(parallelisedSeries);

		standardSeries.setName("Standard");
        memoizedSeries.setName("Memoized");
		parallelisedSeries.setName("Threaded");

		cbStandard.setSelected(true);
		cbMemoized.setSelected(true);
		cbParallelized.setSelected(true);

        txtNumber.setText("20");
	}
	
	@FXML
	private void onCalculate() {
	    //Clear the plot
        standardSeries.getData().clear();
        memoizedSeries.getData().clear();
        parallelisedSeries.getData().clear();

        //Read n from textField
		long n;
		try {
			n = Long.parseLong(txtNumber.getText());
		} catch(NumberFormatException exc) {
            System.err.println("Could not create number out of '" + txtNumber.getText() + "'");
            return;
		} 


		//Save the options, which calculation method should run
        //We need to save them, because they could change while calculating inside thread
        boolean isStandardEnabled = cbStandard.isSelected();
        boolean isMemoizedEnabled = cbMemoized.isSelected();
        boolean isParallelizedEnabled = cbParallelized.isSelected();


        //Check that at least one technique is enabled
        if (!isStandardEnabled && !isMemoizedEnabled && !isParallelizedEnabled) {
            System.err.println("Either one checkbox for Techniques must be enabled");
            return;
        }

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                //Threaded stuff
                for (long i = 0; i <= n; i++) {
                    if (isStandardEnabled) {
                        getFibonacciInStandardWay(i);
                    }

                    if (isMemoizedEnabled) {
                        getFibonacciInMemoizedWay(i);
                    }

                    if (isParallelizedEnabled) {
                        getFibonacciInParalellisedWay(i);
                    }
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

	private void getFibonacciInStandardWay(long n) {
        FibonacciDnC fib = new FibonacciDnC(n);

        ExecutionTimer<Long> timer = new ExecutionTimer<>(() -> fib.divideAndConquer());
        System.out.println("  Simple Fibonacci of " + n + " is: " + timer.result + " (took " + timer.time + " ns)");

        updateSeries(n, timer, standardSeries);
    }

    private void getFibonacciInMemoizedWay(long n) {
        Map<Integer, Long> cache = new HashMap<>();
        DynamicallyFibonacciDnC fib = new DynamicallyFibonacciDnC(n);

        ExecutionTimer<Long> timer = new ExecutionTimer<>(() -> fib.divideAndConquer(cache));
        System.out.println("Memoized Fibonacci of " + n + " is: " + timer.result + " (took " + timer.time + " ns)");

        updateSeries(n, timer, memoizedSeries);
    }

    private void getFibonacciInParalellisedWay(long n) {
        ParallelisedFibonacciDnC fib = new ParallelisedFibonacciDnC(n);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new SynchronousQueue<>());

        ExecutionTimer<Long> timer = new ExecutionTimer<>(() -> fib.divideAndConquer(threadPoolExecutor));
        System.out.println("Threaded Fibonacci of " + n + " is: " + timer.result + " (took " + timer.time + " ns)");

        updateSeries(n, timer, parallelisedSeries);
        //Shutdown thread pool, in order to stop pending threads
        threadPoolExecutor.shutdownNow();
    }

}
