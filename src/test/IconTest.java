package test;

import util.IconUtil;
import javax.swing.*;

public class IconTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Icon");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        
        // Test từng icon
        JLabel lblAdd = new JLabel("Add", IconUtil.getAddIcon(), JLabel.CENTER);
        JLabel lblEdit = new JLabel("Edit", IconUtil.getEditIcon(), JLabel.CENTER);
        JLabel lblDelete = new JLabel("Delete", IconUtil.getDeleteIcon(), JLabel.CENTER);
        JLabel lblSearch = new JLabel("Search", IconUtil.getSearchIcon(), JLabel.CENTER);
        
        panel.add(lblAdd);
        panel.add(lblEdit);
        panel.add(lblDelete);
        panel.add(lblSearch);
        
        frame.add(panel);
        frame.setVisible(true);
        
        System.out.println("Test icons - Kiểm tra xem có hiển thị không?");
    }
}