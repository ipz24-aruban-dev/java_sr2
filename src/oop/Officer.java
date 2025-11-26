package oop;

/**
 * Клас {@code Officer} представляє офіцера — різновид військовослужбовця,
 * який має додатковий атрибут «посада» та перевизначену логіку
 * відображення інформації й розрахунку зарплати.
 *
 * <p>Є нащадком абстрактного класу {@link Serviceman} та реалізує
 * інтерфейс {@link Payable} через успадкування.</p>
 */
public class Officer extends Serviceman {

    /** Посада офіцера (наприклад, «командир роти», «начальник штабу»). */
    private String position;

    /**
     * Конструктор за замовчуванням.
     * Створює офіцера з типовими значеннями ПІБ, звання та посади.
     */
    public Officer() {
        super("Безіменний офіцер", "лейтенант", 1);
        this.position = "молодший командир";
    }

    /**
     * Параметризований конструктор для створення офіцера з усіма характеристиками.
     *
     * @param name           ПІБ офіцера
     * @param rank           військове звання
     * @param yearsOfService стаж служби у роках
     * @param position       посада офіцера
     */
    public Officer(String name, String rank, int yearsOfService, String position) {
        super(name, rank, yearsOfService);
        this.position = position;
    }

    /**
     * Повертає посаду офіцера.
     *
     * @return поточна посада
     */
    public String getPosition() { return position; }

    /**
     * Встановлює нову посаду офіцера.
     *
     * @param position нова посада
     */
    public void setPosition(String position) { this.position = position; }

    /**
     * Виводить на консоль повну інформацію про офіцера.
     * Перевизначає абстрактний метод {@link Serviceman#showInfo()}.
     */
    @Override
    public void showInfo() {
        System.out.println("Офіцер: " + getName() +
                ", звання: " + getRank() +
                ", років служби: " + getYearsOfService() +
                ", посада: " + position);
    }

    /**
     * Обчислює орієнтовну зарплату офіцера.
     * Зарплата вища, ніж у солдатів, і залежить від стажу служби.
     *
     * @return обчислена зарплата
     */
    @Override
    public double calculateSalary() {
        return 25000 + getYearsOfService() * 800;
    }

    /**
     * Формує текст наказу, який віддає офіцер.
     *
     * @param orderText текст наказу
     * @return сформований рядок у форматі:
     *         «Офіцер <ПІБ> віддає наказ: <текст>»
     */
    public String giveOrder(String orderText) {
        return "Офіцер " + getName() + " віддає наказ: " + orderText;
    }
}
