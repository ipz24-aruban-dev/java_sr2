package oop;

/**
 * Клас {@code ServicemanArrayManager} відповідає за керування масивом
 * військовослужбовців ({@link Serviceman}). Він забезпечує:
 * <ul>
 *     <li>ініціалізацію масиву типовими даними;</li>
 *     <li>отримання та зміну масиву;</li>
 *     <li>модифікацію окремого елемента масиву;</li>
 *     <li>сортування за стажем служби (зростання/спадання);</li>
 *     <li>формування текстових звітів для GUI або консолі;</li>
 *     <li>фільтрацію особового складу за мінімальним стажем.</li>
 * </ul>
 *
 * <p>Цей клас використовується у ЛР8 для демонстрації роботи з масивами об'єктів.</p>
 */
public class ServicemanArrayManager {

    /** Масив військовослужбовців, яким керує менеджер. */
    protected Serviceman[] staff;

    /**
     * Конструктор за замовчуванням.
     * Ініціалізує масив стандартними даними, викликаючи {@link #initDefault()}.
     */
    public ServicemanArrayManager() {
        initDefault();
    }

    /**
     * Ініціалізує масив набором попередньо визначених військовослужбовців.
     * Використовується для демонстрації роботи масиву.
     */
    public void initDefault() {
        staff = new Serviceman[] {
                new Soldier("Іван Петренко", "солдат", 1),
                new Soldier("Петро Іваненко", "старший солдат", 3),
                new Officer("Олег Сидоренко", "лейтенант", 2, "командир взводу"),
                new Officer("Марія Шевченко", "капітан", 8, "командир роти"),
                new Soldier("Андрій Коваль", "солдат", 4),
                new Soldier("Сергій Мельник", "старший солдат", 6),
                new Officer("Віталій Гуменюк", "майор", 12, "заступник командира батальйону"),
                new Soldier("Дмитро Сидорук", "солдат", 2),
                new Officer("Ірина Романюк", "підполковник", 15, "начальник штабу"),
                new Soldier("Юрій Кравченко", "солдат", 0)
        };
    }

    /**
     * Повертає поточний масив військовослужбовців.
     *
     * @return масив {@link Serviceman}
     */
    public Serviceman[] getStaff() {
        return staff;
    }

    /**
     * Встановлює новий масив військовослужбовців.
     *
     * @param staff масив, який буде використано менеджером
     */
    public void setStaff(Serviceman[] staff) {
        this.staff = staff;
    }

    /**
     * Повертає кількість елементів у масиві.
     *
     * @return розмір масиву або 0, якщо масив не ініціалізовано
     */
    public int getSize() {
        return staff == null ? 0 : staff.length;
    }

    /**
     * Модифікує дані військовослужбовця за індексом.
     *
     * @param index             індекс елемента масиву
     * @param name              нове ім'я (або null, якщо не змінювати)
     * @param rank              нове звання (або null, якщо не змінювати)
     * @param years             новий стаж служби (або null, якщо не змінювати)
     * @param positionIfOfficer нова посада, якщо військовий є офіцером (або null)
     *
     * @throws IllegalStateException      якщо масив не ініціалізовано
     * @throws IndexOutOfBoundsException якщо індекс некоректний
     */
    public void modifyServiceman(int index, String name, String rank, Integer years, String positionIfOfficer) {
        if (staff == null) throw new IllegalStateException("Масив не ініціалізовано");
        if (index < 0 || index >= staff.length)
            throw new IndexOutOfBoundsException("Невірний індекс: " + index);

        Serviceman s = staff[index];

        if (name != null && !name.isBlank()) {
            s.setName(name);
        }
        if (rank != null && !rank.isBlank()) {
            s.setRank(rank);
        }
        if (years != null) {
            s.setYearsOfService(years);
        }
        if (s instanceof Officer o && positionIfOfficer != null && !positionIfOfficer.isBlank()) {
            o.setPosition(positionIfOfficer);
        }
    }

    /**
     * Сортує масив військовослужбовців за стажем служби у порядку зростання.
     */
    public void sortByYearsAscending() {
        java.util.Arrays.sort(staff, java.util.Comparator.comparingInt(Serviceman::getYearsOfService));
    }

    /**
     * Сортує масив військовослужбовців за стажем служби у порядку спадання.
     */
    public void sortByYearsDescending() {
        java.util.Arrays.sort(staff, java.util.Comparator.comparingInt(Serviceman::getYearsOfService).reversed());
    }

    /**
     * Будує повний текстовий опис масиву військовослужбовців.
     *
     * @return текст із переліком усіх елементів
     */
    public String buildAllInfo() {
        if (staff == null || staff.length == 0) return "Масив порожній.";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < staff.length; i++) {
            Serviceman s = staff[i];
            sb.append("[").append(i).append("] ");
            if (s instanceof Officer o) {
                sb.append("Офіцер: ")
                        .append(o.getName())
                        .append(", звання: ").append(o.getRank())
                        .append(", стаж: ").append(o.getYearsOfService())
                        .append(" років, посада: ").append(o.getPosition());
            } else {
                sb.append("Солдат: ")
                        .append(s.getName())
                        .append(", звання: ").append(s.getRank())
                        .append(", стаж: ").append(s.getYearsOfService())
                        .append(" років");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Будує текстовий звіт про військовослужбовців, стаж яких
     * більший або дорівнює вказаному значенню.
     *
     * @param minYears мінімальний стаж для фільтрації
     * @return список військових, які задовольняють умову, або повідомлення про їх відсутність
     */
    public String buildFilteredByMinYears(int minYears) {
        if (staff == null || staff.length == 0) return "Масив порожній.";

        StringBuilder sb = new StringBuilder();
        sb.append("Військовослужбовці зі стажем ≥ ").append(minYears).append(" років:\n");
        boolean any = false;

        for (int i = 0; i < staff.length; i++) {
            Serviceman s = staff[i];
            if (s.getYearsOfService() >= minYears) {
                any = true;
                sb.append("[").append(i).append("] ")
                        .append(s.getName())
                        .append(", ").append(s.getRank())
                        .append(", стаж: ").append(s.getYearsOfService())
                        .append(" років\n");
            }
        }

        if (!any) {
            sb.append("  Немає військовослужбовців, які задовольняють умову.\n");
        }

        return sb.toString();
    }
}
