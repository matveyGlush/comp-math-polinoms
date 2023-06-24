public class Lagrange extends Interpolation {
    Lagrange(Grid grid) {
        super(grid);
        // проходим по всем точкам из сетки
        int size = grid.getSize();
        for (int i = 0; i < size; i++) {
            // для каждой точки создаем базисный многочлен методом basis(i)
            // умножаем базисный многочлен на соответствующее значение Y из сетки и добавляем результат к полиному
            polinom.add(basis(i).multiply(grid.getY(i)));
        }
    }

    private Polinom basis(int i) {
        Polinom basis = new Polinom(new double[]{1}); //полином вида 1 + 0
        double divisor = 1;
        // проходим по всем точкам из сетки
        int size = grid.getSize();
        for (int j = 0; j < size; j++) {
            // если i и j различны, то вычисляем множитель (x - xj) и умножаем базисный многочлен на него
            if (i != j) {
                double xj = grid.getX(j);
                double xi = grid.getX(i);
                basis.multiply(new Polinom(new double[]{1, -xj})); // умножаем базисный многочлен на (x - xj)
                divisor *= 1.0 / (xi - xj); //вычисляем знаменатель формулы Лагранжа
            }
        }
        // возвращаем базисный многочлен, умноженный на знаменатель
        return basis.multiply(divisor);
    }
}
