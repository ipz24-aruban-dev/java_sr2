package gui;

import javax.swing.*;

/**
 * Головне вікно застосунку для лабораторних робіт 8–11.
 * Містить вкладки для кожної лабораторної роботи та панель з інформацією про автора.
 * Слугує точкою входу для запуску графічного інтерфейсу.
 */

public class Labs8_11App extends JFrame {
    public Labs8_11App() {
        setTitle("Рубан Анатолій Лабораторні роботи 8–11 (Java)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabs.addTab("ЛР8", new Lab8Panel());
        tabs.addTab("ЛР9", new Lab9Panel());
        tabs.addTab("ЛР10", new Lab10Panel());
        tabs.addTab("ЛР11", new Lab11Panel());
        tabs.addTab("Про автора", new AboutPanel());

        add(tabs);
    }
}
