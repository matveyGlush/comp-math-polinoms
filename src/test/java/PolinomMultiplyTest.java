import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolinomMultiplyTest {

    @Test
    void resultPolinom() {
        double[][] ans = {{1,9}, {1,8}, {1,7}, {2,6}, {1,5}, {2,4}, {1,3}, {1,1}};

        double[][] polinom1Parametrs = { {1, 5}, {1, 4}, {1, 3}, {1, 2}, {1, 0}}; // {coeff, degree}
        Polinom polinom1 = new Polinom(polinom1Parametrs);

        double[][] polinom2Parametrs = { {1, 4}, {1, 1} };
        Polinom polinom2 = new Polinom(polinom2Parametrs);

        polinom2.multiply(polinom1);
        Polinom.Monom curr = polinom2.head;
        for (int i = 0; i<ans.length; i++) {
            assertEquals(ans[i][0], curr.coeff);
            assertEquals(ans[i][1], curr.degree);
            curr = curr.next;
        }
    }
}
