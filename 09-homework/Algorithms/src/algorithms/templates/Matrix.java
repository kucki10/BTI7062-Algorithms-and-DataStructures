package algorithms.templates;

public interface Matrix<T> {
    T[][] getMatrix();

    int getWidth();
    int getHeight();

    void setValue(int column, int row, T value);

    T getValue(int column, int row);

    Matrix<T> multiply(Matrix<T> matrix);
    Matrix<T> potentiate(int n);
}
