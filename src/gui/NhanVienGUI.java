package gui;

import bus.NhanVienBUS;
import dto.NhanVienDTO;
import util.GenerateID;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class NhanVienGUI extends JPanel {
    private JTable tblNhanVien;
    private DefaultTableModel model;
    private JTextField txtMaNV, txtHoTen, txtSoDienThoai, txtDiaChi, txtTenDangNhap, txtTim;
    private JComboBox<String> cboChucVu, cboTrangThai;
    private JPasswordField txtMatKhau;
    private JButton btnThem, btnSua, btnXoa, btnTimKiem, btnLamMoi;
    
    private NhanVienBUS nhanVienBUS = new NhanVienBUS();
    private boolean isEditing = false;
    
    public NhanVienGUI() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel tìm kiếm
        JPanel panelTim = createSearchPanel();
        add(panelTim, BorderLayout.NORTH);
        
        // Panel table
        JPanel panelTable = createTablePanel();
        add(panelTable, BorderLayout.CENTER);
        
        // Panel form
        JPanel panelForm = createFormPanel();
        add(panelForm, BorderLayout.SOUTH);
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
        panel.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "DANH SÁCH NHÂN VIÊN"));
        
        String[] columns = {"Mã NV", "Họ tên", "SĐT", "Địa chỉ", "Chức vụ", "Tên đăng nhập", "Trạng thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblNhanVien = new JTable(model);
        tblNhanVien.setRowHeight(25);
        tblNhanVien.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblNhanVien.setSelectionBackground(new Color(173, 216, 230));
        
        // Độ rộng cột
        tblNhanVien.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblNhanVien.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblNhanVien.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblNhanVien.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblNhanVien.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblNhanVien.getColumnModel().getColumn(6).setPreferredWidth(80);
        
        JScrollPane scroll = new JScrollPane(tblNhanVien);
        panel.add(scroll, BorderLayout.CENTER);
        
        tblNhanVien.addMouseListener(new MouseAdapter() {
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
            new TitledBorder(new LineBorder(Color.GRAY), "THÔNG TIN NHÂN VIÊN"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Mã nhân viên:"), gbc);
        gbc.gridx = 1;
        txtMaNV = new JTextField(15);
        txtMaNV.setEditable(false);
        panel.add(txtMaNV, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 3;
        txtHoTen = new JTextField(15);
        panel.add(txtHoTen, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        txtSoDienThoai = new JTextField(15);
        panel.add(txtSoDienThoai, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 3;
        txtDiaChi = new JTextField(15);
        panel.add(txtDiaChi, gbc);
        
        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Chức vụ:"), gbc);
        gbc.gridx = 1;
        cboChucVu = new JComboBox<>(new String[]{"Nhân viên", "Admin"});
        panel.add(cboChucVu, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 3;
        cboTrangThai = new JComboBox<>(new String[]{"Đang làm việc", "Đã nghỉ"});
        panel.add(cboTrangThai, gbc);
        
        // Row 4
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        txtTenDangNhap = new JTextField(15);
        panel.add(txtTenDangNhap, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 3;
        txtMatKhau = new JPasswordField(15);
        panel.add(txtMatKhau, gbc);
        
        // Row 5 - Buttons
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(255, 248, 225));
        
        btnThem = createButton("➕ Thêm mới", new Color(60, 179, 113));
        btnSua = createButton("✏️ Cập nhật", new Color(70, 130, 180));
        btnXoa = createButton("🗑️ Xóa", new Color(220, 20, 60));
        
        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        
        panel.add(buttonPanel, gbc);
        
        // Events
        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        
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
        List<NhanVienDTO> list = nhanVienBUS.getAllNhanVien();
        for (NhanVienDTO nv : list) {
            Object[] row = {
                nv.getMaNV(),
                nv.getHoTen(),
                nv.getSoDienThoai(),
                nv.getDiaChi(),
                nv.getChucVu(),
                nv.getTenDangNhap(),
                nv.isTrangThai() ? "Đang làm việc" : "Đã nghỉ"
            };
            model.addRow(row);
        }
    }
    
    private void loadFormFromTable() {
        int row = tblNhanVien.getSelectedRow();
        if (row != -1) {
            txtMaNV.setText((String) model.getValueAt(row, 0));
            txtHoTen.setText((String) model.getValueAt(row, 1));
            txtSoDienThoai.setText((String) model.getValueAt(row, 2));
            txtDiaChi.setText((String) model.getValueAt(row, 3));
            cboChucVu.setSelectedItem((String) model.getValueAt(row, 4));
            txtTenDangNhap.setText((String) model.getValueAt(row, 5));
            cboTrangThai.setSelectedItem((String) model.getValueAt(row, 6));
            txtMatKhau.setText("");
            isEditing = true;
        }
    }
    
    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtSoDienThoai.setText("");
        txtDiaChi.setText("");
        cboChucVu.setSelectedIndex(0);
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        cboTrangThai.setSelectedIndex(0);
        isEditing = false;
    }
    
    private void themNhanVien() {
        NhanVienDTO nv = new NhanVienDTO();
        nv.setHoTen(txtHoTen.getText().trim());
        nv.setSoDienThoai(txtSoDienThoai.getText().trim());
        nv.setDiaChi(txtDiaChi.getText().trim());
        nv.setChucVu((String) cboChucVu.getSelectedItem());
        nv.setTenDangNhap(txtTenDangNhap.getText().trim());
        
        String password = new String(txtMatKhau.getPassword());
        
        if (nhanVienBUS.addNhanVien(nv, password)) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            loadData();
            clearForm();
        }
    }
    
    private void suaNhanVien() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
            return;
        }
        
        NhanVienDTO nv = new NhanVienDTO();
        nv.setMaNV(txtMaNV.getText());
        nv.setHoTen(txtHoTen.getText().trim());
        nv.setSoDienThoai(txtSoDienThoai.getText().trim());
        nv.setDiaChi(txtDiaChi.getText().trim());
        nv.setChucVu((String) cboChucVu.getSelectedItem());
        nv.setTenDangNhap(txtTenDangNhap.getText().trim());
        nv.setTrangThai(cboTrangThai.getSelectedItem().equals("Đang làm việc"));
        
        if (nhanVienBUS.updateNhanVien(nv)) {
            JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
            loadData();
            clearForm();
        }
    }
    
    private void xoaNhanVien() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
            return;
        }
        
        if (nhanVienBUS.deleteNhanVien(txtMaNV.getText())) {
            JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
            loadData();
            clearForm();
        }
    }
    
    private void timKiem() {
        String keyword = txtTim.getText().trim();
        model.setRowCount(0);
        List<NhanVienDTO> list = nhanVienBUS.searchNhanVien(keyword);
        for (NhanVienDTO nv : list) {
            Object[] row = {
                nv.getMaNV(),
                nv.getHoTen(),
                nv.getSoDienThoai(),
                nv.getDiaChi(),
                nv.getChucVu(),
                nv.getTenDangNhap(),
                nv.isTrangThai() ? "Đang làm việc" : "Đã nghỉ"
            };
            model.addRow(row);
        }
    }
}