public abstract class Interpolation {
    protected Polinom polinom; // полином, который будет использоваться для интерполяции
    protected Grid grid; // сетка, на которой проводится интерполяция

    // конструктор, принимающий объект сетки и инициализирующий полином
    Interpolation(Grid grid) {
        this.grid = grid;
        this.polinom = new Polinom();
    }

    // метод для получения значения полинома в точке
    public double value(double point) {
        return polinom.value(point);
    };

    // метод для вывода полинома на экран
    public void print() {
        polinom.print();
    };

    // метод для получения сетки
    public Grid getGrid() {
        return this.grid;
    }
}
