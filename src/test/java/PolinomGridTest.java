import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PolinomGridTest {

    public static double roundDown(double value) {
        if (value >= 0) return Math.floor(value * 1000000) / 1000000;
        return -Math.floor(Math.abs(value) * 1000000) / 1000000;
    }

    private static Stream<Arguments> functionAndXGridNodes() {
        return Stream.of(
                arguments(new double[]{ -1, 0, 1, 2 }, new double[]{ 4, 2, 0, 1 },
                        new double[][]{{0.500000,3}, {-2.500000,1}, {2.000000,0}}),
                arguments(new double[]{ -9, 12, 33, 54, 75 }, new double[]{ 500, -5, -21, -34, -518 },
                        new double[][]{{0.000002,4}, {-0.008977,3}, {0.874265,2}, {-25.621754,1}, {192.027072,0}})
        );
    }

    @ParameterizedTest
    @MethodSource("functionAndXGridNodes")
    void resultPolinom(double[] x, double[] y, double[][] ans) {

        Grid grid = new Grid(x, y);
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
