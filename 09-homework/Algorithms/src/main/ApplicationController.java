package main;

import algorithms.algorithms.helper.ConsoleWriteWrapper;
import algorithms.algorithms.helper.ExecutionTimer;
import algorithms.examples.*;
import algorithms.templates.Matrix;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.util.*;

public class ApplicationController {

	@FXML
	private LineChart<String, Long> lineChart;

	@FXML
	private Button btnCalculate;

	@FXML
    private CheckBox cbColumnBased;

	@FXML
    private CheckBox cbRowBased;

	@FXML
    private CheckBox cbEnableLogs;

	private int verificationCount = 20;

	private final XYChart.Series<String, Long> columnBasedSeries;
	private final XYChart.Series<String, Long> rowBasedSeries;

    private final XYChart.Series<String, Long> logarithmicSeries;
    private final XYChart.Series<String, Long> linearSeries;
	private final XYChart.Series<String, Long> quadraticSeries;
	private final XYChart.Series<String, Long> qubicSeries;

	public ApplicationController() {
        columnBasedSeries = new XYChart.Series<>();
        rowBasedSeries = new XYChart.Series<>();

        logarithmicSeries  = new XYChart.Series<>();
        linearSeries= new XYChart.Series<>();
        quadraticSeries = new XYChart.Series<>();
        qubicSeries = new XYChart.Series<>();
    }
	
	@FXML
	private void initialize() {
        lineChart.getXAxis().setLabel("Fibonacci (x) -> x -> Result is logged in console");
        lineChart.getYAxis().setLabel("time used to calculate in [ns]");

		lineChart.getData().add(columnBasedSeries);
		lineChart.getData().add(rowBasedSeries);

		lineChart.getData().add(logarithmicSeries);
		lineChart.getData().add(linearSeries);
		lineChart.getData().add(quadraticSeries);
		lineChart.getData().add(qubicSeries);


		columnBasedSeries.setName("Fibonacci Matrix (column based)");
        rowBasedSeries.setName("Fibonacci Matrix (row based)");

        logarithmicSeries.setName("Reference Logarithmic");
        linearSeries.setName("Reference Linear");
        quadraticSeries.setName("Reference Quadratic");
        qubicSeries.setName("Reference Qubic");


		cbColumnBased.setSelected(true);
		cbRowBased.setSelected(true);
	}
	
	@FXML
	private void onCalculate() {
	    //Clear the plot
        columnBasedSeries.getData().clear();
        rowBasedSeries.getData().clear();

		//Save the options, which calculation method should run
        //We need to save them, because they could change while calculating inside thread
        boolean isColumnBasedEnabled = cbColumnBased.isSelected();
        boolean isRowBasedEnabled = cbRowBased.isSelected();

        //Check that at least one technique is enabled
        if (!isColumnBasedEnabled && !isRowBasedEnabled) {
            System.err.println("Either one checkbox for techniques must be enabled");
            return;
        }

        ConsoleWriteWrapper consoleWriter = new ConsoleWriteWrapper(cbEnableLogs.isSelected());

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                //Threaded stuff
                for (int i = 1; i <= 10000 ; i += 1) {
                    consoleWriter.log(String.format("New Calculation run with Fibonacci(%d)\n", i));

                    if (isColumnBasedEnabled) {
                        columnBasedFibonacciMatrix(i, consoleWriter);
                    }

                    if (isColumnBasedEnabled) {
                        rowBasedFibonacciMatrix(i, consoleWriter);
                    }

                    drawLogarithmicLine(i);
                    drawLinearLine(i);
                    drawQuadraticLine(i);
                    drawQubicLine(i);

                    consoleWriter.log(String.format("\nCalculation run with Fibonacci(%d) finished!\n\n", i));
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

	private void columnBasedFibonacciMatrix(int j, ConsoleWriteWrapper consoleWriter) {
        long[] times = new long[verificationCount];

        for (int i = 0; i < verificationCount; i++) {

            Matrix columnBasedMatrix = new ColumnBasedDoubleMatrix(2, 2);
            consoleWriter.log(" Fibonacci before calculation \n " + columnBasedMatrix.toString());

            ExecutionTimer<Matrix> timer = new ExecutionTimer<>(() -> columnBasedMatrix.potentiate(j));

            Matrix result = timer.result;
            consoleWriter.log(" Calcualting Fibonnaci [column] (took " + timer.time + "ns) \n " + result.toString());
            times[i] = timer.time;
        }

        long avgTime = (Arrays.stream(times).sum() / times.length);
        updateSeries(j, avgTime, columnBasedSeries);

        consoleWriter.log(" Fibonacci calculation [column] Fib(" + j + ") (took in average " + avgTime + "ns) ");
    }

    private void rowBasedFibonacciMatrix(int j, ConsoleWriteWrapper consoleWriter) {
        /*
        long[] times = new long[verificationCount];

        for (int i = 0; i < verificationCount; i++) {

            Matrix rowBasedMatrix = new RowBasedDoubleMatrix(2, 2);
            consoleWriter.log(" Fibonacci before calculation \n " + rowBasedMatrix.toString());

            ExecutionTimer<Matrix> timer = new ExecutionTimer<>(() -> rowBasedMatrix.potentiate(j));

            Matrix result = timer.result;
            consoleWriter.log(" Calcualting Fibonnaci [row] (took " + timer.time + "ns) \n " + result.toString());
            times[i] = timer.time;
        }

        long avgTime = (Arrays.stream(times).sum() / times.length);
        updateSeries(j, avgTime, columnBasedSeries);

        consoleWriter.log(" Fibonacci calculation [row] Fib(" + j + ") (took in average " + avgTime + "ns) ");
        */
    }

    private void drawLogarithmicLine(int i) {
        updateSeries(i, (long)Math.log(i), logarithmicSeries);
    }

    private void drawLinearLine(int i) {
        updateSeries(i, i, linearSeries);
    }

    private void drawQuadraticLine(int i) {
        updateSeries(i, i*i, quadraticSeries);
    }

    private void drawQubicLine(int i) {
        updateSeries(i, i*i*i, qubicSeries);
    }

}
