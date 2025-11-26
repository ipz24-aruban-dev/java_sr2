package oop;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас {@code MilitaryUnit} моделює військовий підрозділ, який містить список
 * військовослужбовців ({@link Serviceman}). Надає можливість додавати нових
 * військових, формувати текстове представлення особового складу та
 * обчислювати загальну суму зарплат.
 *
 * <p>Клас демонструє приклад агрегації: підрозділ містить колекцію об'єктів
 * {@code Serviceman}, але не керує їх життєвим циклом за межами колекції.</p>
 */
public class MilitaryUnit {

    /** Назва підрозділу. */
    private String unitName;

    /** Список особового складу підрозділу. */
    private List<Serviceman> staff;

    /**
     * Конструктор за замовчуванням.
     * Створює підрозділ із типовою назвою та порожнім списком військових.
     */
    public MilitaryUnit() {
        this.unitName = "Безіменний підрозділ";
        this.staff = new ArrayList<>();
    }

    /**
     * Створює підрозділ із заданою назвою.
     *
     * @param unitName назва підрозділу
     */
    public MilitaryUnit(String unitName) {
        this.unitName = unitName;
        this.staff = new ArrayList<>();
    }

    /**
     * Повертає назву підрозділу.
     *
     * @return назва підрозділу
     */
    public String getUnitName() { return unitName; }

    /**
     * Встановлює нову назву підрозділу.
     *
     * @param unitName нова назва
     */
    public void setUnitName(String unitName) { this.unitName = unitName; }

    /**
     * Додає до складу підрозділу вже створеного військовослужбовця.
     *
     * @param s об'єкт {@link Serviceman}, який додається
     */
    public void addServiceman(Serviceman s) {
        staff.add(s);
    }

    /**
     * Створює нового солдата «на льоту» та додає його до підрозділу.
     *
     * @param name           ПІБ солдата
     * @param rank           звання
     * @param yearsOfService стаж служби у роках
     */
    public void addServiceman(String name, String rank, int yearsOfService) {
        staff.add(new Soldier(name, rank, yearsOfService));
    }

    /**
     * Формує текстовий опис підрозділу та його особового складу.
     *
     * @return текстовий звіт у форматі:
     * <pre>
     * Підрозділ: <назва>
     *   - <ПІБ>, <звання>, стаж: <років>
     *   ...
     * </pre>
     */
    public String buildUnitInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Підрозділ: ").append(unitName).append("\n");

        if (staff.isEmpty()) {
            sb.append("  Особовий склад відсутній.\n");
        } else {
            for (Serviceman s : staff) {
                sb.append("  - ")
                        .append(s.getName()).append(", ")
                        .append(s.getRank()).append(", стаж: ")
                        .append(s.getYearsOfService()).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Обчислює сумарну зарплату всього особового складу підрозділу.
     *
     * @return загальна зарплата
     */
    public double totalSalary() {
        double total = 0;
        for (Serviceman s : staff) {
            total += s.calculateSalary();
        }
        return total;
    }
}
