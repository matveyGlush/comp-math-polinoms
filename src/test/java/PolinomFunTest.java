import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PolinomFunTest {

    public static double roundDown(double value) {
        if (value >= 0) return Math.floor(value * 1000000) / 1000000;
        return -Math.floor(Math.abs(value) * 1000000) / 1000000;
    }

    private static Stream<Arguments> functionAndXGridNodes() {
        return Stream.of(
                arguments((Function) o -> Math.pow(o, 0.4), 0, 5, 6,
                        new double[][]{{0.004346,5}, {-0.066345,4}, {0.388306,3}, {-1.105941,2}, {1.779633,1}}),
                arguments((Function) o -> o*o*o*o*o*o*3.8332 - 6.7677*o*o*o - 0.0023*o, 0, 1, 7,
                        new double[][]{{3.833200,6}, {-6.767700,3}, {-0.00230,1}}),
                arguments((Function) o -> Math.sin((o * o) / 2), 0, 1, 7,
                        new double[][]{{-0.016304,6}, {-0.009903,5}, {0.008519,4}, {-0.003520,3}, {0.500683,2}, {-4.8E-5,1}})
        );
    }

    @ParameterizedTest
    @MethodSource("functionAndXGridNodes")
    void resultPolinom(Function function, double a, double b, int count, double[][] ans) {
        double step = (b - a) / (count - 1);

        double[] x = new double[count];
        for (int i = 0; i < x.length; i++) {
            x[i] = a + step * i;
        }

        Grid grid = new Grid(x, function);
        Lagrange lagrange = new Lagrange(grid);
        Newton newton = new Newton(grid);

        Polinom.Monom currLg = lagrange.polinom.head;
        Polinom.Monom currNw = newton.polinom.head;

        for (int i = 0; i < ans.length; i++) {
            assertEquals(ans[i][0], roundDown(currLg.coeff));
            assertEquals(ans[i][1], currLg.degree);
            assertEquals(ans[i][0], roundDown(currNw.coeff));
            assertEquals(ans[i][1], currNw.degree);
            currLg = currNw.next;
            currNw = currNw.next;
        }
    }
}