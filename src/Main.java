import gui.Labs8_11App;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Labs8_11App app = new Labs8_11App();
            app.setVisible(true);
        });
    }
}