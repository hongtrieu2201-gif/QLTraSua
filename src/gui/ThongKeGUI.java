package gui;

import dao.ThongKeDAO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ThongKeGUI extends JPanel {
    private JComboBox<String> cboLoaiThongKe;
    private JSpinner spnNgay, spnThang, spnNam;
    private JButton btnXem;
    private JTable tblDoanhThu, tblMonBanChay, tblKhachHangThanThiet;
    private DefaultTableModel modelDoanhThu, modelMonBanChay, modelKhachHang;
    private JLabel lblTongDoanhThu, lblSoHoaDon, lblTrungBinh;
    
    private ThongKeDAO thongKeDAO = new ThongKeDAO();
    
    public ThongKeGUI() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel chọn thống kê
        JPanel panelChon = createChonPanel();
        add(panelChon, BorderLayout.NORTH);
        
        // Tabbed pane cho các loại thống kê
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab doanh thu
        JPanel panelDoanhThu = createDoanhThuPanel();
        tabbedPane.addTab("📊 Doanh thu", panelDoanhThu);
        
        // Tab món bán chạy
        JPanel panelMonBanChay = createMonBanChayPanel();
        tabbedPane.addTab("🏆 Món bán chạy", panelMonBanChay);
        
        // Tab khách hàng thân thiết
        JPanel panelKhachHang = createKhachHangPanel();
        tabbedPane.addTab("⭐ Khách hàng thân thiết", panelKhachHang);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createChonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(Color.GRAY), "CHỌN THỜI GIAN THỐNG KÊ"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        cboLoaiThongKe = new JComboBox<>(new String[]{"Theo ngày", "Theo tháng", "Theo năm"});
        
        // Ngày
        JPanel panelNgay = new JPanel(new FlowLayout());
        panelNgay.add(new JLabel("Ngày:"));
        spnNgay = new JSpinner(new SpinnerDatePickerModel());
        JSpinner.DateEditor deNgay = new JSpinner.DateEditor(spnNgay, "dd/MM/yyyy");
        spnNgay.setEditor(deNgay);
        panelNgay.add(spnNgay);
        
        // Tháng
        JPanel panelThang = new JPanel(new FlowLayout());
        panelThang.add(new JLabel("Tháng:"));
        spnThang = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        panelThang.add(spnThang);
        
        // Năm
        JPanel panelNam = new JPanel(new FlowLayout());
        panelNam.add(new JLabel("Năm:"));
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        spnNam = new JSpinner(new SpinnerNumberModel(currentYear, 2020, currentYear + 10, 1));
        panelNam.add(spnNam);
        
        btnXem = new JButton("📊 Xem thống kê");
        btnXem.setBackground(new Color(70, 130, 180));
        btnXem.setForeground(Color.WHITE);
        btnXem.setFont(new Font("Arial", Font.BOLD, 14));
        btnXem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panel.add(cboLoaiThongKe);
        panel.add(panelNgay);
        panel.add(panelThang);
        panel.add(panelNam);
        panel.add(btnXem);
        
        // Ẩn/hiện panel theo loại thống kê
        cboLoaiThongKe.addActionListener(e -> {
            boolean isNgay = cboLoaiThongKe.getSelectedIndex() == 0;
            boolean isThang = cboLoaiThongKe.getSelectedIndex() == 1;
            boolean isNam = cboLoaiThongKe.getSelectedIndex() == 2;
            
            panelNgay.setVisible(isNgay);
            panelThang.setVisible(isThang);
            panelNam.setVisible(isThang || isNam);
        });
        
        panelNgay.setVisible(true);
        panelThang.setVisible(false);
        panelNam.setVisible(false);
        
        btnXem.addActionListener(e -> xemThongKe());
        
        return panel;
    }
    
    private JPanel createDoanhThuPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        
        // Panel thông tin tổng hợp
        JPanel panelInfo = new JPanel(new GridLayout(1, 3, 10, 10));
        panelInfo.setBackground(new Color(255, 248, 225));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblTongDoanhThu = new JLabel("0 VNĐ", SwingConstants.CENTER);
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongDoanhThu.setForeground(Color.RED);
        
        lblSoHoaDon = new JLabel("0", SwingConstants.CENTER);
        lblSoHoaDon.setFont(new Font("Arial", Font.BOLD, 18));
        lblSoHoaDon.setForeground(new Color(0, 100, 0));
        
        lblTrungBinh = new JLabel("0 VNĐ", SwingConstants.CENTER);
        lblTrungBinh.setFont(new Font("Arial", Font.BOLD, 18));
        lblTrungBinh.setForeground(new Color(70, 130, 180));
        
        JPanel p1 = createInfoPanel("💰 TỔNG DOANH THU", lblTongDoanhThu, new Color(255, 200, 200));
        JPanel p2 = createInfoPanel("📄 TỔNG SỐ HÓA ĐƠN", lblSoHoaDon, new Color(200, 255, 200));
        JPanel p3 = createInfoPanel("📊 TRUNG BÌNH/HD", lblTrungBinh, new Color(200, 200, 255));
        
        panelInfo.add(p1);
        panelInfo.add(p2);
        panelInfo.add(p3);
        
        // Table doanh thu chi tiết
        String[] columns = {"Thời gian", "Số hóa đơn", "Doanh thu"};
        modelDoanhThu = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblDoanhThu = new JTable(modelDoanhThu);
        tblDoanhThu.setRowHeight(25);
        tblDoanhThu.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scroll = new JScrollPane(tblDoanhThu);
        
        panel.add(panelInfo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createInfoPanel(String title, JLabel label, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(label, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMonBanChayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "TOP 10 MÓN BÁN CHẠY NHẤT"));
        
        String[] columns = {"STT", "Mã món", "Tên món", "Số lượng bán", "Doanh thu"};
        modelMonBanChay = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblMonBanChay = new JTable(modelMonBanChay);
        tblMonBanChay.setRowHeight(25);
        tblMonBanChay.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        tblMonBanChay.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblMonBanChay.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblMonBanChay.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblMonBanChay.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblMonBanChay.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        JScrollPane scroll = new JScrollPane(tblMonBanChay);
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createKhachHangPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "TOP 10 KHÁCH HÀNG THÂN THIẾT"));
        
        String[] columns = {"STT", "Mã KH", "Họ tên", "Số điện thoại", "Điểm tích lũy", "Tổng chi tiêu"};
        modelKhachHang = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblKhachHangThanThiet = new JTable(modelKhachHang);
        tblKhachHangThanThiet.setRowHeight(25);
        tblKhachHangThanThiet.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scroll = new JScrollPane(tblKhachHangThanThiet);
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void xemThongKe() {
        int loai = cboLoaiThongKe.getSelectedIndex();
        
        if (loai == 0) { // Theo ngày
            Date date = (Date) spnNgay.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngay = sdf.format(date);
            thongKeTheoNgay(ngay);
            thongKeMonBanChayTheoNgay(ngay);
            thongKeKhachHangThanThietTheoNgay(ngay);
            
        } else if (loai == 1) { // Theo tháng
            int thang = (Integer) spnThang.getValue();
            int nam = (Integer) spnNam.getValue();
            thongKeTheoThang(thang, nam);
            thongKeMonBanChayTheoThang(thang, nam);
            thongKeKhachHangThanThietTheoThang(thang, nam);
            
        } else { // Theo năm
            int nam = (Integer) spnNam.getValue();
            thongKeTheoNam(nam);
            thongKeMonBanChayTheoNam(nam);
            thongKeKhachHangThanThietTheoNam(nam);
        }
    }
    
    private void thongKeTheoNgay(String ngay) {
        Map<String, Object> result = thongKeDAO.thongKeTheoNgay(ngay);
        
        long doanhThu = (long) result.get("doanhThu");
        int soHoaDon = (int) result.get("soHoaDon");
        double trungBinh = (double) result.get("trungBinh");
        
        lblTongDoanhThu.setText(String.format("%,d VNĐ", doanhThu));
        lblSoHoaDon.setText(String.valueOf(soHoaDon));
        lblTrungBinh.setText(String.format("%,.0f VNĐ", trungBinh));
        
        // Chi tiết theo giờ
        modelDoanhThu.setRowCount(0);
        List<Object[]> chiTiet = thongKeDAO.chiTietDoanhThuTheoNgay(ngay);
        for (Object[] row : chiTiet) {
            modelDoanhThu.addRow(row);
        }
    }
    
    private void thongKeTheoThang(int thang, int nam) {
        Map<String, Object> result = thongKeDAO.thongKeTheoThang(thang, nam);
        
        long doanhThu = (long) result.get("doanhThu");
        int soHoaDon = (int) result.get("soHoaDon");
        double trungBinh = (double) result.get("trungBinh");
        
        lblTongDoanhThu.setText(String.format("%,d VNĐ", doanhThu));
        lblSoHoaDon.setText(String.valueOf(soHoaDon));
        lblTrungBinh.setText(String.format("%,.0f VNĐ", trungBinh));
        
        // Chi tiết theo ngày
        modelDoanhThu.setRowCount(0);
        List<Object[]> chiTiet = thongKeDAO.chiTietDoanhThuTheoThang(thang, nam);
        for (Object[] row : chiTiet) {
            modelDoanhThu.addRow(row);
        }
    }
    
    private void thongKeTheoNam(int nam) {
        Map<String, Object> result = thongKeDAO.thongKeTheoNam(nam);
        
        long doanhThu = (long) result.get("doanhThu");
        int soHoaDon = (int) result.get("soHoaDon");
        double trungBinh = (double) result.get("trungBinh");
        
        lblTongDoanhThu.setText(String.format("%,d VNĐ", doanhThu));
        lblSoHoaDon.setText(String.valueOf(soHoaDon));
        lblTrungBinh.setText(String.format("%,.0f VNĐ", trungBinh));
        
        // Chi tiết theo tháng
        modelDoanhThu.setRowCount(0);
        List<Object[]> chiTiet = thongKeDAO.chiTietDoanhThuTheoNam(nam);
        for (Object[] row : chiTiet) {
            modelDoanhThu.addRow(row);
        }
    }
    
    private void thongKeMonBanChayTheoNgay(String ngay) {
        modelMonBanChay.setRowCount(0);
        List<Object[]> list = thongKeDAO.topMonBanChayTheoNgay(ngay);
        int stt = 1;
        for (Object[] row : list) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = stt++;
            System.arraycopy(row, 0, newRow, 1, row.length);
            modelMonBanChay.addRow(newRow);
        }
    }
    
    private void thongKeMonBanChayTheoThang(int thang, int nam) {
        modelMonBanChay.setRowCount(0);
        List<Object[]> list = thongKeDAO.topMonBanChayTheoThang(thang, nam);
        int stt = 1;
        for (Object[] row : list) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = stt++;
            System.arraycopy(row, 0, newRow, 1, row.length);
            modelMonBanChay.addRow(newRow);
        }
    }
    
    private void thongKeMonBanChayTheoNam(int nam) {
        modelMonBanChay.setRowCount(0);
        List<Object[]> list = thongKeDAO.topMonBanChayTheoNam(nam);
        int stt = 1;
        for (Object[] row : list) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = stt++;
            System.arraycopy(row, 0, newRow, 1, row.length);
            modelMonBanChay.addRow(newRow);
        }
    }
    
    private void thongKeKhachHangThanThietTheoNgay(String ngay) {
        modelKhachHang.setRowCount(0);
        List<Object[]> list = thongKeDAO.topKhachHangTheoNgay(ngay);
        int stt = 1;
        for (Object[] row : list) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = stt++;
            System.arraycopy(row, 0, newRow, 1, row.length);
            modelKhachHang.addRow(newRow);
        }
    }
    
    private void thongKeKhachHangThanThietTheoThang(int thang, int nam) {
        modelKhachHang.setRowCount(0);
        List<Object[]> list = thongKeDAO.topKhachHangTheoThang(thang, nam);
        int stt = 1;
        for (Object[] row : list) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = stt++;
            System.arraycopy(row, 0, newRow, 1, row.length);
            modelKhachHang.addRow(newRow);
        }
    }
    
    private void thongKeKhachHangThanThietTheoNam(int nam) {
        modelKhachHang.setRowCount(0);
        List<Object[]> list = thongKeDAO.topKhachHangTheoNam(nam);
        int stt = 1;
        for (Object[] row : list) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = stt++;
            System.arraycopy(row, 0, newRow, 1, row.length);
            modelKhachHang.addRow(newRow);
        }
    }
    
    // Inner class cho SpinnerDatePicker
    class SpinnerDatePickerModel extends SpinnerDateModel {
        public SpinnerDatePickerModel() {
            super(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        }
    }
}