package algorithms.examples;

import algorithms.templates.Matrix;

public class ColumnBasedDoubleMatrix implements Matrix<Double> {
    private Double[][] matrix;

    public ColumnBasedDoubleMatrix(int columnNumber, int rowNumber) {
        this.matrix = new Double[rowNumber][columnNumber];
    }

    @Override
    public Double[][] getMatrix() {
        return this.matrix;
    }

    @Override
    public int getWidth() {
        return this.matrix[0].length;
    }

    @Override
    public int getHeight() {
        return this.matrix.length;
    }

    @Override
    public void setValue(int column, int row, Double value) {
        this.matrix[row][column] = value;
    }

    @Override
    public Double getValue(int column, int row) {
        return this.matrix[row][column];
    }

    @Override
    public Matrix<Double> multiply(Matrix<Double> matrix) {
        if (this.getWidth() == matrix.getHeight()) {
            Double[][] result = new Double[this.getHeight()][matrix.getWidth()];
            for (int i = 0; i < this.getHeight(); i++) {
                for (int j = 0; j < this.getWidth(); j++) {



                }
            }
        }

        return null;
    }

    @Override
    public Matrix<Double> potentiate(int n) {

        return null;
    }


    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                builder.append(getValue(j, i)).append("\t");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
