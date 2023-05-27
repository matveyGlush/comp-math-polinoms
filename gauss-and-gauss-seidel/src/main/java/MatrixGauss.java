import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class MatrixGauss {
    private double[][] matrix;
    private int dimension;
    private double[] vector;
    final double zero = 1E-3;

    public double[][] getMatrix() { return matrix; }
    public double[] getVector() { return vector; }
    public int getDimension() { return dimension; }

    /**
     * Method for displaying matrix in table representation.
     */
    public void printMatrix() {
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) {
                System.out.printf("%15.6E", aDouble);
            }
            System.out.println(); //Makes a new row
        }
        System.out.println();
    }

    /**
     * Method for printing vector-column of values.
     */
    public void printVector() {
        if (vector == null) {
            System.out.println("no solution for today, go get some rest");
            return;
        }
        for (double v : vector) {
            System.out.printf("%15.6E", v);
        }
    }

    private void swapRows(int row1, int row2) {
        double[] tempRow = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = tempRow;
    }

    private void zeroUnderDiagonal(int col) {
        for (int row = col + 1; row < dimension; row++) {
            double coef = -matrix[row][col] / matrix[col][col];
            for (int i = col; i <= dimension; i++) {
                matrix[row][i] += coef * matrix[col][i];
                if (Math.abs(matrix[row][i]) < zero) matrix[row][i] = 0;
            }
        }
    }

    private int reduceToTriangularForm(double[][] matrix) {
        for (int col = 0; col < dimension; col++) {
            // ищем максимальный по модулю элемент в столбце
            double maxElement = Math.abs(matrix[col][col]);
            int maxRow = col;
            for (int row = col + 1; row < dimension; row++) {
                double curElement = Math.abs(matrix[row][col]);
                if (curElement > maxElement) {
                    maxElement = curElement;
                    maxRow = row;
                }
            }
            // если максимальный элемент в столбце равен нулю, возвращаем вердикт, не доходя до конца
            if (maxElement == 0) {
                return 2;
            }
            // меняем местами строки, чтобы максимальный элемент оказался на главной диагонали
            if (maxRow != col) {
                swapRows(col, maxRow);
            }
            // Приводим элементы в столбце под главной диагональю к нулю. Передаем текущий столбец
            zeroUnderDiagonal(col);

            // если ранг матрицы равен количеству неизвестных, то матрица невырожденная
            if (col+1 == dimension) {
                return 1;
            }
        }
        if (matrix[dimension-1][dimension] == 0) return 2;
        else return 3;
    }

    /**
     * Calculated solution of system of linear equations.
     */
    private void reversal() {
        vector = new double[dimension];
        for (int i = dimension - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < dimension; j++) {
                sum += matrix[i][j] * vector[j];
            }
            vector[i] = (matrix[i][dimension] - sum) / matrix[i][i];
        }
    }

    /**
     * Method with Gaussian method realization.
     */
    public void gaussSolution() {
        int result = reduceToTriangularForm(matrix);
        System.out.println("Your augmented matrix reduced to triangular form:");
        printMatrix();

        switch (result) {
            case (1) -> {
                System.out.println("Матрица имеет одно единственное решение");
                // Обратный ход
                reversal();
            }
            case (2) -> System.out.println("Матрица имеет бесконечно много решений");
            case (3) -> System.out.println("Матрица не имеет решений");
            default -> System.out.println("неожиданность");
        }
    }

    /**
     * Method for receiving matrix from the file
     */
    public void receiveMatrixFromFile(String path) throws FileNotFoundException {
        int counter = 0;
        File file = new File(path);
        if (!(file.exists() && file.isFile())) throw new FileNotFoundException();
        Scanner fileScanner = new Scanner(new BufferedReader(new FileReader(file)));

        dimension = Integer.parseInt(fileScanner.nextLine().trim());
        matrix = new double[dimension][dimension+1];

        for (int i = 0; i < matrix.length; i++) {
            String[] line = fileScanner.nextLine().trim().split(" ");
            for (int j = 0; j < line.length; j++) {
                matrix[i][j] = Double.parseDouble(line[j]);
                counter += 1;
                if (counter == dimension * dimension + dimension) return;
            }
        }
    }
}
