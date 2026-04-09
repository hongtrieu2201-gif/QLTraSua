package gui;

import bus.ToppingBUS;
import dto.ToppingDTO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ToppingGUI extends JPanel {
    private JTable tblTopping;
    private DefaultTableModel model;
    private JTextField txtMaTopping, txtTenTopping, txtGiaTopping, txtTim;
    private JComboBox<String> cboTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnTimKiem, btnLamMoi;
    
    private ToppingBUS toppingBUS = new ToppingBUS();
    private boolean isEditing = false;
    
    public ToppingGUI() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel tìm kiếm
        JPanel panelTim = createSearchPanel();
        add(panelTim, BorderLayout.NORTH);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        // Panel table
        JPanel panelTable = createTablePanel();
        splitPane.setTopComponent(panelTable);
        
        // Panel form
        JPanel panelForm = createFormPanel();
        splitPane.setBottomComponent(panelForm);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(Color.GRAY), "TÌM KIẾM"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        panel.add(new JLabel("Tìm kiếm:"));
        txtTim = new JTextField(20);
        btnTimKiem = new JButton("🔍 Tìm");
        btnLamMoi = new JButton("🔄 Làm mới");
        
        panel.add(txtTim);
        panel.add(btnTimKiem);
        panel.add(btnLamMoi);
        
        btnTimKiem.addActionListener(e -> timKiem());
        btnLamMoi.addActionListener(e -> {
            txtTim.setText("");
            loadData();
        });
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "DANH SÁCH TOPPING"));
        
        String[] columns = {"Mã topping", "Tên topping", "Giá", "Trạng thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblTopping = new JTable(model);
        tblTopping.setRowHeight(25);
        tblTopping.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblTopping.setSelectionBackground(new Color(173, 216, 230));
        
        tblTopping.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblTopping.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblTopping.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblTopping.getColumnModel().getColumn(3).setPreferredWidth(80);
        
        JScrollPane scroll = new JScrollPane(tblTopping);
        panel.add(scroll, BorderLayout.CENTER);
        
        tblTopping.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                loadFormFromTable();
            }
        });
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 248, 225));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(Color.GRAY), "THÔNG TIN TOPPING"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Mã topping
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Mã topping:"), gbc);
        gbc.gridx = 1;
        txtMaTopping = new JTextField(15);
        txtMaTopping.setEditable(false);
        panel.add(txtMaTopping, gbc);
        
        // Tên topping
        gbc.gridx = 2;
        panel.add(new JLabel("Tên topping:"), gbc);
        gbc.gridx = 3;
        txtTenTopping = new JTextField(15);
        panel.add(txtTenTopping, gbc);
        
        // Giá
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Giá topping:"), gbc);
        gbc.gridx = 1;
        txtGiaTopping = new JTextField(15);
        panel.add(txtGiaTopping, gbc);
        
        // Trạng thái
        gbc.gridx = 2;
        panel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 3;
        cboTrangThai = new JComboBox<>(new String[]{"Đang bán", "Ngừng bán"});
        panel.add(cboTrangThai, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(255, 248, 225));
        
        btnThem = createButton("➕ Thêm mới", new Color(60, 179, 113));
        btnSua = createButton("✏️ Cập nhật", new Color(70, 130, 180));
        btnXoa = createButton("🗑️ Ngừng bán", new Color(220, 20, 60));
        
        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        
        panel.add(buttonPanel, gbc);
        
        // Events
        btnThem.addActionListener(e -> themTopping());
        btnSua.addActionListener(e -> suaTopping());
        btnXoa.addActionListener(e -> xoaTopping());
        
        return panel;
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        return button;
    }
    
    private void loadData() {
        model.setRowCount(0);
        List<ToppingDTO> list = toppingBUS.getAllTopping();
        for (ToppingDTO top : list) {
            Object[] row = {
                top.getMaTopping(),
                top.getTenTopping(),
                String.format("%,.0f", top.getGiaTopping()),
                top.isTrangThai() ? "Đang bán" : "Ngừng bán"
            };
            model.addRow(row);
        }
    }
    
    private void loadFormFromTable() {
        int row = tblTopping.getSelectedRow();
        if (row != -1) {
            txtMaTopping.setText((String) model.getValueAt(row, 0));
            txtTenTopping.setText((String) model.getValueAt(row, 1));
            String gia = (String) model.getValueAt(row, 2);
            txtGiaTopping.setText(gia.replace(",", ""));
            cboTrangThai.setSelectedItem((String) model.getValueAt(row, 3));
            isEditing = true;
        }
    }
    
    private void clearForm() {
        txtMaTopping.setText("");
        txtTenTopping.setText("");
        txtGiaTopping.setText("");
        cboTrangThai.setSelectedIndex(0);
        isEditing = false;
    }
    
    private void themTopping() {
        ToppingDTO top = new ToppingDTO();
        top.setTenTopping(txtTenTopping.getText().trim());
        
        try {
            top.setGiaTopping(Double.parseDouble(txtGiaTopping.getText().trim()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá topping không hợp lệ!");
            return;
        }
        
        if (toppingBUS.addTopping(top)) {
            JOptionPane.showMessageDialog(this, "Thêm topping thành công!");
            loadData();
            clearForm();
        }
    }
    
    private void suaTopping() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn topping cần sửa!");
            return;
        }
        
        ToppingDTO top = new ToppingDTO();
        top.setMaTopping(txtMaTopping.getText());
        top.setTenTopping(txtTenTopping.getText().trim());
        top.setTrangThai(cboTrangThai.getSelectedItem().equals("Đang bán"));
        
        try {
            top.setGiaTopping(Double.parseDouble(txtGiaTopping.getText().trim()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá topping không hợp lệ!");
            return;
        }
        
        if (toppingBUS.updateTopping(top)) {
            JOptionPane.showMessageDialog(this, "Cập nhật topping thành công!");
            loadData();
            clearForm();
        }
    }
    
    private void xoaTopping() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn topping cần ngừng bán!");
            return;
        }
        
        if (toppingBUS.deleteTopping(txtMaTopping.getText())) {
            JOptionPane.showMessageDialog(this, "Đã ngừng bán topping!");
            loadData();
            clearForm();
        }
    }
    
    private void timKiem() {
        String keyword = txtTim.getText().trim();
        model.setRowCount(0);
        List<ToppingDTO> list = toppingBUS.searchTopping(keyword);
        for (ToppingDTO top : list) {
            Object[] row = {
                top.getMaTopping(),
                top.getTenTopping(),
                String.format("%,.0f", top.getGiaTopping()),
                top.isTrangThai() ? "Đang bán" : "Ngừng bán"
            };
            model.addRow(row);
        }
    }
}