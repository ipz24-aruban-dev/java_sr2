package gui;

import oop.ServicemanArrayManagerV3;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

/**
 * Графічний інтерфейс для лабораторної роботи №10.
 * Додає роботу з файлами для масиву військовослужбовців: зчитування з текстового файлу,
 * збереження відсортованих даних у бінарний файл та відновлення інформації з нього.
 */

public class Lab10Panel extends JPanel {

    private final ServicemanArrayManagerV3 manager = new ServicemanArrayManagerV3();
    private final JTextArea output = new JTextArea(20, 70);

    public Lab10Panel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Лабораторна робота 10 — Робота з файлами (масив \"Військовослужбовець\")");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(title);

        // --- Кнопки ---
        JButton btnLoadText   = new JButton("Зчитати з ТЕКСТОВОГО файлу...");
        JButton btnSaveBinary = new JButton("Записати в БІНАРНИЙ файл...");
        JButton btnLoadBinary = new JButton("Зчитати з БІНАРНОГО файлу...");
        JButton btnShowCurrent = new JButton("Показати поточний масив");

        // НОВА КНОПКА:
        JButton btnResetToDefault = new JButton("Скинути до початкового масиву");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.add(btnLoadText);
        buttonsPanel.add(btnSaveBinary);
        buttonsPanel.add(btnLoadBinary);
        buttonsPanel.add(btnShowCurrent);
        buttonsPanel.add(btnResetToDefault);   // додали сюди

        // --- Пояснення формату текстового файлу ---
        JTextArea help = new JTextArea(4, 70);
        help.setEditable(false);
        help.setLineWrap(true);
        help.setWrapStyleWord(true);
        help.setText(
                "Формат рядків у текстовому файлі:\n" +
                        "S;Іван Петренко;солдат;3;\n" +
                        "O;Олег Сидоренко;капітан;10;командир роти\n" +
                        "(S - солдат, O - офіцер; поля розділено крапкою з комою)."
        );

        JPanel topBlock = new JPanel();
        topBlock.setLayout(new BoxLayout(topBlock, BoxLayout.Y_AXIS));
        topBlock.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        help.setAlignmentX(Component.LEFT_ALIGNMENT);

        topBlock.add(titlePanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(buttonsPanel);
        topBlock.add(Box.createVerticalStrut(5));
        topBlock.add(help);

        add(topBlock, BorderLayout.NORTH);

        // --- Вихідна область ---
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(output), BorderLayout.CENTER);

        // --- Обробники кнопок ---

        btnShowCurrent.addActionListener(e ->
                output.setText(manager.buildAllInfo())
        );

        btnResetToDefault.addActionListener(e -> {
            manager.initDefault();
            output.setText("Масив скинуто до початкового стану (initDefault).\n\n"
                    + manager.buildAllInfo());
        });

        btnLoadText.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Виберіть ТЕКСТОВИЙ файл з даними (S;name;rank;years;[position])");
            int res = chooser.showOpenDialog(Lab10Panel.this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    manager.loadFromTextFile(file.toPath());
                    output.setText("Дані зчитано з текстового файлу:\n" +
                            file.getAbsolutePath() + "\n\n" +
                            manager.buildAllInfo());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Lab10Panel.this,
                            "Помилка зчитування текстового файлу:\n" + ex.getMessage(),
                            "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSaveBinary.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Оберіть, куди зберегти БІНАРНИЙ файл");
            int res = chooser.showSaveDialog(Lab10Panel.this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    Path path = file.toPath();
                    manager.saveToBinaryFile(path);
                    output.setText("Поточний масив збережено в бінарний файл:\n" +
                            file.getAbsolutePath() + "\n\n" +
                            manager.buildAllInfo());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Lab10Panel.this,
                            "Помилка запису бінарного файлу:\n" + ex.getMessage(),
                            "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnLoadBinary.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Виберіть БІНАРНИЙ файл для зчитування");
            int res = chooser.showOpenDialog(Lab10Panel.this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    manager.loadFromBinaryFile(file.toPath());
                    output.setText("Дані зчитано з бінарного файлу:\n" +
                            file.getAbsolutePath() + "\n\n" +
                            manager.buildAllInfo());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Lab10Panel.this,
                            "Помилка зчитування бінарного файлу:\n" + ex.getMessage(),
                            "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // початковий стан
        output.setText(manager.buildAllInfo());
    }
}
