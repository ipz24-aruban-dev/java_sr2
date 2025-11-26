package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Допоміжний клас {@code ServicemanDbHelper} інкапсулює роботу з
 * базою даних SQLite, яка зберігає інформацію про військовослужбовців.
 *
 * <p>Відповідає за:</p>
 * <ul>
 *     <li>створення таблиці та початкове заповнення даними (ініціалізація БД);</li>
 *     <li>отримання всіх записів;</li>
 *     <li>фільтрацію за суфіксом ПІБ;</li>
 *     <li>додавання нового запису;</li>
 *     <li>оновлення існуючого запису;</li>
 *     <li>видалення запису;</li>
 *     <li>перетворення рядків {@link ResultSet} на об’єкти {@link DbServicemanRecord}.</li>
 * </ul>
 *
 * <p>База даних зберігається у файлі {@code servicemen.db} у корені проєкту.</p>
 */
public class ServicemanDbHelper {

    /**
     * Рядок підключення до SQLite-бази даних.
     * Файл БД {@code servicemen.db} створюється в кореневій директорії проєкту,
     * якщо він ще не існує.
     */
    private static final String DB_URL = "jdbc:sqlite:servicemen.db";

    /**
     * Конструктор за замовчуванням.
     * Під час створення екземпляра автоматично викликає {@link #initDatabase()},
     * щоб створити таблицю (якщо її ще немає) та заповнити початковими даними.
     */
    public ServicemanDbHelper() {
        initDatabase();
    }

    /**
     * Створює та повертає нове з'єднання з базою даних.
     *
     * @return активне {@link Connection} з SQLite-базою
     * @throws SQLException якщо не вдалося встановити з'єднання
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Ініціалізує структуру бази даних:
     * <ul>
     *     <li>створює таблицю {@code servicemen}, якщо вона ще не існує;</li>
     *     <li>перевіряє, чи є в таблиці записи;</li>
     *     <li>якщо таблиця порожня, додає кілька початкових записів.</li>
     * </ul>
     *
     * <p>У разі помилки виводить стек викликів у консоль.</p>
     */
    public void initDatabase() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
                    CREATE TABLE IF NOT EXISTS servicemen (
                        id       INTEGER PRIMARY KEY AUTOINCREMENT,
                        type     TEXT    NOT NULL,   -- 'S' або 'O'
                        name     TEXT    NOT NULL,
                        rank     TEXT    NOT NULL,
                        years    INTEGER NOT NULL,
                        position TEXT
                    )
                    """);

            // Перевіряємо, чи є дані
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM servicemen")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // Якщо порожньо — додамо кілька записів
                    st.executeUpdate("""
                        INSERT INTO servicemen(type, name, rank, years, position) VALUES
                        ('S', 'Іван Петренко', 'солдат', 1, ''),
                        ('S', 'Петро Іваненко', 'старший солдат', 3, ''),
                        ('O', 'Олег Сидоренко', 'капітан', 10, 'командир роти'),
                        ('O', 'Ірина Романюк', 'підполковник', 15, 'начальник штабу'),
                        ('S', 'Юрій Кравченко', 'солдат', 0, '')
                        """);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Повертає список усіх записів про військовослужбовців із таблиці {@code servicemen},
     * відсортований за зростанням ідентифікатора.
     *
     * @return список об’єктів {@link DbServicemanRecord}
     * @throws SQLException якщо сталася помилка під час виконання запиту
     */
    public List<DbServicemanRecord> getAll() throws SQLException {
        List<DbServicemanRecord> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id, type, name, rank, years, position FROM servicemen ORDER BY id");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }

        return list;
    }

    /**
     * Повертає список військовослужбовців, чиє ім’я/ПІБ закінчується
     * на вказаний суфікс (наприклад, «ко»).
     *
     * @param suffix суфікс, на який повинне закінчуватися поле {@code name}
     * @return список записів, які задовольняють умову
     * @throws SQLException якщо сталася помилка при виконанні SQL-запиту
     */
    public List<DbServicemanRecord> getByNameSuffix(String suffix) throws SQLException {
        List<DbServicemanRecord> list = new ArrayList<>();

        String sql = "SELECT id, type, name, rank, years, position " +
                "FROM servicemen WHERE name LIKE ? ORDER BY name";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // name LIKE '%suffix' — прізвище/ПІБ закінчується на suffix
            ps.setString(1, "%" + suffix);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }

        return list;
    }

    /**
     * Додає новий запис про військовослужбовця до таблиці {@code servicemen}.
     *
     * @param rec об’єкт {@link DbServicemanRecord} з даними для вставки.
     *            Поле {@code id} може бути нульовим — значення генерується автоматично.
     * @return згенерований ідентифікатор нового запису або 0, якщо його не вдалося отримати
     * @throws SQLException якщо сталася помилка при виконанні INSERT-запиту
     */
    public long insert(DbServicemanRecord rec) throws SQLException {
        String sql = """
                INSERT INTO servicemen(type, name, rank, years, position)
                VALUES(?,?,?,?,?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, rec.getType());
            ps.setString(2, rec.getName());
            ps.setString(3, rec.getRank());
            ps.setInt(4, rec.getYears());
            ps.setString(5, rec.getPosition());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    rec.setId(id);
                    return id;
                }
            }
        }

        return 0;
    }

    /**
     * Оновлює існуючий запис у таблиці {@code servicemen} згідно з даними
     * об’єкта {@link DbServicemanRecord}. Ідентифікатор береться з поля {@code id}.
     *
     * @param rec запис з оновленими даними, який містить коректний {@code id}
     * @throws SQLException якщо сталася помилка під час виконання UPDATE-запиту
     */
    public void update(DbServicemanRecord rec) throws SQLException {
        String sql = """
                UPDATE servicemen
                SET type=?, name=?, rank=?, years=?, position=?
                WHERE id=?
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rec.getType());
            ps.setString(2, rec.getName());
            ps.setString(3, rec.getRank());
            ps.setInt(4, rec.getYears());
            ps.setString(5, rec.getPosition());
            ps.setLong(6, rec.getId());

            ps.executeUpdate();
        }
    }

    /**
     * Видаляє запис про військовослужбовця з таблиці {@code servicemen}
     * за вказаним ідентифікатором.
     *
     * @param id ідентифікатор запису, який потрібно видалити
     * @throws SQLException якщо сталася помилка при виконанні DELETE-запиту
     */
    public void delete(long id) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM servicemen WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Перетворює поточний рядок обʼєкта {@link ResultSet} у
     * екземпляр {@link DbServicemanRecord}.
     *
     * @param rs результат запиту, позиція курсора якого вказує на потрібний рядок
     * @return об’єкт {@link DbServicemanRecord}, заповнений даними з поточного рядка
     * @throws SQLException якщо сталася помилка при читанні значень зі стовпців
     */
    private DbServicemanRecord mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String type = rs.getString("type");
        String name = rs.getString("name");
        String rank = rs.getString("rank");
        int years = rs.getInt("years");
        String position = rs.getString("position");
        return new DbServicemanRecord(id, type, name, rank, years, position);
    }
}
