public class Newton extends Interpolation{
    private double[] deltasY;     // распределённые разности

    Newton(Grid grid) {
        super(grid);

        int n = grid.getSize();
        // инициализация массива дельт
        initDeltasY(n);

        // создание полинома с первым коэффициентом
        polinom = new Polinom(new double[] { grid.getY(0) });

        // создание полинома-базиса
        Polinom basis = new Polinom(new double[] {1});

        // цикл для вычисления всех коэффициентов полинома
        for (int i = 0; i < n - 1; i++) {
            Polinom binom = new Polinom(new double[]{1, -grid.getX(i)}); // создание бинома
            // умножение полинома-базиса на полином-бином, потом умножение результата на соответствующую дельту,
            // в конце добавление нового слагаемого в полином
            polinom.add(new Polinom(basis.multiply(binom).getStart()).multiply(deltasY[i + 1]));
        }
    }

    private void initDeltasY(int n) {
        // Инициализация массива для хранения разделенных разностей
        deltasY = new double[n];

        // Инициализация массива для временного хранения значений разделенных разностей
        double[] next = new double[n];

        // Заполнение массива deltasY значениями y узлов
        for (int i = 0; i < n; i++) {
            deltasY[i] = grid.getY(i);
        }

        // Цикл вычисления разделенных разностей
        for (int k = 1; k < n; k++) {
            // Вычисление новых значений разделенных разностей
            for (int i = k; i < n; i++) {
                next[i] = (deltasY[i-1] - deltasY[i]) / (grid.getX(i-k) - grid.getX(i));
            }

            // Обновление значений разделенных разностей
            for (int i = k; i < n; i++) {
                deltasY[i] = next[i];
            }
        }
    }
}