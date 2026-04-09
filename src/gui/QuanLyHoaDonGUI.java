package gui;

import bus.HoaDonBUS;
import bus.ChiTietHoaDonBUS;
import dto.HoaDonDTO;
import dto.ChiTietHoaDonDTO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class QuanLyHoaDonGUI extends JPanel {
    
    private JTable tblHoaDon, tblChiTiet;
    private DefaultTableModel modelHoaDon, modelChiTiet;
    private JTextField txtTimKiem;
    private JButton btnTimKiem, btnLamMoi, btnXemChiTiet, btnInHoaDon;
    private JLabel lblTongDoanhThu, lblSoHoaDon;
    
    private HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private ChiTietHoaDonBUS chiTietBUS = new ChiTietHoaDonBUS();
    
    public QuanLyHoaDonGUI() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 248, 255));
        
        // Panel trên - Thống kê nhanh
        JPanel panelThongKe = createThongKePanel();
        add(panelThongKe, BorderLayout.NORTH);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        
        // Panel danh sách hóa đơn
        JPanel panelHoaDon = createHoaDonPanel();
        splitPane.setTopComponent(panelHoaDon);
        
        // Panel chi tiết hóa đơn
        JPanel panelChiTiet = createChiTietPanel();
        splitPane.setBottomComponent(panelChiTiet);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createThongKePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBackground(new Color(255, 248, 225));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(new Color(0, 100, 0)), "THỐNG KÊ NHANH"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Panel tổng doanh thu
        JPanel pDoanhThu = new JPanel(new BorderLayout());
        pDoanhThu.setBackground(new Color(255, 228, 196));
        pDoanhThu.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0)));
        
        JLabel lblDoanhThuTitle = new JLabel("💰 TỔNG DOANH THU", SwingConstants.CENTER);
        lblDoanhThuTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblDoanhThuTitle.setForeground(new Color(0, 100, 0));
        
        lblTongDoanhThu = new JLabel("0 VNĐ", SwingConstants.CENTER);
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongDoanhThu.setForeground(Color.RED);
        
        pDoanhThu.add(lblDoanhThuTitle, BorderLayout.NORTH);
        pDoanhThu.add(lblTongDoanhThu, BorderLayout.CENTER);
        
        // Panel số hóa đơn
        JPanel pHoaDon = new JPanel(new BorderLayout());
        pHoaDon.setBackground(new Color(173, 216, 230));
        pHoaDon.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0)));
        
        JLabel lblHoaDonTitle = new JLabel("📄 SỐ HÓA ĐƠN", SwingConstants.CENTER);
        lblHoaDonTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblHoaDonTitle.setForeground(new Color(0, 100, 0));
        
        lblSoHoaDon = new JLabel("0", SwingConstants.CENTER);
        lblSoHoaDon.setFont(new Font("Arial", Font.BOLD, 18));
        lblSoHoaDon.setForeground(new Color(0, 0, 255));
        
        pHoaDon.add(lblHoaDonTitle, BorderLayout.NORTH);
        pHoaDon.add(lblSoHoaDon, BorderLayout.CENTER);
        
        panel.add(pDoanhThu);
        panel.add(pHoaDon);
        
        return panel;
    }
    
    private JPanel createHoaDonPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "DANH SÁCH HÓA ĐƠN"));
        
        // Panel tìm kiếm
        JPanel panelTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTim.setBackground(Color.WHITE);
        panelTim.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(20);
        btnTimKiem = createButton("🔍 Tìm", new Color(70, 130, 180));
        btnLamMoi = createButton("🔄 Làm mới", new Color(100, 100, 100));
        panelTim.add(txtTimKiem);
        panelTim.add(btnTimKiem);
        panelTim.add(btnLamMoi);
        
        // Table hóa đơn
        String[] columns = {"Mã HD", "Ngày lập", "Nhân viên", "Khách hàng", "Tổng tiền", "Giảm giá", "Thành tiền"};
        modelHoaDon = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(25);
        tblHoaDon.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblHoaDon.setSelectionBackground(new Color(173, 216, 230));
        
        // Độ rộng cột
        tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblHoaDon.getColumnModel().getColumn(1).setPreferredWidth(130);
        tblHoaDon.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblHoaDon.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblHoaDon.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblHoaDon.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblHoaDon.getColumnModel().getColumn(6).setPreferredWidth(120);
        
        JScrollPane scroll = new JScrollPane(tblHoaDon);
        
        panel.add(panelTim, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        
        // Sự kiện
        btnTimKiem.addActionListener(e -> timKiem());
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadData();
        });
        
        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                xemChiTietHoaDon();
            }
        });
        
        return panel;
    }
    
    private JPanel createChiTietPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "CHI TIẾT HÓA ĐƠN"));
        
        // Table chi tiết
        String[] columns = {"Mã món", "Tên món", "Size", "Số lượng", "Đơn giá", "Thành tiền"};
        modelChiTiet = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblChiTiet = new JTable(modelChiTiet);
        tblChiTiet.setRowHeight(25);
        tblChiTiet.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scroll = new JScrollPane(tblChiTiet);
        
        // Panel nút
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setBackground(Color.WHITE);
        btnInHoaDon = createButton("🖨️ In hóa đơn", new Color(100, 149, 237));
        panelButton.add(btnInHoaDon);
        
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelButton, BorderLayout.SOUTH);
        
        btnInHoaDon.addActionListener(e -> inHoaDon());
        
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
        return button;
    }
    
    private void loadData() {
        modelHoaDon.setRowCount(0);
        List<HoaDonDTO> list = hoaDonBUS.getAllHoaDon();
        
        double tongDoanhThu = 0;
        
        if (list != null) {
            for (HoaDonDTO hd : list) {
                Object[] row = {
                    hd.getMaHD(),
                    hd.getNgayLapFormatted(),
                    hd.getTenNV() != null ? hd.getTenNV() : "",
                    hd.getTenKH() != null ? hd.getTenKH() : "Khách lẻ",
                    String.format("%,.0f", hd.getTongTien()),
                    String.format("%,.0f", hd.getGiamGia()),
                    String.format("%,.0f", hd.getThanhTien())
                };
                modelHoaDon.addRow(row);
                tongDoanhThu += hd.getThanhTien();
            }
        }
        
        lblTongDoanhThu.setText(String.format("%,.0f VNĐ", tongDoanhThu));
        lblSoHoaDon.setText(String.valueOf(modelHoaDon.getRowCount()));
    }
    
    private void timKiem() {
        String keyword = txtTimKiem.getText().trim();
        modelHoaDon.setRowCount(0);
        
        List<HoaDonDTO> list = hoaDonBUS.searchHoaDon(keyword);
        double tongDoanhThu = 0;
        
        if (list != null) {
            for (HoaDonDTO hd : list) {
                Object[] row = {
                    hd.getMaHD(),
                    hd.getNgayLapFormatted(),
                    hd.getTenNV() != null ? hd.getTenNV() : "",
                    hd.getTenKH() != null ? hd.getTenKH() : "Khách lẻ",
                    String.format("%,.0f", hd.getTongTien()),
                    String.format("%,.0f", hd.getGiamGia()),
                    String.format("%,.0f", hd.getThanhTien())
                };
                modelHoaDon.addRow(row);
                tongDoanhThu += hd.getThanhTien();
            }
        }
        
        lblTongDoanhThu.setText(String.format("%,.0f VNĐ", tongDoanhThu));
        lblSoHoaDon.setText(String.valueOf(modelHoaDon.getRowCount()));
    }
    
    private void xemChiTietHoaDon() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) return;
        
        String maHD = (String) modelHoaDon.getValueAt(row, 0);
        
        modelChiTiet.setRowCount(0);
        List<ChiTietHoaDonDTO> list = chiTietBUS.getByMaHD(maHD);
        
        if (list != null) {
            for (ChiTietHoaDonDTO ct : list) {
                Object[] rowData = {
                    ct.getMaMon(),
                    ct.getTenMon(),
                    ct.getSize(),
                    ct.getSoLuong(),
                    String.format("%,.0f", ct.getDonGia()),
                    String.format("%,.0f", ct.getThanhTien())
                };
                modelChiTiet.addRow(rowData);
            }
        }
    }
    
    private void inHoaDon() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần in!");
            return;
        }
        
        String maHD = (String) modelHoaDon.getValueAt(row, 0);
        String ngayLap = (String) modelHoaDon.getValueAt(row, 1);
        String khachHang = (String) modelHoaDon.getValueAt(row, 3);
        String tongTien = (String) modelHoaDon.getValueAt(row, 4);
        String giamGia = (String) modelHoaDon.getValueAt(row, 5);
        String thanhTien = (String) modelHoaDon.getValueAt(row, 6);
        
        // Tạo nội dung hóa đơn
        StringBuilder content = new StringBuilder();
        content.append("═══════════════════════════════════════════\n");
        content.append("         ☕ TEA SHOP MANAGEMENT ☕\n");
        content.append("═══════════════════════════════════════════\n");
        content.append("Mã HD: ").append(maHD).append("\n");
        content.append("Ngày: ").append(ngayLap).append("\n");
        content.append("Khách hàng: ").append(khachHang).append("\n");
        content.append("───────────────────────────────────────────\n");
        content.append("Chi tiết hóa đơn:\n");
        
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            String tenMon = (String) modelChiTiet.getValueAt(i, 1);
            String size = (String) modelChiTiet.getValueAt(i, 2);
            String sl = (String) modelChiTiet.getValueAt(i, 3);
            String donGia = (String) modelChiTiet.getValueAt(i, 4);
            String thanhTienCT = (String) modelChiTiet.getValueAt(i, 5);
            
            content.append(String.format("  %s (%s) x%s = %s\n", tenMon, size, sl, thanhTienCT));
        }
        
        content.append("───────────────────────────────────────────\n");
        content.append(String.format("Tổng tiền: %s\n", tongTien));
        content.append(String.format("Giảm giá: %s\n", giamGia));
        content.append(String.format("Thành tiền: %s\n", thanhTien));
        content.append("═══════════════════════════════════════════\n");
        content.append("         Cảm ơn quý khách!\n");
        content.append("═══════════════════════════════════════════\n");
        
        // Hiển thị hóa đơn dạng text
        JTextArea textArea = new JTextArea(content.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "HÓA ĐƠN - " + maHD, 
                                      JOptionPane.INFORMATION_MESSAGE);
    }
}