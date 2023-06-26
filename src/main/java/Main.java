public class Main {
    public static void main(String[] args) {
//        init1();
//        init2();
//        init3();
//        init4();
//        init5();
        initMultiply();
    }

    private static void initMultiply() {
        double[][] polinom1Parametrs = { {1, 5}, {1, 4}, {1, 3}, {1, 2}, {1, 0}}; // {coeff, degree}
        Polinom polinom1 = new Polinom(polinom1Parametrs);

        double[][] polinom2Parametrs = { {1, 4}, {1, 1} };
        Polinom polinom2 = new Polinom(polinom2Parametrs);
        System.out.println();
        System.out.println("Перемножение двух произвольных полиномов: ");
        polinom2.multiply(polinom1);
        polinom2.print();
    }

    // Инициализация первой сетки
    private static void init1() {
        // начальная и конечная точки сетки
        double a = 0;
        double b = 1;
        // количество точек в сетке
        int count = 7;
        // вычисляем расстояние между соседними точками
        double step = (b - a) / count;
        // создаем массив точек на оси X
        double[] x = new double[(int) ((b - a) / step)];
        // заполняем массив точек на оси X
        for (int i = 0; i < x.length; i++) {
            x[i] = a + step * i;
        }
        // создаем объект Grid для этой сетки
        Grid grid = new Grid(x, o -> (o*o*o*o*o*o*3.8332 - 6.7677*o*o*o - 0.0023*o));
        // передаем объект Grid в метод initShared() для интерполяции
        initShared(grid, true);
    }

    // Инициализация второй сетки
    private static void init2() {
        // начальная и конечная точки сетки
        double a = 0;
        double b = 1;
        // количество точек в сетке
        int count = 7;
        // вычисляем расстояние между соседними точками
        double step = (b - a) / (count - 1);
        // создаем массив точек на оси X
        double[] x = new double[count];
        // заполняем массив точек на оси X
        for (int i = 0; i < x.length; i++) {
            x[i] = a + step * i;
        }
        // создаем объект Grid для этой сетки
        Grid grid = new Grid(x, o -> (Math.sin((o * o) / 2)));
        // передаем объект Grid в метод initShared() для интерполяции
        initShared(grid, true);
    }

    private static void init3() {
        double[] x = { -9, 12, 33, 54, 75 }; // ans: 2,570945e-6x^4 + 0,008977x^3 + 0,87426x^2 - 25,62x + 192
        double[] y = { 500, -5, -21, -34, -518 };

        Grid grid = new Grid(x, y);
        initShared(grid, false);
    }

    private static void init4() {
        double[] x = { -1, 0, 1, 2 }; // ans: 1/2x^3-5/2x+2
        double[] y = { 4, 2, 0, 1 };

        Grid grid = new Grid(x, y);
        initShared(grid, false);
    }

    // Инициализация второй сетки
    private static void init5() {
        // начальная и конечная точки сетки
        double a = 0;
        // количество точек в сетке
        int count = 6;
        // вычисляем расстояние между соседними точками
        double step = 1;
        // создаем массив точек на оси X
        double[] x = new double[count];
        // заполняем массив точек на оси X
        for (int i = 0; i < x.length; i++) {
            x[i] = a + step * i;
        }
        // создаем объект Grid для этой сетки
        Grid grid = new Grid(x, o -> (Math.pow(o, 0.4)));
        // передаем объект Grid в метод initShared() для интерполяции
        initShared(grid, true); // ans 1.7796 x − 1.1059 x^2 + 0.38831 x^3 − 0.066345 x^4 + 0.0043460 x^5
    }

    private static void initShared(Grid grid, boolean withFun) {
        // создаем полином Лагранжа для данной сетки
        Lagrange lagrange = new Lagrange(grid);
        // выводим на печать название полинома
        System.out.println("Полином Лагранжа");
        // выводим на печать формулу полинома
        lagrange.print();
        // выводим таблицу значений функции и интерполяционной функции для данного полинома
        if (withFun) print(lagrange);
        else printWithoutFunValue(lagrange);

        // создаем полином Ньютона для данной сетки
        Newton newton = new Newton(grid);
        // выводим на печать название полинома
        System.out.println("Полином Ньютона");
        // выводим на печать формулу полинома
        newton.print();
        // выводим таблицу значений функции и интерполяционной функции для данного полинома
        if (withFun)
            print(newton);
        else printWithoutFunValue(newton);
    }

    public static void print(Interpolation interpolation) {
        System.out.println("Таблица");
        // выводим заголовок таблицы
        System.out.printf("%12s\t%12s\t%12s\t%12s\n", "x", "y", "f(x)", "In(x)");
        // получаем количество точек в сетке
        double n = interpolation.getGrid().getSize();
        // вычисляем шаг между соседними точками
        double step = interpolation.getGrid().getX(1) - interpolation.getGrid().getX(0);
        // проходим по всем точкам сетки и выводим значения функции и интерполяционной функции
        for (int i = 0; i < n; i++) {
            double xi = interpolation.getGrid().getX(i);
            System.out.printf("%10.6e\t%10.6e\t%10.6e\t%10.6e\n", xi, interpolation.getGrid().getY(i), interpolation.getGrid().getFunctionValue(xi), interpolation.value(xi));

            // для всех точек, кроме последней, выводим дополнительное значение интерполяционной функции
            if (i != n - 1) {
                xi += step / 2;
                System.out.printf("%10.6e\t%12s\t%10.6e\t%10.6e\n", xi, "", interpolation.getGrid().getFunctionValue(xi), interpolation.value(xi));
            }
        }
    }

    public static void printWithoutFunValue(Interpolation interpolation) {
        System.out.println("Таблица");
        // выводим заголовок таблицы
        System.out.printf("%12s\t%12s\t%12s\n", "x", "y", "In(x)");
        // получаем количество точек в сетке
        double n = interpolation.getGrid().getSize();
        // вычисляем шаг между соседними точками
        double step = interpolation.getGrid().getX(1) - interpolation.getGrid().getX(0);
        // проходим по всем точкам сетки и выводим значения функции и интерполяционной функции
        for (int i = 0; i < n; i++) {
            double xi = interpolation.getGrid().getX(i);
            System.out.printf("%10.6e\t%10.6e\t%10.6e\n", xi, interpolation.getGrid().getY(i), interpolation.value(xi));

            // для всех точек, кроме последней, выводим дополнительное значение интерполяционной функции
            if (i != n - 1) {
                xi += step / 2;
                System.out.printf("%10.6e\t%12s\t%10.6e\n", xi, "", interpolation.value(xi));
            }
        }
    }

}
