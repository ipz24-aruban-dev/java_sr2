package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Панель "Про автора".
 * Відображає ПІБ, групу та фотографію студента, який виконав лабораторні роботи 8–11.
 * Використовується як окрема вкладка в головному вікні застосунку.
 */

public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        textPanel.add(new JLabel("Автор: Прізвище Ім'я По батькові"));
        textPanel.add(new JLabel("Група: ІПЗ-22008б"));
        textPanel.add(new JLabel("Варіант: 17"));
        textPanel.add(new JLabel("Лабораторні роботи 8–11 (Java)"));

        add(textPanel, BorderLayout.NORTH);

        JLabel photoLabel = new JLabel("Фото не завантажено");
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            ImageIcon icon = new ImageIcon("author.jpg");
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                photoLabel.setIcon(new ImageIcon(img));
                photoLabel.setText("");
            }
        } catch (Exception ignored) { }

        add(photoLabel, BorderLayout.CENTER);
    }
}
