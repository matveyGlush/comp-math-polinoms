public class Polinom {

    class Monom {
        double coeff; // коэффициент
        int degree; // степень
        Monom next; // следующий моном

        Monom(double coeff, int degree) {
            this.coeff = coeff;
            this.degree = degree;
        }

        // Копирующий конструктор
        Monom(Monom newMonom) {
            coeff = newMonom.coeff;
            degree = newMonom.degree;
        }

        // Метод вывода монома на экран
        public void print() {
            System.out.printf("%12.6e x^%d", coeff, degree);
        }
    }

    protected Monom head; // головной элемент (первый моном) полинома
    private static final double ZERO = 1e-9; // константа для сравнения вещественных чисел с нулем

    // Конструктор по умолчанию
    public Polinom() {
    }


    // создаем полином из массива коэфициентов
    // используется для создания базиса
    public Polinom(double[] coeffs) {
        // Создаем переменную current типа Monom и присваиваем ей значение null
        Monom current = null;
        // Создаем цикл, проходящий по всем элементам массива коэффициентов
        for (int i = 0; i < coeffs.length; i++) {
            // Проверяем, не является ли текущий коэффициент равным нулю
            if (coeffs[i] != 0) {
                // Если переменная head равна null, создаем первый элемент списка Monom с текущим коэффициентом
                if (head == null) {
                    head = new Monom(coeffs[i], coeffs.length - i - 1);
                    // Присваиваем значение переменной current созданному элементу списка
                    current = head;
                } else {
                    // Иначе, создаем новый элемент списка Monom с текущим коэффициентом и добавляем его в конец списка
                    current.next = new Monom(coeffs[i], coeffs.length - i - 1);
                    current = current.next;
                }
            }
        }
    }

    // создаем полином из массива коэфициентов
    // используется для создания базиса
    public Polinom(double[][] monoms) {
        // Создаем переменную current типа Monom и присваиваем ей значение null
        Monom current = null;
        // Создаем цикл, проходящий по всем элементам массива коэффициентов
        for (double[] monom : monoms) {
            // Проверяем, не является ли текущий коэффициент равным нулю
            if (monom[0] != 0) {
                // Если переменная head равна null, создаем первый элемент списка Monom с текущим коэффициентом
                if (head == null) {
                    head = new Monom(monom[0], (int) monom[1]);
                    // Присваиваем значение переменной current созданному элементу списка
                    current = head;
                } else {
                    // Иначе, создаем новый элемент списка Monom с текущим коэффициентом и добавляем его в конец списка
                    current.next = new Monom(monom[0], (int) monom[1]);
                    current = current.next;
                }
            }
        }
    }

    //копирующий конструктор
    public Polinom(Monom from) {
        // Создаем новый объект Monom и присваиваем его значению переменной head
        this.head = new Monom(from);
        // Создаем переменные thisCurrent и current, присваиваем им ссылки на head
        // и на следующий элемент from соответственно
        Monom thisCurrent = this.head;
        Monom current = from.next;
        // Создаем цикл, который будет копировать элементы из переданного списка Monom
        // в новый список Monom, связанный с классом Polinom
        while (current != null) {
            // Создаем новый объект Monom и присваиваем его значению переменной thisCurrent.next
            thisCurrent.next = new Monom(current);
            // Присваиваем переменной thisCurrent значение следующего элемента списка Monom
            thisCurrent = thisCurrent.next;
            // Присваиваем переменной current значение следующего элемента переданного списка Monom
            current = current.next;
        }
    }

    // начало полинома
    public Monom getStart() {
        return head;
    }

    // +=
    public void add(Polinom polinom) {
        // (если нечего прибавлять)
        // Если добавляемый многочлен пуст, то возвращаем текущий многочлен
        if (polinom.head == null) return;

        // (если не к чему прибавлять)
        // Если текущий многочлен пуст, то копируем добавляемый многочлен и возвращаем его
        if (this.head == null) {
            this.head = new Polinom(polinom.head).head;
            return;
        }

        // Создаем переменные current и thisCurrent, которые будут ссылаться
        // на первый элемент добавляемого многочлена и текущего многочлена соответственно
        Monom current = polinom.head; // то, что мы прибавляем
        Monom thisCurrent = head; // то, к чему мы прибавляем


        // Если степень текущего Monom, который мы хотим добавить, больше степени текущего Monom текущего объекта
        // Polinom, то мы добавляем этот Monom в начало списка Monom текущего объекта Polinom
        if (current.degree > head.degree) {
            Monom newMonom = new Monom(current);
            head = newMonom;
            newMonom.next = thisCurrent;
            thisCurrent = newMonom;
            current = current.next;
        } else if (current.degree == head.degree) {
            // Если степень текущего Monom, который мы хотим добавить, равна степени текущего Monom текущего
            // объекта Polinom, то мы складываем коэффициенты этих Monom
            double sum = current.coeff + head.coeff;
            if (Math.abs(sum) < ZERO) { // == 0
                // Если сумма коэффициентов равна 0, то мы удаляем Monom текущего объекта Polinom
                head = thisCurrent.next;
            } else {
                // Иначе мы обновляем коэффициент Monom текущего объекта Polinom
                head.coeff = sum;
            }
            current = current.next;
        }

        // Создаем цикл, который будет проходить по каждому элементу добавляемого многочлена
        while (current != null) {
            if (thisCurrent.next == null) {
                // Если текущий Monom текущего объекта Polinom больше текущего Monom, который мы хотим добавить, и
                // следующий элемент текущего объекта Polinom не существует, то мы добавляем текущий Monom в конец
                // списка Monom текущего объекта Polinom
                thisCurrent.next = new Monom(current);
                thisCurrent = thisCurrent.next;
                current = current.next;
            } else if (current.degree > thisCurrent.next.degree) {
                // Если текущий Monom текущего объекта Polinom больше текущего Monom, который мы хотим добавить, и
                // следующий элемент текущего объекта Polinom имеет меньшую степень, чем текущий Monom, который мы
                // прибавляем, то вставляем этот Monom между thisCurrent и thisCurrent.next
                Monom newMonom = new Monom(current);
                newMonom.next = thisCurrent.next;
                thisCurrent.next = newMonom;
                current = current.next;
                thisCurrent = thisCurrent.next;
            } else if (current.degree == thisCurrent.next.degree) {
                double sum = current.coeff + thisCurrent.next.coeff;
                if (Math.abs(sum) < ZERO) { // == 0
                    thisCurrent.next = thisCurrent.next.next;
                } else {
                    thisCurrent.next.coeff = sum;
                }
                current = current.next;
            } else {
                thisCurrent = thisCurrent.next;
            }
        }
    }

    //умножить на полином
    public Polinom multiply(Polinom polinom) {
        // если один из полиномов пуст, то результат пуст
        if (head == null || polinom.head == null) {
            head = null;
            return this;
        }
        // создаем новый полином для результата умножения
        Polinom result = new Polinom();

        // создаем первый моном в результат
        result.head = new Monom(head.coeff * polinom.head.coeff, head.degree + polinom.head.degree);

        // начинаем итерироваться по мономам текущего полинома
        Monom current = head;

        while (current != null) {
            // итерируемся по мономам второго полинома
            Monom resultCurrent = result.head;
            Monom polinomCurrent = current == head ? polinom.head.next : polinom.head;

            while (polinomCurrent != null) {
                // определяем степень и коэффициент нового монома
                int degree = current.degree + polinomCurrent.degree;
                double coeff = current.coeff * polinomCurrent.coeff;

                if (resultCurrent.next == null) {
                    // если новый моном имеет максимальную степень в результирующем полиноме, то добавляем его в конец
                    resultCurrent.next = new Monom(coeff, degree);
                    resultCurrent = resultCurrent.next;
                    polinomCurrent = polinomCurrent.next;
                } else if (resultCurrent.next.degree == degree) {
                    // если новый моном имеет ту же степень, что и следующий моном в результирующем
                    // полиноме, то складываем коэффициенты
                    resultCurrent.next.coeff += coeff;
                    if (Math.abs(resultCurrent.next.coeff) < ZERO) {
                        resultCurrent.next = resultCurrent.next.next;
                    }
                    polinomCurrent = polinomCurrent.next;
                } else if (resultCurrent.next.degree < degree) {
                    // если новый моном имеет меньшую степень, чем следующий моном
                    // в результирующем полиноме, то вставляем его перед ним
                    Monom newMonom = new Monom(coeff, degree);
                    newMonom.next = resultCurrent.next;
                    resultCurrent.next = newMonom;
                    resultCurrent = resultCurrent.next;
                    polinomCurrent = polinomCurrent.next;
                } else {
                    // иначе переходим к следующему моному в результирующем полиноме
                    resultCurrent = resultCurrent.next;
                }
            }
            // переходим к следующему моному текущего полинома
            current = current.next;
        }
        // заменяем текущий полином на результирующий и возвращаем его
        head = result.head;
        return this;
    }

    //умножить на число
    public Polinom multiply(Double number) {
        // Если число близко к нулю, полином будет нулевым
        if (Math.abs(number) < ZERO) {
            head = null;
            return this;
        }
        // Если полином пустой, то его умножение на любое число также будет пустым полиномом
        if (head != null) {
            // Проходим по всем мономам полинома
            Monom current = head;
            do {
                current.coeff *= number; // Умножаем коэффициент монома на число
                current = current.next; // Переходим к следующему моному
            } while (current != null); // Выполняем пока не достигнем конца полинома
        }
        return this; // Возвращаем сам полином
    }

    //значение в точке
    public double value(double point) {
        if (head == null) return 0d; // Если полином пустой, то значение равно нулю

        double result = 0d; // Инициализируем переменную результата
        Monom current = head; // Устанавливаем текущий моном на первый моном полинома

        for (int i = head.degree; i >= 0; i--) { // Цикл по степеням полинома от максимальной до нуля
            // Если есть моном с такой степенью, то добавляем его в результат
            if (current != null && i == current.degree) {
                result += current.coeff;
                current = current.next; // Переходим к следующему моному
            }
            if (i != 0) { // Если степень не равна нулю, то умножаем результат на значение точки
                result *= point;
            }
        }
        return result;
    }

    //печать
    public void print() {
        if (head != null) {
            Monom current = head;
            do {
                current.print();
                System.out.print(current.next == null ? "" : " + ");
                current = current.next;
            } while (current != null);
            System.out.println();
        }
    }
}
