package gui;

import db.DbServicemanRecord;
import db.ServicemanDbHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Графічний інтерфейс для лабораторної роботи №11.
 * Забезпечує роботу з простою базою даних SQLite, яка містить записи про військовослужбовців:
 * перегляд усіх записів, додавання, редагування, видалення та вибірка за умовою (ПІБ із певним суфіксом).
 */

public class Lab11Panel extends JPanel {

    private final ServicemanDbHelper dbHelper = new ServicemanDbHelper();

    private final JTextArea output = new JTextArea(18, 70);

    private final JTextField idField = new JTextField(4);
    private final JTextField typeField = new JTextField(2);      // S або O
    private final JTextField nameField = new JTextField(15);
    private final JTextField rankField = new JTextField(10);
    private final JTextField yearsField = new JTextField(5);
    private final JTextField positionField = new JTextField(15);

    private final JTextField suffixField = new JTextField("ко", 5);  // умова для вибірки

    public Lab11Panel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Лабораторна робота 11 — Робота з БД (\"Військовослужбовець\")");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(title);

        // --- Панель кнопок зверху ---
        JButton btnShowAll = new JButton("Переглянути всі записи");
        JButton btnAdd     = new JButton("Додати новий запис");
        JButton btnUpdate  = new JButton("Редагувати запис за ID");
        JButton btnDelete  = new JButton("Видалити запис за ID");
        JButton btnFilter  = new JButton("Показати, де ПІБ закінчується на...");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.add(btnShowAll);
        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnUpdate);
        buttonsPanel.add(btnDelete);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Вибірка за умовою (прізвище закінчується на...)"));
        filterPanel.add(new JLabel("Суфікс:"));
        filterPanel.add(suffixField);
        filterPanel.add(btnFilter);

        // --- Форма введення/редагування ---
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBorder(BorderFactory.createTitledBorder("Дані військовослужбовця"));

        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Тип (S/O):"));
        formPanel.add(typeField);
        formPanel.add(new JLabel("ПІБ:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Звання:"));
        formPanel.add(rankField);
        formPanel.add(new JLabel("Стаж (років):"));
        formPanel.add(yearsField);
        formPanel.add(new JLabel("Посада:"));
        formPanel.add(positionField);

        JPanel topBlock = new JPanel();
        topBlock.setLayout(new BoxLayout(topBlock, BoxLayout.Y_AXIS));
        topBlock.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topBlock.add(titlePanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(buttonsPanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(formPanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(filterPanel);

        add(topBlock, BorderLayout.NORTH);

        // --- Вивід ---
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(output), BorderLayout.CENTER);

        // --- Обробники подій ---

        btnShowAll.addActionListener(e -> refreshAll());

        btnAdd.addActionListener(e -> addRecord());

        btnUpdate.addActionListener(e -> updateRecord());

        btnDelete.addActionListener(e -> deleteRecord());

        btnFilter.addActionListener(e -> filterBySuffix());

        // Початковий вивід
        refreshAll();
    }

    private void refreshAll() {
        try {
            List<DbServicemanRecord> list = dbHelper.getAll();
            output.setText(formatList(list));
        } catch (SQLException ex) {
            showError("Помилка при зчитуванні БД: " + ex.getMessage());
        }
    }

    private void addRecord() {
        try {
            DbServicemanRecord rec = readRecordFromForm(false);
            long id = dbHelper.insert(rec);
            output.setText("Додано новий запис з ID = " + id + "\n\n");
            refreshAll();
        } catch (Exception ex) {
            showError("Помилка при додаванні: " + ex.getMessage());
        }
    }

    private void updateRecord() {
        try {
            DbServicemanRecord rec = readRecordFromForm(true);
            dbHelper.update(rec);
            output.setText("Запис з ID = " + rec.getId() + " оновлено.\n\n");
            refreshAll();
        } catch (Exception ex) {
            showError("Помилка при оновленні: " + ex.getMessage());
        }
    }

    private void deleteRecord() {
        try {
            long id = parseId();
            dbHelper.delete(id);
            output.setText("Запис з ID = " + id + " видалено.\n\n");
            refreshAll();
        } catch (Exception ex) {
            showError("Помилка при видаленні: " + ex.getMessage());
        }
    }

    private void filterBySuffix() {
        String suffix = suffixField.getText().trim();
        if (suffix.isEmpty()) {
            showError("Введіть суфікс для фільтрації (наприклад, 'ко').");
            return;
        }

        try {
            List<DbServicemanRecord> list = dbHelper.getByNameSuffix(suffix);
            output.setText("Записи, де ПІБ закінчується на \"" + suffix + "\":\n\n"
                    + formatList(list));
        } catch (SQLException ex) {
            showError("Помилка при фільтрації: " + ex.getMessage());
        }
    }

    private long parseId() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            throw new IllegalArgumentException("Поле ID не заповнене.");
        }
        return Long.parseLong(idText);
    }

    /**
     * @param requireId якщо true — чекаємо, що ID заповнено (для update)
     */
    private DbServicemanRecord readRecordFromForm(boolean requireId) {
        long id = 0;
        if (requireId) {
            id = parseId();
        }

        String type = typeField.getText().trim().toUpperCase();
        if (!(type.equals("S") || type.equals("O"))) {
            throw new IllegalArgumentException("Тип має бути 'S' (солдат) або 'O' (офіцер).");
        }

        String name = nameField.getText().trim();
        String rank = rankField.getText().trim();
        String yearsText = yearsField.getText().trim();
        String position = positionField.getText().trim();

        if (name.isEmpty() || rank.isEmpty() || yearsText.isEmpty()) {
            throw new IllegalArgumentException("ПІБ, звання та стаж мають бути заповнені.");
        }

        int years = Integer.parseInt(yearsText);

        DbServicemanRecord rec = new DbServicemanRecord(type, name, rank, years, position);
        if (requireId) {
            rec.setId(id);
        }
        return rec;
    }

    private String formatList(List<DbServicemanRecord> list) {
        if (list.isEmpty()) {
            return "Немає записів.\n";
        }
        StringBuilder sb = new StringBuilder();
        for (DbServicemanRecord r : list) {
            sb.append("[ID=").append(r.getId()).append("] ")
                    .append(r.getType().equals("O") ? "Офіцер" : "Солдат").append(": ")
                    .append(r.getName())
                    .append(", звання: ").append(r.getRank())
                    .append(", стаж: ").append(r.getYears()).append(" років");
            if (r.getType().equals("O") && r.getPosition() != null && !r.getPosition().isBlank()) {
                sb.append(", посада: ").append(r.getPosition());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Помилка", JOptionPane.ERROR_MESSAGE);
    }
}
