package main;

import algorithms.algorithms.helper.ConsoleWriteWrapper;
import algorithms.algorithms.helper.ExecutionTimer;
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

public class ApplicationController {

	@FXML
	private LineChart<String, Integer> lineChart;

	@FXML
	private Button btnCalculate;

	@FXML
    private CheckBox cbSquareRoot;

    @FXML
    private CheckBox cbCubicRoot;

    @FXML
    private CheckBox cbLogarithm2;

    @FXML
    private CheckBox cbLogarithm10;

	@FXML
    private CheckBox cbEnableLogs;

	private final XYChart.Series<String, Integer> squareRoot;
    private final XYChart.Series<String, Integer> cubicRoot;
    private final XYChart.Series<String, Integer> logarithm2;
    private final XYChart.Series<String, Integer> logarithm10;

	public ApplicationController() {
        squareRoot = new XYChart.Series<>();
        cubicRoot = new XYChart.Series<>();
        logarithm2 = new XYChart.Series<>();
        logarithm10 = new XYChart.Series<>();
    }
	
	@FXML
	private void initialize() {
        lineChart.getXAxis().setLabel("Number to be calculated - Result is logged in console");
        lineChart.getYAxis().setLabel("Iteration count");

		lineChart.getData().add(squareRoot);
		lineChart.getData().add(cubicRoot);
		lineChart.getData().add(logarithm2);
        lineChart.getData().add(logarithm10);

        squareRoot.setName("Square Root");
        cubicRoot.setName("Cubic Root");
        logarithm2.setName("Logarithm 2");
        logarithm10.setName("Logarithm 10");

        cbSquareRoot.setSelected(true);
        cbCubicRoot.setSelected(true);
        cbLogarithm2.setSelected(true);
        cbLogarithm10.setSelected(true);
	}
	
	@FXML
	private void onCalculate() {
	    //Clear the plot
        squareRoot.getData().clear();

		//Save the options, which calculation method should run
        //We need to save them, because they could change while calculating inside thread
        boolean isSquareRootEnabled = cbSquareRoot.isSelected();
        boolean isCubicRootEnabled = cbCubicRoot.isSelected();
        boolean isLogarithm2Enabled = cbLogarithm2.isSelected();
        boolean isLogarithm10Enabled = cbLogarithm10.isSelected();

        //Check that at least one technique is enabled
        if (!isSquareRootEnabled && !isCubicRootEnabled && !isLogarithm2Enabled && !isLogarithm10Enabled) {
            System.err.println("Either one checkbox for techniques must be enabled");
            return;
        }

        ConsoleWriteWrapper consoleWriter = new ConsoleWriteWrapper(cbEnableLogs.isSelected());

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                //Threaded stuff

                double delta = 0.000000001;

                for (int i = 10000; i <= 1000000 ; i += 10000) {
                    if (isSquareRootEnabled) {
                        calculateSquareRootByPrecision(i, delta, consoleWriter);
                    }

                    if (isCubicRootEnabled) {
                        calculateCubicRootByPrecision(i, delta, consoleWriter);
                    }

                    if (isLogarithm2Enabled) {
                        calculateLogarithm2ByPrecision(i, delta, consoleWriter);
                    }

                    if (isLogarithm10Enabled) {
                        calculateLogarithm10ByPrecision(i, delta, consoleWriter);
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
	
    private void updateSeries(int n, int iterations, XYChart.Series<String, Integer> series) {
        Platform.runLater(() -> {
            XYChart.Data<String, Integer> newPoint = new XYChart.Data<>(Integer.toString(n), iterations);
            series.getData().add(newPoint);
        });
    }

    private void calculateSquareRootByPrecision(int n, double delta, ConsoleWriteWrapper consoleWrapper) {
        SquareRootByPrecision sqrt = new SquareRootByPrecision(delta);
        double calcRes = sqrt.calculate(n);
        consoleWrapper.log(" Square to be calculated : " + n);
        consoleWrapper.log(" Iteration count : " + sqrt.getIterationCount());
        consoleWrapper.log(" Calculated value is : " + calcRes);

        double realRes = Math.sqrt(n);
        consoleWrapper.log(" Real value is : " +  realRes);

        double diff = Math.abs(realRes - calcRes);
        consoleWrapper.log(" Diff is : " + diff + "\n");
        updateSeries(n, sqrt.getIterationCount(), squareRoot);
    }

    private void calculateCubicRootByPrecision(int n, double delta, ConsoleWriteWrapper consoleWrapper) {
        CubicRootByPrecision cbrt = new CubicRootByPrecision(delta);
        double calcRes = cbrt.calculate(n);
        consoleWrapper.log(" Cubic to be calculated : " + n);
        consoleWrapper.log(" Iteration count : " + cbrt.getIterationCount());
        consoleWrapper.log(" Calculated value is : " + calcRes);

        double realRes = Math.cbrt(n);
        consoleWrapper.log(" Real value is : " +  realRes);

        double diff = Math.abs(realRes - calcRes);
        consoleWrapper.log(" Diff is : " + diff + "\n");
        updateSeries(n, cbrt.getIterationCount(), cubicRoot);
    }

    private void calculateLogarithm2ByPrecision(int n, double delta, ConsoleWriteWrapper consoleWrapper) {
        Logarithm2ByPrecision log2 = new Logarithm2ByPrecision(delta);
        double calcRes = log2.calculate(n);
        consoleWrapper.log(" Logarithm 2 to be calculated : " + n);
        consoleWrapper.log(" Iteration count : " + log2.getIterationCount());
        consoleWrapper.log(" Calculated value is : " + calcRes);

        double realRes = Math.log(n)/Math.log(2);
        consoleWrapper.log(" Real value is : " +  realRes);

        double diff = Math.abs(realRes - calcRes);
        consoleWrapper.log(" Diff is : " + diff + "\n");
        updateSeries(n, log2.getIterationCount(), logarithm2);
    }

    private void calculateLogarithm10ByPrecision(int n, double delta, ConsoleWriteWrapper consoleWrapper) {
        Logarithm10ByPrecision log10 = new Logarithm10ByPrecision(delta);
        double calcRes = log10.calculate(n);
        consoleWrapper.log(" Logarithm 10 to be calculated : " + n);
        consoleWrapper.log(" Iteration count : " + log10.getIterationCount());
        consoleWrapper.log(" Calculated value is : " + calcRes);

        double realRes = Math.log(n)/Math.log(10);
        consoleWrapper.log(" Real value is : " +  realRes);

        double diff = Math.abs(realRes - calcRes);
        consoleWrapper.log(" Diff is : " + diff + "\n");
        updateSeries(n, log10.getIterationCount(), logarithm10);
    }
}
