package main;

import gui.LoginGUI;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Main {
    public static void main(String[] args) {
        // Set Look and Feel về Nimbus (đẹp và nút rõ)
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // TẮT HIỆU ỨNG MỜ CHO NÚT
        UIManager.put("Button.opaque", true);
        UIManager.put("Button.contentAreaFilled", true);
        
        // Chạy chương trình
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}