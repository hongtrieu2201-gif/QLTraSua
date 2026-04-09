package gui;

import bus.KhachHangBUS;
import dto.KhachHangDTO;
import util.GenerateID;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class KhachHangGUI extends JPanel {
    
    private JTextField txtMaKH, txtHoTen, txtSoDienThoai, txtDiem, txtTimKiem;
    private JTable tblKhachHang;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    
    private KhachHangBUS khachHangBUS = new KhachHangBUS();
    private boolean isEditing = false;
    
    public KhachHangGUI() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 248, 255));
        
        // Panel thông tin khách hàng
        JPanel panelInfo = createInfoPanel();
        add(panelInfo, BorderLayout.NORTH);
        
        // Panel table
        JPanel panelTable = createTablePanel();
        add(panelTable, BorderLayout.CENTER);
        
        // Panel button
        JPanel panelBottom = createBottomPanel();
        add(panelBottom, BorderLayout.SOUTH);
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 248, 225));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(new Color(205, 133, 63)), "THÔNG TIN KHÁCH HÀNG", 
                           TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Mã KH
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Mã khách hàng:"), gbc);
        gbc.gridx = 1;
        txtMaKH = new JTextField(15);
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(240, 240, 240));
        panel.add(txtMaKH, gbc);
        
        // Họ tên
        gbc.gridx = 2;
        panel.add(createLabel("Họ tên:"), gbc);
        gbc.gridx = 3;
        txtHoTen = new JTextField(15);
        panel.add(txtHoTen, gbc);
        
        // Số điện thoại
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        txtSoDienThoai = new JTextField(15);
        panel.add(txtSoDienThoai, gbc);
        
        // Điểm tích lũy
        gbc.gridx = 2;
        panel.add(createLabel("Điểm tích lũy:"), gbc);
        gbc.gridx = 3;
        txtDiem = new JTextField(15);
        txtDiem.setEditable(false);
        txtDiem.setBackground(new Color(240, 240, 240));
        panel.add(txtDiem, gbc);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "DANH SÁCH KHÁCH HÀNG"));
        
        String[] columns = {"Mã KH", "Họ tên", "Số điện thoại", "Điểm tích lũy"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(25);
        tblKhachHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblKhachHang.setSelectionBackground(new Color(173, 216, 230));
        
        // Set column width
        tblKhachHang.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblKhachHang.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblKhachHang.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblKhachHang.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        JScrollPane scroll = new JScrollPane(tblKhachHang);
        panel.add(scroll, BorderLayout.CENTER);
        
        // Selection listener
        tblKhachHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiLenForm();
            }
        });
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(255, 248, 225));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(Color.GRAY), "CHỨC NĂNG"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        btnThem = createButton("➕ Thêm mới", new Color(60, 179, 113));
        btnSua = createButton("✏️ Cập nhật", new Color(70, 130, 180));
        btnXoa = createButton("🗑️ Xóa", new Color(220, 20, 60));
        btnLamMoi = createButton("🔄 Làm mới", new Color(128, 128, 128));
        
        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        
        // Panel tìm kiếm
        JPanel panelTim = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelTim.setBackground(new Color(255, 248, 225));
        panelTim.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(15);
        btnTimKiem = createButton("🔍 Tìm", new Color(100, 149, 237));
        panelTim.add(txtTimKiem);
        panelTim.add(btnTimKiem);
        
        panel.add(panelTim);
        
        // Button events
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadData();
            txtTimKiem.setText("");
        });
        btnTimKiem.addActionListener(e -> timKiem());
        
        // Enter key for search
        txtTimKiem.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    timKiem();
                }
            }
        });
        
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
        List<KhachHangDTO> list = khachHangBUS.getAllKhachHang();
        if (list != null) {
            for (KhachHangDTO kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getSoDienThoai(),
                    kh.getDiemTichLuy()
                });
            }
        }
    }
    
    private void clearForm() {
        txtMaKH.setText("");
        txtHoTen.setText("");
        txtSoDienThoai.setText("");
        txtDiem.setText("");
        isEditing = false;
    }
    
    private void hienThiLenForm() {
        int row = tblKhachHang.getSelectedRow();
        if (row >= 0) {
            txtMaKH.setText(model.getValueAt(row, 0).toString());
            txtHoTen.setText(model.getValueAt(row, 1).toString());
            txtSoDienThoai.setText(model.getValueAt(row, 2).toString());
            txtDiem.setText(model.getValueAt(row, 3).toString());
            isEditing = true;
        }
    }
    
    private void themKhachHang() {
        // Lấy dữ liệu từ form
        String hoTen = txtHoTen.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        
        // Validate
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!soDienThoai.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (10-11 số)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tạo đối tượng KH (không cần mã, BUS sẽ tự tạo)
        KhachHangDTO kh = new KhachHangDTO();
        kh.setHoTen(hoTen);
        kh.setSoDienThoai(soDienThoai);
        kh.setDiemTichLuy(0);
        
        if (khachHangBUS.addKhachHang(kh)) {
            JOptionPane.showMessageDialog(this, "✓ Thêm khách hàng thành công!");
            loadData();
            clearForm();
        }
    }
    
    private void suaKhachHang() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String maKH = txtMaKH.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        
        // Validate
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        KhachHangDTO kh = new KhachHangDTO();
        kh.setMaKH(maKH);
        kh.setHoTen(hoTen);
        kh.setSoDienThoai(soDienThoai);
        // Giữ nguyên điểm cũ
        int diemCu = Integer.parseInt(txtDiem.getText().trim());
        kh.setDiemTichLuy(diemCu);
        
        if (khachHangBUS.updateKhachHang(kh)) {
            JOptionPane.showMessageDialog(this, "✓ Cập nhật khách hàng thành công!");
            loadData();
            clearForm();
        }
    }
    
    private void xoaKhachHang() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String maKH = txtMaKH.getText().trim();
        String tenKH = txtHoTen.getText().trim();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa khách hàng " + maKH + " - " + tenKH + "?", 
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (khachHangBUS.deleteKhachHang(maKH)) {
                JOptionPane.showMessageDialog(this, "✓ Xóa khách hàng thành công!");
                loadData();
                clearForm();
            }
        }
    }
    
    private void timKiem() {
        String keyword = txtTimKiem.getText().trim();
        model.setRowCount(0);
        
        List<KhachHangDTO> list;
        if (keyword.isEmpty()) {
            list = khachHangBUS.getAllKhachHang();
        } else {
            list = khachHangBUS.searchKhachHang(keyword);
        }
        
        if (list != null) {
            for (KhachHangDTO kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getSoDienThoai(),
                    kh.getDiemTichLuy()
                });
            }
        }
        
        if (model.getRowCount() == 0 && !keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}