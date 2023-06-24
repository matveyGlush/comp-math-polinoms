public class Grid {

    // представляет собой узел сетки
    private class Node {
        double x, y;

        Node(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private final Node[] nodes; // массив узлов
    private final int size; //Число узлов
    private Function function = null; // заданная функция

    // Конструктор класса Grid, принимающий массив значений X и заданную функцию
    Grid(double[] x, Function function) {
        // выкидываем исключение, если узлов меньше 2
        if (x.length < 2) throw new IllegalArgumentException();
        this.function = function;
        size = x.length;
        // инициализируем узлы сетки и значения в них
        nodes = new Node[size];
        for (int i = 0; i < size; i++) {
            nodes[i] = new Node(x[i], function.value(x[i]));
        }
    }

    // для теста
    Grid(double[] x, double[] y) {
        size = x.length;
        nodes = new Node[size];
        for (int i = 0; i < size; i++) {
            nodes[i] = new Node(x[i], y[i]);
        }
    }

    public double getX(int i) {
        return nodes[i].x;
    }

    public double getY(int i) {
        return nodes[i].y;
    }

    public int getSize() {
        return size;
    }

    // Метод для получения значения функции в точке x
    public double getFunctionValue(double x) {
        return function.value(x);
    }

}
