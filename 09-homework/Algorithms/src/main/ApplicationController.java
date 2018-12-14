package main;

import algorithms.algorithms.helper.ConsoleWriteWrapper;
import algorithms.algorithms.helper.ExecutionTimer;
import algorithms.examples.*;
import algorithms.templates.Matrix;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.util.*;

public class ApplicationController {

	@FXML
	private LineChart<String, Long> lineChart;

	@FXML
	private Button btnCalculate;

	@FXML
    private TextField txtMaxXValue;

	@FXML
    private CheckBox cbColumnBased;

	@FXML
    private CheckBox cbRowBased;

	@FXML
    private CheckBox cbEnableLogs;

	private int verificationCount = 10;

	private final XYChart.Series<String, Long> columnBasedSeries;
	private final XYChart.Series<String, Long> rowBasedSeries;

    private final XYChart.Series<String, Long> linearSeries;

	public ApplicationController() {
        columnBasedSeries = new XYChart.Series<>();
        rowBasedSeries = new XYChart.Series<>();

        linearSeries= new XYChart.Series<>();
    }
	
	@FXML
	private void initialize() {
        lineChart.getXAxis().setLabel("Fibonacci (x) -> x -> Result is logged in console");
        lineChart.getYAxis().setLabel("time used to calculate in [ns]");

		lineChart.getData().add(columnBasedSeries);
		lineChart.getData().add(rowBasedSeries);

		lineChart.getData().add(linearSeries);

		columnBasedSeries.setName("Fibonacci Matrix (column based)");
        rowBasedSeries.setName("Fibonacci Matrix (row based)");

        linearSeries.setName("Reference line Linear");


		cbColumnBased.setSelected(true);
		cbRowBased.setSelected(true);

        txtMaxXValue.setText("20");
	}
	
	@FXML
	private void onCalculate() {
	    //Clear the plot
        columnBasedSeries.getData().clear();
        rowBasedSeries.getData().clear();

        linearSeries.getData().clear();

        //Read n from textField
        int n;
        try {
            n = Integer.parseInt(txtMaxXValue.getText());
        } catch(NumberFormatException exc) {
            System.err.println("Could not create number out of '" + txtMaxXValue.getText() + "'");
            return;
        }

        int startValue = 1;
        int endValue = n;
        int interval = 1;

        if (startValue > endValue) {
            System.err.println("Start value is " + startValue + " - End value is " + endValue + " -> End must be greater!");
            return;
        }

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

                for (int i = startValue; i <= endValue ; i += interval) {
                    consoleWriter.log(String.format("New Calculation run with Fibonacci(%d)\n", i));

                    if (isColumnBasedEnabled) {
                        columnBasedFibonacciMatrix(i, consoleWriter);
                    }

                    if (isRowBasedEnabled) {
                        rowBasedFibonacciMatrix(i, consoleWriter);
                    }

                    consoleWriter.log(String.format("\nCalculation run with Fibonacci(%d) finished!\n\n", i));
                }

                drawLinearLine(startValue, endValue, interval, consoleWriter);
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
            Matrix columnBasedMatrix = new ColumnBasedLongMatrix(2, 2);
            setupInitialFibonacciMatrix(columnBasedMatrix);
            consoleWriter.log(" Fibonacci(" + j + ") [column] \n " + columnBasedMatrix.toString());

            ExecutionTimer<Matrix> timer = new ExecutionTimer<>(() -> columnBasedMatrix.potentiate(j));

            Matrix result = timer.result;
            consoleWriter.log(" Fibonacci(" + j + ") [column] (took " + timer.time + "ns) \n " + result.toString());
            times[i] = timer.time;
        }

        long avgTime = (Arrays.stream(times).sum() / times.length);
        updateSeries(j, avgTime, columnBasedSeries);

        consoleWriter.log(" Fibonacci(" + j + ") [column] (took in average " + avgTime + "ns) ");
    }

    private void rowBasedFibonacciMatrix(int j, ConsoleWriteWrapper consoleWriter) {
        long[] times = new long[verificationCount];

        for (int i = 0; i < verificationCount; i++) {
            Matrix rowBasedMatrix = new RowBasedLongMatrix(2, 2);
            setupInitialFibonacciMatrix(rowBasedMatrix);
            consoleWriter.log(" Fibonacci(" + j + ") [row] \n " + rowBasedMatrix.toString());

            ExecutionTimer<Matrix> timer = new ExecutionTimer<>(() -> rowBasedMatrix.potentiate(j));

            Matrix result = timer.result;
            consoleWriter.log(" Fibonacci(" + j + ") [row] (took " + timer.time + "ns) \n " + result.toString());
            times[i] = timer.time;
        }

        long avgTime = (Arrays.stream(times).sum() / times.length);
        updateSeries(j, avgTime, rowBasedSeries);

        consoleWriter.log(" Fibonacci(" + j + ") [row] (took in average " + avgTime + "ns) ");
    }

    private void setupInitialFibonacciMatrix(Matrix<Long> matrix) {
	    matrix.setValue(0, 0, (long) 0);
	    matrix.setValue(1, 0, (long) 1);
	    matrix.setValue(0, 1, (long) 1);
	    matrix.setValue(1, 1, (long) 2);
    }

    private void drawLinearLine(int startValue, int endValue, int interval, ConsoleWriteWrapper consoleWriter) {
	    ObservableList<XYChart.Data<String, Long>> data = columnBasedSeries.getData().size() != 0 ? columnBasedSeries.getData() : rowBasedSeries.getData();

	    if (data.size() == 0) {
	        consoleWriter.error("Matrix calculated series are not yet ready! --> No linear reference!");
	        consoleWriter.error("Please recalculate again!");
	        return;
        }

        long ordinate = data.get(0).getYValue();
        long incline = (data.get(data.size() - 1).getYValue() - data.get(0).getYValue()) / data.size();

        for (int i = startValue; i <= endValue ; i += interval) {
            long yValue =  incline * i + ordinate;
            updateSeries(i, yValue, linearSeries);
        }

        consoleWriter.log("Linear function = f(x) = " + incline + "x + " + ordinate);
    }

}
