import java.io.FileNotFoundException;

/**
 * Main class of this lab work. Allows to use Gaussian method
 * for solving systems of linear equations. Calculates residuals.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // Gauss method
//        MatrixGauss objMatrix = new MatrixGauss();
//        objMatrix.receiveMatrixFromFile("C:\\Users\\matve\\IdeaProjects\\test\\matrixSeidel.txt");
//
//        System.out.println("Your augmented matrix:");
//        objMatrix.printMatrix();
//        objMatrix.gaussSolution();
//        objMatrix.printVector();
//
//        System.out.println();

        // Gauss Seidel method
        MatrixGaussSeidel gaussSeidel = new MatrixGaussSeidel();
        gaussSeidel.receiveMatrixFromFile("C:\\Users\\matve\\IdeaProjects\\test\\matrixSeidel.txt");
        System.out.println("Система:");
        gaussSeidel.print();
        System.out.println();

        gaussSeidel.resolve();
        gaussSeidel.printSolution();
    }
}
