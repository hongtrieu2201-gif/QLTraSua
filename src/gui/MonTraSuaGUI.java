package gui;

import bus.MonTraSuaBUS;
import dto.MonTraSuaDTO;
import dto.GiaMonTheoSizeDTO;
import dto.SizeDTO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonTraSuaGUI extends JPanel {
    private JTable tblMon;
    private DefaultTableModel model;
    private JTextField txtMaMon, txtTenMon, txtTim;
    private JComboBox<String> cboLoaiMon, cboTrangThai, cboLocLoai;
    private JButton btnThem, btnSua, btnXoa, btnTimKiem, btnLamMoi, btnLoc, btnQuanLyGia;
    
    private MonTraSuaBUS monBUS = new MonTraSuaBUS();
    private boolean isEditing = false;
    
    public MonTraSuaGUI() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel trên - Tìm kiếm và lọc
        JPanel panelTop = createTopPanel();
        add(panelTop, BorderLayout.NORTH);
        
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
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Panel tìm kiếm
        JPanel panelTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTim.add(new JLabel("Tìm kiếm:"));
        txtTim = new JTextField(20);
        btnTimKiem = new JButton("🔍 Tìm");
        panelTim.add(txtTim);
        panelTim.add(btnTimKiem);
        
        // Panel lọc
        JPanel panelLoc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLoc.add(new JLabel("Lọc theo loại:"));
        cboLocLoai = new JComboBox<>(new String[]{"Tất cả", "Trà sữa", "Trà trái cây", "Sữa", "Khác"});
        btnLoc = new JButton("📌 Lọc");
        panelLoc.add(cboLocLoai);
        panelLoc.add(btnLoc);
        
        btnLamMoi = new JButton("🔄 Làm mới");
        panelLoc.add(btnLamMoi);
        
        btnQuanLyGia = new JButton("💰 Quản lý giá theo size");
        btnQuanLyGia.setBackground(new Color(100, 149, 237));
        btnQuanLyGia.setForeground(Color.WHITE);
        panelLoc.add(btnQuanLyGia);
        
        panel.add(panelTim, BorderLayout.WEST);
        panel.add(panelLoc, BorderLayout.EAST);
        
        btnTimKiem.addActionListener(e -> timKiem());
        btnLoc.addActionListener(e -> locTheoLoai());
        btnLamMoi.addActionListener(e -> {
            txtTim.setText("");
            cboLocLoai.setSelectedIndex(0);
            loadData();
        });
        btnQuanLyGia.addActionListener(e -> moQuanLyGia());
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "DANH SÁCH MÓN TRÀ SỮA"));
        
        String[] columns = {"Mã món", "Tên món", "Loại món", "Trạng thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblMon = new JTable(model);
        tblMon.setRowHeight(25);
        tblMon.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblMon.setSelectionBackground(new Color(173, 216, 230));
        
        tblMon.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblMon.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblMon.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblMon.getColumnModel().getColumn(3).setPreferredWidth(80);
        
        JScrollPane scroll = new JScrollPane(tblMon);
        panel.add(scroll, BorderLayout.CENTER);
        
        tblMon.addMouseListener(new MouseAdapter() {
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
            new TitledBorder(new LineBorder(Color.GRAY), "THÔNG TIN MÓN"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Mã món
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Mã món:"), gbc);
        gbc.gridx = 1;
        txtMaMon = new JTextField(15);
        txtMaMon.setEditable(false);
        txtMaMon.setBackground(new Color(240, 240, 240));
        panel.add(txtMaMon, gbc);
        
        // Tên món
        gbc.gridx = 2;
        panel.add(new JLabel("Tên món:"), gbc);
        gbc.gridx = 3;
        txtTenMon = new JTextField(15);
        panel.add(txtTenMon, gbc);
        
        // Loại món
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Loại món:"), gbc);
        gbc.gridx = 1;
        cboLoaiMon = new JComboBox<>(new String[]{"Trà sữa", "Trà trái cây", "Sữa", "Khác"});
        panel.add(cboLoaiMon, gbc);
        
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
        btnThem.addActionListener(e -> themMon());
        btnSua.addActionListener(e -> suaMon());
        btnXoa.addActionListener(e -> xoaMon());
        
        return panel;
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void loadData() {
        model.setRowCount(0);
        List<MonTraSuaDTO> list = monBUS.getAllMon();
        if (list != null) {
            for (MonTraSuaDTO mon : list) {
                Object[] row = {
                    mon.getMaMon(),
                    mon.getTenMon(),
                    mon.getLoaiMon(),
                    mon.isTrangThai() ? "Đang bán" : "Ngừng bán"
                };
                model.addRow(row);
            }
        }
    }
    
    private void loadFormFromTable() {
        int row = tblMon.getSelectedRow();
        if (row != -1) {
            txtMaMon.setText((String) model.getValueAt(row, 0));
            txtTenMon.setText((String) model.getValueAt(row, 1));
            cboLoaiMon.setSelectedItem((String) model.getValueAt(row, 2));
            cboTrangThai.setSelectedItem((String) model.getValueAt(row, 3));
            isEditing = true;
        }
    }
    
    private void clearForm() {
        txtMaMon.setText("");
        txtTenMon.setText("");
        cboLoaiMon.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        isEditing = false;
    }
    
    private void themMon() {
        String tenMon = txtTenMon.getText().trim();
        
        if (tenMon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên món không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        MonTraSuaDTO mon = new MonTraSuaDTO();
        mon.setTenMon(tenMon);
        mon.setLoaiMon((String) cboLoaiMon.getSelectedItem());
        mon.setTrangThai(true);
        
        if (monBUS.addMon(mon)) {
            JOptionPane.showMessageDialog(this, "Thêm món thành công!\nVui lòng cập nhật giá cho các size.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
            
            // Mở dialog quản lý giá sau khi thêm món
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn cập nhật giá cho các size ngay bây giờ?", "Cập nhật giá", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                moQuanLyGiaChoMon(mon.getMaMon());
            }
        }
    }
    
    private void suaMon() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String tenMon = txtTenMon.getText().trim();
        if (tenMon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên món không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        MonTraSuaDTO mon = new MonTraSuaDTO();
        mon.setMaMon(txtMaMon.getText());
        mon.setTenMon(tenMon);
        mon.setLoaiMon((String) cboLoaiMon.getSelectedItem());
        mon.setTrangThai(cboTrangThai.getSelectedItem().equals("Đang bán"));
        
        if (monBUS.updateMon(mon)) {
            JOptionPane.showMessageDialog(this, "Cập nhật món thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        }
    }
    
    private void xoaMon() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần ngừng bán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn ngừng bán món " + txtMaMon.getText() + " - " + txtTenMon.getText() + "?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (monBUS.deleteMon(txtMaMon.getText())) {
                JOptionPane.showMessageDialog(this, "Đã ngừng bán món!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearForm();
            }
        }
    }
    
    private void timKiem() {
        String keyword = txtTim.getText().trim();
        model.setRowCount(0);
        
        List<MonTraSuaDTO> list = monBUS.searchMon(keyword);
        if (list != null) {
            for (MonTraSuaDTO mon : list) {
                Object[] row = {
                    mon.getMaMon(),
                    mon.getTenMon(),
                    mon.getLoaiMon(),
                    mon.isTrangThai() ? "Đang bán" : "Ngừng bán"
                };
                model.addRow(row);
            }
        }
        
        if (model.getRowCount() == 0 && !keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy món nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void locTheoLoai() {
        String loai = (String) cboLocLoai.getSelectedItem();
        model.setRowCount(0);
        
        List<MonTraSuaDTO> allMon = monBUS.getAllMon();
        if (allMon != null) {
            for (MonTraSuaDTO mon : allMon) {
                if (loai.equals("Tất cả") || mon.getLoaiMon().equals(loai)) {
                    Object[] row = {
                        mon.getMaMon(),
                        mon.getTenMon(),
                        mon.getLoaiMon(),
                        mon.isTrangThai() ? "Đang bán" : "Ngừng bán"
                    };
                    model.addRow(row);
                }
            }
        }
    }
    
    private void moQuanLyGia() {
        int row = tblMon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần quản lý giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String maMon = (String) model.getValueAt(row, 0);
        moQuanLyGiaChoMon(maMon);
    }
    
    private void moQuanLyGiaChoMon(String maMon) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Quản lý giá theo size - " + maMon, true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Size", "Giá bán (VNĐ)"};
        DefaultTableModel priceModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        
        JTable priceTable = new JTable(priceModel);
        priceTable.setRowHeight(30);
        
        // Lấy danh sách size
        List<SizeDTO> sizeList = monBUS.getAllSizes();
        List<GiaMonTheoSizeDTO> giaList = monBUS.getAllGiaTheoSize();
        
        // Tạo map giá
        Map<String, Double> giaMap = new HashMap<>();
        if (giaList != null) {
            for (GiaMonTheoSizeDTO gia : giaList) {
                if (gia.getMaMon().equals(maMon)) {
                    giaMap.put(gia.getMaSize(), gia.getGiaBan());
                }
            }
        }
        
        // Hiển thị các size và giá
        for (SizeDTO size : sizeList) {
            Double gia = giaMap.get(size.getMaSize());
            Object[] row = {
                size.getTenSize() + " (" + size.getMaSize() + ")",
                gia != null ? String.format("%,.0f", gia) : "0"
            };
            priceModel.addRow(row);
        }
        
        JScrollPane scroll = new JScrollPane(priceTable);
        panel.add(scroll, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnSave = new JButton("💾 Lưu giá");
        btnSave.setBackground(new Color(60, 179, 113));
        btnSave.setForeground(Color.WHITE);
        
        JButton btnCancel = new JButton("❌ Hủy");
        btnCancel.setBackground(new Color(128, 128, 128));
        btnCancel.setForeground(Color.WHITE);
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        btnSave.addActionListener(e -> {
            for (int i = 0; i < sizeList.size(); i++) {
                String giaStr = priceModel.getValueAt(i, 1).toString().replace(",", "");
                try {
                    double gia = Double.parseDouble(giaStr);
                    if (gia > 0) {
                        monBUS.saveGiaTheoSize(maMon, sizeList.get(i).getMaSize(), gia);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Giá không hợp lệ cho size " + sizeList.get(i).getMaSize(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            JOptionPane.showMessageDialog(dialog, "Lưu giá thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}