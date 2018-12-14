package algorithms.examples;

import algorithms.templates.Matrix;

public class ColumnBasedLongMatrix implements Matrix<Long> {
    private Long[][] matrix;

    public ColumnBasedLongMatrix(int columnNumber, int rowNumber) {
        this.matrix = new Long[columnNumber][rowNumber];

        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                this.setValue(j, i, (long)0);
            }
        }
    }

    @Override
    public Long[][] getMatrix() {
        return this.matrix;
    }

    @Override
    public int getWidth() {
        return this.matrix.length;
    }

    @Override
    public int getHeight() {
        return this.matrix[0].length;
    }

    @Override
    public void setValue(int row, int column, Long value) {
        this.matrix[column][row] = value;
    }

    @Override
    public Long getValue(int row, int column) {
        return this.matrix[column][row];
    }

    @Override
    public Matrix<Long> multiply(Matrix<Long> matrix) {

        if (this.getWidth() != matrix.getHeight()) {
            String message = String.format("(%dx%d) & (%dx%d)", this.getWidth(), this.getHeight(), matrix.getWidth(), matrix.getHeight());
            throw new IllegalArgumentException("Matrices " + message + " cannot be multiplied!");
        }

        Matrix<Long> resultMatrix = new ColumnBasedLongMatrix(this.getHeight(), matrix.getWidth());

        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < matrix.getWidth(); j++) {

                //TODO: Calculate each field

                //https://www.google.ch/imgres?imgurl=https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Matrix_multiplication_diagram_2.svg/313px-Matrix_multiplication_diagram_2.svg.png&imgrefurl=https://en.wikipedia.org/wiki/Matrix_multiplication&h=275&w=313&tbnid=uh9XG5Bi_r-C2M:&q=matrix+multiplication&tbnh=176&tbnw=200&usg=AI4_-kR5pXKNF3Ydkhi1--LDBGnR7G8q6w&vet=12ahUKEwi91eWNh5PfAhVOKVAKHWcgCz8Q_B0wHHoECAQQBg..i&docid=kYLWyiVwPjVdDM&itg=1&sa=X&ved=2ahUKEwi91eWNh5PfAhVOKVAKHWcgCz8Q_B0wHHoECAQQBg

                //c11 => a11*b11 + a12*b21
                //c12 => a11*b12 + a12*b22
                //c21 => a21*b11 + a22*b21
                //c22 => a21*b12 + a22*b22

                //EXAMPLE
                // A x B        | 0  1 |
                //              | 1  2 |
                // -----------------------
                // | 0  1 |     | 1  2 |
                // | 1  2 |     | 2  5 |

                // A x B        | 0  1 |
                //              | 1  2 |
                // -----------------------
                // | 1  2 |     | 2  5 |
                // | 2  5 |     | 5 12 |


                for (int k = 0; k < matrix.getWidth(); k++) {
                    long value = resultMatrix.getValue(i, j);
                    resultMatrix.setValue(i, j, (value + (this.getValue(i, k) * matrix.getValue(k, j))));
                }
            }
        }

        return resultMatrix;
    }

    @Override
    public Matrix<Long> potentiate(int n) {

        Matrix<Long> resultMatrix = this;

        while (n > 1) {
            resultMatrix = resultMatrix.multiply(this); // Square
            n--;
        }

        return resultMatrix;
    }


    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                builder.append("\t").append(getValue(i, j));
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
