package gui;

import oop.ServicemanArrayManagerV2;

import javax.swing.*;
import java.awt.*;

/**
 * Графічний інтерфейс для лабораторної роботи №9.
 * Розширює можливості ЛР8: сортування масиву за текстовим полем (ПІБ),
 * фільтрація за регулярним виразом та пошук елементів з мінімальним і максимальним стажем.
 */

public class Lab9Panel extends JPanel {

    private final ServicemanArrayManagerV2 manager = new ServicemanArrayManagerV2();
    private final JTextArea output = new JTextArea(20, 70);

    // для фільтрації за regex
    private final JTextField regexField = new JTextField(".*ко$", 15); // приклад: ПІБ закінчується на "ко"

    public Lab9Panel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Лабораторна робота 9 — Розширена робота з масивом об'єктів");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(title);

        // --- Панель кнопок загальних операцій ---
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnShowAll = new JButton("Показати всі елементи");
        JButton btnReinit = new JButton("Переініціалізувати масив");
        topButtons.add(btnShowAll);
        topButtons.add(btnReinit);

        // --- Сортування за текстовим полем name ---
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.setBorder(BorderFactory.createTitledBorder("Сортування за ПІБ (name)"));
        JButton btnSortNameAsc = new JButton("Сортувати за ПІБ ↑");
        JButton btnSortNameDesc = new JButton("Сортувати за ПІБ ↓");
        sortPanel.add(btnSortNameAsc);
        sortPanel.add(btnSortNameDesc);

        // --- Фільтрація за regex по name ---
        JPanel regexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        regexPanel.setBorder(BorderFactory.createTitledBorder("Фільтрація за регулярним виразом (ПІБ)"));
        regexPanel.add(new JLabel("Шаблон (regex):"));
        regexPanel.add(regexField);
        JButton btnFilterRegex = new JButton("Показати збіги");
        regexPanel.add(btnFilterRegex);

        JLabel hint = new JLabel("Наприклад: \".*ко$\" — прізвище закінчується на \"ко\"");
        hint.setFont(hint.getFont().deriveFont(Font.ITALIC, 11f));
        regexPanel.add(hint);

        // --- Мін/макс за стажем ---
        JPanel minMaxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        minMaxPanel.setBorder(BorderFactory.createTitledBorder("Мінімальний та максимальний стаж"));
        JButton btnMinMax = new JButton("Показати військових з найменшим та найбільшим стажем");
        minMaxPanel.add(btnMinMax);

        // --- Верхній блок (вертикально) ---
        JPanel topBlock = new JPanel();
        topBlock.setLayout(new BoxLayout(topBlock, BoxLayout.Y_AXIS));
        topBlock.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        sortPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        regexPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        minMaxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topBlock.add(titlePanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(topButtons);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(sortPanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(regexPanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(minMaxPanel);

        add(topBlock, BorderLayout.NORTH);

        // --- Текстовий вивід ---
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(output), BorderLayout.CENTER);

        // --- Обробники подій ---

        btnShowAll.addActionListener(e ->
                output.setText(manager.buildAllInfo())
        );

        btnReinit.addActionListener(e -> {
            manager.initDefault();
            output.setText("Масив переініціалізовано.\n\n" + manager.buildAllInfo());
        });

        btnSortNameAsc.addActionListener(e -> {
            manager.sortByNameAscending();
            output.setText("Масив відсортовано за ПІБ (за зростанням).\n\n" + manager.buildAllInfo());
        });

        btnSortNameDesc.addActionListener(e -> {
            manager.sortByNameDescending();
            output.setText("Масив відсортовано за ПІБ (за спаданням).\n\n" + manager.buildAllInfo());
        });

        btnFilterRegex.addActionListener(e -> {
            String regex = regexField.getText().trim();
            if (regex.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введіть регулярний вираз.", "Помилка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                output.setText(manager.buildFilteredByNameRegex(regex));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Невірний регулярний вираз:\n" + ex.getMessage(),
                        "Помилка regex",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnMinMax.addActionListener(e ->
                output.setText(manager.buildMinMaxYearsInfo())
        );

        // Початковий вивід
        output.setText(manager.buildAllInfo());
    }
}
