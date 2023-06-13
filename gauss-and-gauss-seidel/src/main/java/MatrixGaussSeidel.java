import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixGaussSeidel {
    private int dimension; // размерность
    private double[][] matrix; // коэффициенты
    private double[] solution;
    private int[] order;
    private double tolerance; // заданная точность, также является нулем при сравнении вещественных чисел
    private int maxIterations; // количество итераций для схождения

    /**
     * Метод вывода матрицы
     */
    public void print() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension+1; j++) {
                System.out.printf("%15.6E", matrix[getIndex(i)][j]);
            }
            System.out.println();
        }
    }

    /**
     * Метод возвращает индекс i-ой строки
     */
    private int getIndex(int i) {
        return order == null ? i : order[i];
    }

    /**
     * Метод для решения системы, возвращает:
     * 2 - невозможно решить
     * 1 - метод сходится
     * 0 - найдено решение
     */
    public void resolve() {
        double[] sums = new double[dimension];
        int permutation = rearrangeRows(sums); // ищется перестановка
        System.out.println("после перестановки:");
        print(); // выводится результат перестановки

        switch (permutation) {
            case 2:
                System.out.println("Невозможно решить итерационным методом");
                break;
            case 1:
                if (resolveWithControl()) {
                    System.out.println("Метод расходится");
                    break;
                }
            case 0:
                resolveWithoutControl();
        }
    }

    /**
     * Метод для проверки наличия нулей на главной диагонали
     */
    public boolean areThereZerosOnDiagonal(double[] sums) {
        boolean isZero = false;
        // Создание нового массива с размерностью матрицы
        // Проход по каждой строке матрицы
        for (int i = 0; i < dimension; i++) {
        // Проход по каждому столбцу матрицы
            for (int j = 0; j < dimension; j++) {
                // Суммирование всех элементов каждой строки
                sums[i] += Math.abs(matrix[i][j]);
                // Если текущий элемент находится на главной диагонали и его
                // модуль меньше tolerance (порога терпимости), то возвращаем true
                if (i == j && Math.abs(matrix[getIndex(i)][j]) < tolerance)
                    isZero = true;//если на диагонали 0
            }
        }
        // Если нулей на главной диагонали не найдено, возвращаем false
        return isZero;
    }

    /**
     * Метод перестановки строк.
     * 0 - выполнена ДУС
     * 1 - не выполнена ДУС
     * 2 - нет перестановки
     */
    public int rearrangeRows(double[] sums) {
        // Если на главной диагонали есть нули
        if (areThereZerosOnDiagonal(sums)) {
            // Создание массивов для перестановок строк
            int[] permutation = new int[dimension];
            boolean[] isFree = new boolean[dimension];

            // Установка флагов для всех строк, что они свободны
            for (int i = 0; i < dimension; i++) {
                isFree[i] = true;
            }

            try {
                // Получение порядка перестановки строк и проверка на ее успешность
                if (getOrder(0, permutation, isFree, sums)) return 0;
                else return order == null ? 2 : 1;
            } catch (ResultException e) {
                return e.getI();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            // Если на главной диагонали нет нулей
            // Проверка, является ли матрица СЛАУ совместной
            return isSCC(sums) ? 0 : 1;
        }
    }

    /**
     * Метод для перебора всех возможных перестановок строк
     */
    private boolean getOrder(int index, int[] permutation, boolean[] free, double[] sums) throws Exception {

        // Если это последняя строка, которую нужно переместить на главную диагональ
        if (index == dimension - 1) {
            for (int i = 0; i < dimension; i++) {
                if (free[i]) {
                    permutation[index] = i;
                    short result = isSCC(sums, permutation);
                    if (result == 0 || result == 1) {
                        order = permutation;
                        throw new ResultException(result);
                    }
                    break;
                }
            }
        } else {
            // Проходим по всем строкам, которые еще не были использованы, и пытаемся их использовать
            for (int i = 0; i < dimension; i++) {
                // Если строка не использована и ее элемент на диагонали не равен нулю
                if (free[i] && Math.abs(matrix[i][index]) > tolerance) {
                    free[i] = false; // Отмечаем строку как использованную
                    permutation[index] = i; // Добавляем строку в порядок перестановки
                    // Рекурсивно вызываем этот метод для следующего индекса
                    if (getOrder(index + 1, permutation, free, sums)) {
                        return true;
                    }
                    // Снимаем отметку об использовании строки, чтобы можно было попробовать другую строку
                    free[i] = true;
                }
            }
        }
        return false;
    }

    /**
     * Метод проверки ДУС для матрицы
     * @return - значение флага isSCC
     */
    public boolean isSCC(double[] sums) {
        boolean isSCC = false;
        // Итерация по строкам матрицы
        for (int i = 0; i < dimension; i++) {
            // Вызов метода isRowSCC для каждой строки матрицы и получение результата
            switch (isRowSCC(getIndex(i), i, sums)) {
                case 0:
                    isSCC = true;
                case 1:
                    break;
                case 2:
                    return false;
            }
        }
        // Возвращаем значение флага isSCC
        return isSCC;
    }

    private short isSCC(double[] sums, int[] perm) {
        short isSCC = 1;
        // Итерация по строкам матрицы
        for (short i = 0; i < dimension; i++) {
            // Вызов метода isRowSCC для каждой строки матрицы и получение результата
            switch (isRowSCC(perm[i], i, sums)) {
                case 0:
                    isSCC = 0;
                case 1:
                    break;
                case 2:
                    return 2;
            }
        }
        // Возвращаем значение флага isSCC
        return isSCC;
    }

    /**
     * Метод проверки достаточного условия сходимости для строки
     * @param rowId - индекс строки
     * @param diagonalId - индекс диагонального элемента
     * @return - значение переменной после выражения
     */
    private int isRowSCC(int rowId, int diagonalId, double[] sums) {

        double element = Math.abs(matrix[rowId][diagonalId]);

        if (element > sums[rowId] - element + tolerance) {
            return 0;  // ДУС выполнена в полном объёме для строки (>)
        } else if (element > sums[rowId] - element - tolerance) {
            return 1;  // ДУС выполнена в неполном объёме для строки (>=)
        } else {
            return 2;  // ДУС не выполнена для строки (<)
        }
    }

    /**
     * Метод для расчета решения
     */
    public void resolveWithoutControl() {
        // Выполняем итерации до тех пор, пока разница между текущим
        // и предыдущим решениями не станет меньше заданной точности
        double difference;
        do {
            difference = iterate();
        } while (difference >= tolerance);
    }

    /**
     * Метод для расчета решения, ограниченного кол-вом итераций,
     * @return - значение переменной после выражения,
     * где true - метод расходится, false - метод сходится
     */
    public boolean resolveWithControl() {
        // количество итераций, на которых произошло уменьшение разницы
        // между текущим и предыдущим решениями
        int decreaseCount = 0;

        // выполняем первую итерацию и запоминаем разницу между текущим и предыдущим решениями
        double difference = iterate();

        // выполняем итерации до тех пор, пока не будет достигнуто максимальное количество итераций (10)
        // или не будет достигнута нужная точность
        for (int i = 1; i < 25; i++) {

            // выполняем новую итерацию и запоминаем разницу между текущим и предыдущим решениями
            double newDifference = iterate();

            if (newDifference < difference) //проверяем монотонное убывание на каждом шаге
                decreaseCount++;
            else
                decreaseCount = 0;
            if (newDifference < tolerance) { //выходим из цикла, если добились нужной точности
                return false;
            }
            difference = newDifference; // обновляем разницу между текущим и предыдущим решениями
        }
        // возвращаем true, если на последних maxIterations итерациях не произошло уменьшение
        // разницы между текущим и предыдущим решениями, и false в противном случае
        return decreaseCount < maxIterations;
    }

    /**
     * Метод для итерации поиска решения
     */
    private double iterate() {
        //Переменная для хранения новых значений переменных
        double newValue;
        //Переменная для хранения разницы между новым и старым значениями переменных
        double difference;
        //Переменная для хранения максимальной разницы между новым и старым значениями переменных
        double maxDifference = Double.MIN_VALUE;
        //Цикл итерации по всем переменным
        for (int i = 0; i < dimension; i++) {
            //Расчет нового значения переменной
            newValue = solveVariable(i);
            //Расчет разницы между новым и старым значениями переменной
            difference = Math.abs(newValue - solution[i]);
            //Обновление значения переменной в решении
            solution[i] = newValue;
            //Если разница между новым и старым значениями переменной больше,
            //чем максимальная разница на текущей итерации,
            //то обновляем значение максимальной разницы
            if (difference > maxDifference)
                maxDifference = difference;
        }
        return maxDifference;
    }

    /**
     * Метод, выражающий переменную
     * @param index - индекс переменной, которую нужно выразить
     * @return - значение переменной после выражения
     */
    private double solveVariable(int index) {
        double sum = 0; // переменная для хранения суммы
        for (int i = 0; i < dimension; i++) { // проходим по всем столбцам матрицы
            if (i != index) // если столбец не является тем, который нужно выразить
                sum += matrix[getIndex(index)][i] * solution[i]; // добавляем соответствующее слагаемое в сумму
        }
        // выражаем переменную и возвращаем ее значение
        return (matrix[getIndex(index)][dimension] - sum) / matrix[getIndex(index)][index];
    }

    /**
     * Метод для вывода решения
     */
    public void printSolution() {
        System.out.println("Ответ:");
        for (int i = 0; i < dimension; i++) {
            System.out.printf("%15.6E", solution[i]);
        }
    }

    /**
     * Метод для извлечения матрицы из файла
     * @param path - путь к файлу
     */
    public void receiveMatrixFromFile(String path) throws FileNotFoundException {
        int counter = 0;
        File file = new File(path);
        if (!(file.exists() && file.isFile())) throw new FileNotFoundException();
        Scanner fileScanner = new Scanner(new BufferedReader(new FileReader(file)));

        dimension = Integer.parseInt(fileScanner.nextLine().trim());
        tolerance = Double.parseDouble(fileScanner.nextLine().trim());
        maxIterations = Integer.parseInt(fileScanner.nextLine().trim());

        matrix = new double[dimension][dimension+1];
        solution = new double[dimension];

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