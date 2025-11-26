package oop;

/**
 * Абстрактний базовий клас, який описує загальні характеристики військовослужбовця.
 * Містить спільні атрибути: імʼя, звання та кількість років служби.
 * Реалізує інтерфейс {@link Payable}, який вимагає визначення методу розрахунку зарплати.
 *
 * <p>Класи-нащадки (наприклад, {@link Soldier} та {@link Officer})
 * повинні реалізувати абстрактний метод {@link #showInfo()},
 * а також можуть перевизначати інші поведінкові методи.</p>
 */
public abstract class Serviceman implements Payable {

    /** ПІБ військовослужбовця. */
    private String name;

    /** Військове звання (солдат, сержант, капітан тощо). */
    private String rank;

    /** Стаж служби у роках. */
    private int yearsOfService;

    /**
     * Конструктор за замовчуванням — створює узагальненого військовослужбовця
     * з типовими значеннями полів.
     */
    public Serviceman() {
        this.name = "Невідомий військовослужбовець";
        this.rank = "солдат";
        this.yearsOfService = 0;
    }

    /**
     * Конструктор з параметрами.
     *
     * @param name           ПІБ військовослужбовця
     * @param rank           військове звання
     * @param yearsOfService кількість років служби
     */
    public Serviceman(String name, String rank, int yearsOfService) {
        this.name = name;
        this.rank = rank;
        this.yearsOfService = yearsOfService;
    }

    /**
     * Повертає ПІБ військовослужбовця.
     *
     * @return імʼя та прізвище
     */
    public String getName() { return name; }

    /**
     * Задає нове імʼя військовослужбовця.
     *
     * @param name новий ПІБ
     */
    public void setName(String name) { this.name = name; }

    /**
     * Повертає звання військовослужбовця.
     *
     * @return військове звання
     */
    public String getRank() { return rank; }

    /**
     * Встановлює нове військове звання.
     *
     * @param rank нове звання
     */
    public void setRank(String rank) { this.rank = rank; }

    /**
     * Повертає кількість років служби.
     *
     * @return стаж служби у роках
     */
    public int getYearsOfService() { return yearsOfService; }

    /**
     * Встановлює новий стаж служби.
     *
     * @param yearsOfService кількість років служби
     */
    public void setYearsOfService(int yearsOfService) { this.yearsOfService = yearsOfService; }

    /**
     * Перевантажений метод, який дозволяє одночасно задати звання і стаж.
     *
     * @param rank            нове звання
     * @param yearsOfService  новий стаж служби
     */
    public void setRank(String rank, int yearsOfService) {
        this.rank = rank;
        this.yearsOfService = yearsOfService;
    }

    /**
     * Абстрактний метод, який повинен бути реалізований у класах-нащадках.
     * Використовується для виведення повної інформації про військовослужбовця.
     */
    public abstract void showInfo();
}
