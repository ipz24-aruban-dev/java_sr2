package gui;

import oop.ServicemanArrayManager;

import javax.swing.*;
import java.awt.*;

/**
 * Графічний інтерфейс для лабораторної роботи №8.
 * Демонструє роботу з масивом об'єктів "Військовослужбовець": ініціалізація, редагування,
 * фільтрація та сортування за числовим полем (стаж служби).
 */

public class Lab8Panel extends JPanel {

    private final ServicemanArrayManager manager = new ServicemanArrayManager();

    private final JTextArea output = new JTextArea(20, 70);

    private final JTextField indexField = new JTextField(3);
    private final JTextField nameField = new JTextField(10);
    private final JTextField rankField = new JTextField(10);
    private final JTextField yearsField = new JTextField(5);
    private final JTextField positionField = new JTextField(12);

    private final JTextField filterYearsField = new JTextField("5", 4);

    public Lab8Panel() {
        setLayout(new BorderLayout());

        // ---- Заголовок ----
        JLabel title = new JLabel("Лабораторна робота 8 — Масив об'єктів \"Військовослужбовець\"");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(title);

        // ---- Кнопки роботи з масивом ----
        JPanel arrayButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton initButton = new JButton("Переініціалізувати масив");
        JButton showAllButton = new JButton("Показати всі елементи");
        arrayButtonsPanel.add(initButton);
        arrayButtonsPanel.add(showAllButton);

        // ---- Модифікація елементів ----
        JPanel modifyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modifyPanel.setBorder(BorderFactory.createTitledBorder("Модифікація елемента масиву"));

        modifyPanel.add(new JLabel("Індекс:"));
        modifyPanel.add(indexField);
        modifyPanel.add(new JLabel("Ім'я:"));
        modifyPanel.add(nameField);
        modifyPanel.add(new JLabel("Звання:"));
        modifyPanel.add(rankField);
        modifyPanel.add(new JLabel("Стаж (років):"));
        modifyPanel.add(yearsField);
        modifyPanel.add(new JLabel("Посада (для офіцера):"));
        modifyPanel.add(positionField);

        JButton modifyButton = new JButton("Змінити елемент");
        modifyPanel.add(modifyButton);

        // ---- Фільтрація та сортування ----
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterSortPanel.setBorder(BorderFactory.createTitledBorder("Фільтрація та сортування"));

        JButton filterButton = new JButton("Показати зі стажем ≥");
        JButton sortAscButton = new JButton("Сортувати за стажем ↑");
        JButton sortDescButton = new JButton("Сортувати за стажем ↓");

        filterSortPanel.add(filterButton);
        filterSortPanel.add(filterYearsField);
        filterSortPanel.add(new JLabel("років"));
        filterSortPanel.add(sortAscButton);
        filterSortPanel.add(sortDescButton);

        // ---- Верхній блок (заголовок + усі контролі вертикально) ----
        JPanel topBlock = new JPanel();
        topBlock.setLayout(new BoxLayout(topBlock, BoxLayout.Y_AXIS));
        topBlock.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        arrayButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        modifyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterSortPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topBlock.add(titlePanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(arrayButtonsPanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(modifyPanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(filterSortPanel);

        add(topBlock, BorderLayout.NORTH);

        // ---- Вихідний текст ----
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(output), BorderLayout.CENTER);

        // ---- Обробники подій ----
        initButton.addActionListener(e -> {
            manager.initDefault();
            output.setText("Масив переініціалізовано.\n\n" + manager.buildAllInfo());
        });

        showAllButton.addActionListener(e -> {
            output.setText(manager.buildAllInfo());
        });

        modifyButton.addActionListener(e -> modifyElement());

        filterButton.addActionListener(e -> filterByYears());

        sortAscButton.addActionListener(e -> {
            manager.sortByYearsAscending();
            output.setText("Масив відсортовано за стажем за зростанням.\n\n" + manager.buildAllInfo());
        });

        sortDescButton.addActionListener(e -> {
            manager.sortByYearsDescending();
            output.setText("Масив відсортовано за стажем за спаданням.\n\n" + manager.buildAllInfo());
        });

        // Початковий вивід
        output.setText(manager.buildAllInfo());
    }

    private void modifyElement() {
        try {
            int index = Integer.parseInt(indexField.getText().trim());
            String name = nameField.getText().trim();
            String rank = rankField.getText().trim();
            String yearsText = yearsField.getText().trim();
            Integer years = yearsText.isEmpty() ? null : Integer.parseInt(yearsText);
            String position = positionField.getText().trim();

            manager.modifyServiceman(index, emptyToNull(name), emptyToNull(rank), years, emptyToNull(position));

            output.setText("Елемент з індексом " + index + " змінено.\n\n" + manager.buildAllInfo());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Помилка введення числа (індекс або стаж).", "Помилка", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterByYears() {
        try {
            int minYears = Integer.parseInt(filterYearsField.getText().trim());
            output.setText(manager.buildFilteredByMinYears(minYears));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Введіть ціле число для стажу.", "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
