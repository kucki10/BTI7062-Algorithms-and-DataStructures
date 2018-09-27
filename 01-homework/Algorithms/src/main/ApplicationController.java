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
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ApplicationController {

	@FXML
	private LineChart<String, Long> lineChart;

	@FXML
	private TextField txtNumber;

	@FXML
	private Button btnCalculate;

	
	private final XYChart.Series<String, Long> standardSeries;
	private final XYChart.Series<String, Long> concurrentSeries;
	private final XYChart.Series<String, Long> memoizedSeries;

	public ApplicationController() {
        standardSeries = new XYChart.Series<>();
        concurrentSeries = new XYChart.Series<>();
        memoizedSeries = new XYChart.Series<>();
	}
	
	@FXML
	private void initialize() {
        lineChart.getXAxis().setLabel("Desired number");
        lineChart.getYAxis().setLabel("time in [ns]");

		lineChart.getData().add(standardSeries);
		lineChart.getData().add(concurrentSeries);
		lineChart.getData().add(memoizedSeries);

		standardSeries.setName("Standard");
        memoizedSeries.setName("Memoized");
		concurrentSeries.setName("Threaded");

        txtNumber.setText("30");
	}
	
	@FXML
	private void onCalculate() {
		long n;
		try {
			n = Long.parseLong(txtNumber.getText());
		} catch(NumberFormatException exc) {
            System.err.println("Could not create number out of '" + txtNumber.getText() + "'");
            return;
		} 
		
		standardSeries.getData().clear();
        concurrentSeries.getData().clear();
		memoizedSeries.getData().clear();


        new Thread(new Task<Void>() {

            @Override
            protected Void call() {
                //Threaded stuff
                for (long i = 0; i <= n; i++) {
                    getFibonacciInStandardWay(i);
                    getFibonacciInMemoizedWay(i);
                    getFibonacciInParalellisedWay(i);
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
        System.out.println("Simple Fibonacci of " + n + " is: " + timer.result + " (took " + timer.time + " ns)");

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

        ExecutionTimer<Long> timer = new ExecutionTimer<>(() -> fib.divideAndConquer(new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new SynchronousQueue<>())));
        System.out.println("Threaded Fibonacci of " + n + " is: " + timer.result + " (took " + timer.time + " ns)");

        updateSeries(n, timer, concurrentSeries);
    }

}
