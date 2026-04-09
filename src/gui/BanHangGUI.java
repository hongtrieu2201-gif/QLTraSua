package gui;

import dto.ChiTietToppingDTO;
import dao.*;
import dto.*;
import bus.*;
import util.GenerateID;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class BanHangGUI extends JPanel {
    
    // Components chính
    private JTable tblGioHang, tblSanPham, tblTopping;
    private DefaultTableModel modelGioHang, modelSanPham, modelTopping;
    private JComboBox<String> cboKhachHang, cboSize;
    private JSpinner spnSoLuong, spnSoLuongTopping;
    private JTextField txtTimSanPham, txtTongTien, txtGiamGia, txtThanhTien, txtDiemSD, txtTimTopping;
    private JLabel lblMaHD, lblNgayLap, lblNhanVien, lblDiemTichLuy;
    private JButton btnThemMon, btnXoaMon, btnThanhToan, btnTimSanPham, btnLamMoi, btnThemTopping, btnXoaTopping, btnTimTopping;
    
    // Business
    private HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private KhachHangBUS khachHangBUS = new KhachHangBUS();
    private MonTraSuaBUS monBUS = new MonTraSuaBUS();
    private ToppingBUS toppingBUS = new ToppingBUS();
    
    // Data
    private String maNVDangNhap = "NV001";
    private String maHDHienTai;
    private List<GioHangItem> gioHang = new ArrayList<>();
    private List<ChiTietToppingDTO> selectedToppings = new ArrayList<>();
    private String currentSelectedMon = null;
    
    // Màu sắc
    private final Color COLOR_BG_MAIN = new Color(240, 248, 255);
    private final Color COLOR_BG_INFO = new Color(173, 216, 230);
    private final Color COLOR_BG_PRODUCT = new Color(255, 250, 205);
    private final Color COLOR_BG_CART = new Color(255, 240, 245);
    private final Color COLOR_BG_PAYMENT = new Color(255, 228, 196);
    private final Color COLOR_BORDER = new Color(139, 69, 19);
    private final Color COLOR_BUTTON_GREEN = new Color(60, 179, 113);
    private final Color COLOR_BUTTON_BLUE = new Color(70, 130, 180);
    private final Color COLOR_BUTTON_RED = new Color(220, 20, 60);
    private final Color COLOR_BUTTON_ORANGE = new Color(255, 140, 0);
    private final Color COLOR_BUTTON_GRAY = new Color(100, 100, 100);
    
    public BanHangGUI() {
        initComponents();
        loadData();
        taoHoaDonMoi();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(COLOR_BG_MAIN);
        
        // Panel trên cùng - Thông tin hóa đơn
        JPanel panelInfo = createInfoPanel();
        add(panelInfo, BorderLayout.NORTH);
        
        // Split pane chính
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.6);
        mainSplitPane.setBorder(null);
        
        // Panel trái - Danh sách món và Topping
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftSplitPane.setResizeWeight(0.55);
        leftSplitPane.setBorder(null);
        
        JPanel panelSanPham = createSanPhamPanel();
        leftSplitPane.setTopComponent(panelSanPham);
        
        JPanel panelTopping = createToppingPanel();
        leftSplitPane.setBottomComponent(panelTopping);
        
        mainSplitPane.setLeftComponent(leftSplitPane);
        
        // Panel phải - Giỏ hàng
        JPanel panelGioHang = createGioHangPanel();
        mainSplitPane.setRightComponent(panelGioHang);
        
        add(mainSplitPane, BorderLayout.CENTER);
        
        // Panel dưới cùng - Thanh toán
        JPanel panelThanhToan = createThanhToanPanel();
        add(panelThanhToan, BorderLayout.SOUTH);
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 5));
        panel.setBackground(COLOR_BG_INFO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(COLOR_BORDER), "THÔNG TIN HÓA ĐƠN", 
                           TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), COLOR_BORDER),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        lblMaHD = new JLabel("HD001");
        lblNgayLap = new JLabel(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        lblNhanVien = new JLabel("Nguyễn Văn A");
        cboKhachHang = new JComboBox<>();
        
        cboKhachHang.addItem("KH000 - Khách lẻ");
        List<KhachHangDTO> khachHangList = khachHangBUS.getAllKhachHang();
        if (khachHangList != null) {
            for (KhachHangDTO kh : khachHangList) {
                cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getHoTen() + " (" + kh.getDiemTichLuy() + " điểm)");
            }
        }
        
        cboKhachHang.addActionListener(e -> capNhatDiemKhachHang());
        
        lblDiemTichLuy = new JLabel("0 điểm");
        
        panel.add(createLabelField("Mã HD:", lblMaHD));
        panel.add(createLabelField("Ngày lập:", lblNgayLap));
        panel.add(createLabelField("Nhân viên:", lblNhanVien));
        panel.add(createLabelField("Khách hàng:", cboKhachHang));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(createLabelField("Điểm tích lũy:", lblDiemTichLuy));
        panel.add(new JLabel(""));
        
        return panel;
    }
    
    private JPanel createSanPhamPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(COLOR_BG_PRODUCT);
        panel.setBorder(new TitledBorder(new LineBorder(COLOR_BORDER), "DANH SÁCH MÓN", 
                       TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), COLOR_BORDER));
        
        // Panel tìm kiếm
        JPanel panelTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTim.setBackground(COLOR_BG_PRODUCT);
        panelTim.add(new JLabel("Tìm món:"));
        txtTimSanPham = new JTextField(20);
        btnTimSanPham = createButton("🔍 Tìm", COLOR_BUTTON_BLUE);
        btnLamMoi = createButton("🔄 Làm mới", COLOR_BUTTON_GRAY);
        panelTim.add(txtTimSanPham);
        panelTim.add(btnTimSanPham);
        panelTim.add(btnLamMoi);
        
        // Table sản phẩm - Có cột hình ảnh
        String[] columns = {"Hình ảnh", "Mã món", "Tên món", "Loại", "Size", "Giá (M)"};
        modelSanPham = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return ImageIcon.class;
                return String.class;
            }
        };
        
        tblSanPham = new JTable(modelSanPham);
        tblSanPham.setRowHeight(60);
        tblSanPham.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblSanPham.setSelectionBackground(new Color(255, 228, 196));
        
        // Độ rộng cột
        tblSanPham.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblSanPham.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblSanPham.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblSanPham.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblSanPham.getColumnModel().getColumn(4).setPreferredWidth(50);
        tblSanPham.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        JScrollPane scroll = new JScrollPane(tblSanPham);
        
        // Panel thêm món
        JPanel panelThem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelThem.setBackground(COLOR_BG_PRODUCT);
        panelThem.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "THÊM VÀO GIỎ", 
                           TitledBorder.LEFT, TitledBorder.TOP));
        
        panelThem.add(new JLabel("Size:"));
        cboSize = new JComboBox<>(new String[]{"S", "M", "L"});
        panelThem.add(cboSize);
        
        panelThem.add(new JLabel("Số lượng:"));
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        panelThem.add(spnSoLuong);
        
        btnThemMon = createButton("➕ Thêm vào giỏ", COLOR_BUTTON_GREEN);
        panelThem.add(btnThemMon);
        
        panel.add(panelTim, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelThem, BorderLayout.SOUTH);
        
        // Events
        btnTimSanPham.addActionListener(e -> timSanPham());
        btnLamMoi.addActionListener(e -> {
            txtTimSanPham.setText("");
            loadSanPham();
        });
        btnThemMon.addActionListener(e -> themVaoGio());
        
        tblSanPham.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblSanPham.getSelectedRow();
                if (row >= 0) {
                    currentSelectedMon = (String) modelSanPham.getValueAt(row, 1);
                }
            }
        });
        
        tblSanPham.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    themVaoGio();
                }
            }
        });
        
        return panel;
    }
    
    private JPanel createToppingPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(COLOR_BG_PRODUCT);
        panel.setBorder(new TitledBorder(new LineBorder(COLOR_BORDER), "🍬 CHỌN TOPPING", 
                       TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), COLOR_BORDER));
        panel.setPreferredSize(new Dimension(0, 250));
        
        // Panel tìm kiếm topping
        JPanel panelTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTim.setBackground(COLOR_BG_PRODUCT);
        panelTim.add(new JLabel("Tìm topping:"));
        txtTimTopping = new JTextField(15);
        btnTimTopping = createButton("🔍 Tìm", COLOR_BUTTON_BLUE);
        panelTim.add(txtTimTopping);
        panelTim.add(btnTimTopping);
        
        // Table topping
        String[] columns = {"Mã", "Tên topping", "Giá"};
        modelTopping = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblTopping = new JTable(modelTopping);
        tblTopping.setRowHeight(25);
        tblTopping.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(tblTopping);
        scroll.setPreferredSize(new Dimension(0, 120));
        
        // Panel thêm topping
        JPanel panelThem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelThem.setBackground(COLOR_BG_PRODUCT);
        panelThem.add(new JLabel("Số lượng:"));
        spnSoLuongTopping = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        panelThem.add(spnSoLuongTopping);
        
        btnThemTopping = createButton("➕ Thêm topping", COLOR_BUTTON_GREEN);
        btnXoaTopping = createButton("🗑️ Xóa topping", COLOR_BUTTON_RED);
        panelThem.add(btnThemTopping);
        panelThem.add(btnXoaTopping);
        
        panel.add(panelTim, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelThem, BorderLayout.SOUTH);
        
        // Events
        btnTimTopping.addActionListener(e -> timTopping());
        btnThemTopping.addActionListener(e -> themToppingVaoMon());
        btnXoaTopping.addActionListener(e -> xoaToppingKhoiDanhSach());
        
        return panel;
    }
    
    private JPanel createGioHangPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(COLOR_BG_CART);
        panel.setBorder(new TitledBorder(new LineBorder(COLOR_BORDER), "GIỎ HÀNG", 
                       TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), COLOR_BORDER));
        
        String[] columns = {"Mã món", "Tên món", "Size", "SL", "Đơn giá", "Thành tiền"};
        modelGioHang = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblGioHang = new JTable(modelGioHang);
        tblGioHang.setRowHeight(50);
        tblGioHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Độ rộng cột
        tblGioHang.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblGioHang.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblGioHang.getColumnModel().getColumn(2).setPreferredWidth(50);
        tblGioHang.getColumnModel().getColumn(3).setPreferredWidth(50);
        tblGioHang.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblGioHang.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        JScrollPane scroll = new JScrollPane(tblGioHang);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        btnXoaMon = createButton("🗑️ Xóa món", COLOR_BUTTON_RED);
        
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setBackground(COLOR_BG_CART);
        panelButton.add(btnXoaMon);
        
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelButton, BorderLayout.SOUTH);
        
        btnXoaMon.addActionListener(e -> xoaMonKhoiGio());
        
        return panel;
    }
    
    private JPanel createThanhToanPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(COLOR_BG_PAYMENT);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(new LineBorder(COLOR_BORDER), "THANH TOÁN", 
                           TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), COLOR_BORDER),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel panelTinhTien = new JPanel(new GridLayout(2, 4, 10, 5));
        panelTinhTien.setBackground(COLOR_BG_PAYMENT);
        
        txtTongTien = new JTextField("0");
        txtTongTien.setEditable(false);
        txtTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
        
        txtGiamGia = new JTextField("0");
        txtGiamGia.setFont(new Font("Arial", Font.BOLD, 14));
        txtGiamGia.setHorizontalAlignment(JTextField.RIGHT);
        
        txtDiemSD = new JTextField("0");
        txtDiemSD.setFont(new Font("Arial", Font.BOLD, 14));
        txtDiemSD.setHorizontalAlignment(JTextField.RIGHT);
        
        txtThanhTien = new JTextField("0");
        txtThanhTien.setEditable(false);
        txtThanhTien.setFont(new Font("Arial", Font.BOLD, 18));
        txtThanhTien.setForeground(Color.RED);
        txtThanhTien.setHorizontalAlignment(JTextField.RIGHT);
        
        panelTinhTien.add(createLabelField("Tổng tiền:", txtTongTien));
        panelTinhTien.add(createLabelField("Giảm giá (VNĐ):", txtGiamGia));
        panelTinhTien.add(createLabelField("Điểm sử dụng (10đ=1k):", txtDiemSD));
        panelTinhTien.add(createLabelField("THÀNH TIỀN:", txtThanhTien));
        
        btnThanhToan = createButton("💰 THANH TOÁN 💰", COLOR_BUTTON_ORANGE);
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 16));
        
        panel.add(panelTinhTien, BorderLayout.CENTER);
        panel.add(btnThanhToan, BorderLayout.SOUTH);
        
        txtGiamGia.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tinhThanhTien();
            }
        });
        
        txtDiemSD.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tinhThanhTien();
            }
        });
        
        btnThanhToan.addActionListener(e -> thanhToan());
        
        return panel;
    }
    
    private JPanel createLabelField(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(COLOR_BG_PAYMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
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
    
    // ==================== LOAD DATA ====================
    
    private void loadData() {
        loadSanPham();
        loadTopping();
    }
    
    private void loadSanPham() {
        modelSanPham.setRowCount(0);
        List<MonTraSuaDTO> list = monBUS.getAllMon();
        
        if (list != null) {
            for (MonTraSuaDTO mon : list) {
                double giaMacDinh = monBUS.getGiaTheoSize(mon.getMaMon(), "M");
                ImageIcon image = getProductImage(mon.getMaMon());
                
                Object[] row = {
                    image,
                    mon.getMaMon(),
                    mon.getTenMon(),
                    mon.getLoaiMon(),
                    "M",
                    String.format("%,.0f", giaMacDinh)
                };
                modelSanPham.addRow(row);
            }
        }
    }
    
    private void loadTopping() {
        modelTopping.setRowCount(0);
        List<ToppingDTO> list = toppingBUS.getAllTopping();
        if (list != null) {
            for (ToppingDTO top : list) {
                Object[] row = {
                    top.getMaTopping(),
                    top.getTenTopping(),
                    String.format("%,.0f", top.getGiaTopping())
                };
                modelTopping.addRow(row);
            }
        }
    }
    
    // ==================== HÌNH ẢNH ====================
    
    private ImageIcon getProductImage(String maMon) {
    try {
        // Thử đọc file .jpg từ thư mục icons (cùng cấp)
        String path = "/icons/" + maMon + ".jpg";
        java.net.URL url = getClass().getResource(path);
        
        if (url != null) {
            System.out.println("Tìm thấy ảnh: " + path);
            ImageIcon original = new ImageIcon(url);
            Image scaled = original.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        
        // Thử đọc file .png
        path = "/icons/" + maMon + ".png";
        url = getClass().getResource(path);
        
        if (url != null) {
            System.out.println("Tìm thấy ảnh: " + path);
            ImageIcon original = new ImageIcon(url);
            Image scaled = original.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        
        // Thử đọc từ thư mục products
        path = "/icons/products/" + maMon + ".jpg";
        url = getClass().getResource(path);
        
        if (url != null) {
            System.out.println("Tìm thấy ảnh: " + path);
            ImageIcon original = new ImageIcon(url);
            Image scaled = original.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        
        System.out.println("Không tìm thấy ảnh cho: " + maMon);
        
    } catch (Exception e) {
        System.out.println("Lỗi tải ảnh: " + e.getMessage());
    }
    
    return createDefaultImage();
}
    
    private ImageIcon createDefaultImage() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRoundRect(5, 5, 40, 40, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.drawString("☕", 15, 38);
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    // ==================== TÌM KIẾM ====================
    
    private void timSanPham() {
        String keyword = txtTimSanPham.getText().trim();
        modelSanPham.setRowCount(0);
        
        List<MonTraSuaDTO> list = monBUS.searchMon(keyword);
        if (list != null) {
            for (MonTraSuaDTO mon : list) {
                double giaMacDinh = monBUS.getGiaTheoSize(mon.getMaMon(), "M");
                ImageIcon image = getProductImage(mon.getMaMon());
                
                Object[] row = {
                    image,
                    mon.getMaMon(),
                    mon.getTenMon(),
                    mon.getLoaiMon(),
                    "M",
                    String.format("%,.0f", giaMacDinh)
                };
                modelSanPham.addRow(row);
            }
        }
    }
    
    private void timTopping() {
        String keyword = txtTimTopping.getText().trim();
        modelTopping.setRowCount(0);
        List<ToppingDTO> list = toppingBUS.searchTopping(keyword);
        if (list != null) {
            for (ToppingDTO top : list) {
                Object[] row = {
                    top.getMaTopping(),
                    top.getTenTopping(),
                    String.format("%,.0f", top.getGiaTopping())
                };
                modelTopping.addRow(row);
            }
        }
    }
    
    // ==================== TOPPING ====================
    
    private void themToppingVaoMon() {
        int row = tblTopping.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn topping cần thêm!");
            return;
        }
        
        if (currentSelectedMon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món trước khi thêm topping!");
            return;
        }
        
        String maTopping = (String) modelTopping.getValueAt(row, 0);
        String tenTopping = (String) modelTopping.getValueAt(row, 1);
        String giaStr = (String) modelTopping.getValueAt(row, 2);
        double gia = Double.parseDouble(giaStr.replace(",", ""));
        int soLuong = (Integer) spnSoLuongTopping.getValue();
        
        ChiTietToppingDTO topping = new ChiTietToppingDTO();
        topping.setMaTopping(maTopping);
        topping.setTenTopping(tenTopping);
        topping.setSoLuong(soLuong);
        topping.setDonGia(gia);
        topping.tinhThanhTien();
        
        selectedToppings.add(topping);
        
        JOptionPane.showMessageDialog(this, "✅ Đã thêm " + tenTopping + " x" + soLuong + " vào món");
    }
    
    private void xoaToppingKhoiDanhSach() {
        if (selectedToppings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có topping nào để xóa!");
            return;
        }
        
        String[] toppingNames = new String[selectedToppings.size()];
        for (int i = 0; i < selectedToppings.size(); i++) {
            toppingNames[i] = selectedToppings.get(i).getTenTopping() + " x" + selectedToppings.get(i).getSoLuong();
        }
        
        String selected = (String) JOptionPane.showInputDialog(this, 
            "Chọn topping muốn xóa:", "Xóa topping", 
            JOptionPane.QUESTION_MESSAGE, null, toppingNames, toppingNames[0]);
        
        if (selected != null) {
            for (int i = 0; i < toppingNames.length; i++) {
                if (toppingNames[i].equals(selected)) {
                    selectedToppings.remove(i);
                    JOptionPane.showMessageDialog(this, "✅ Đã xóa " + selected);
                    break;
                }
            }
        }
    }
    
    // ==================== GIỎ HÀNG ====================
    
    private void themVaoGio() {
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần thêm!");
            return;
        }
        
        // Lấy dữ liệu từ các cột: 0=ảnh, 1=mã, 2=tên, 3=loại, 4=size, 5=giá
        String maMon = (String) modelSanPham.getValueAt(selectedRow, 1);
        String tenMon = (String) modelSanPham.getValueAt(selectedRow, 2);
        String size = (String) cboSize.getSelectedItem();
        int soLuong = (Integer) spnSoLuong.getValue();
        
        double donGia = monBUS.getGiaTheoSize(maMon, size);
        
        if (donGia == 0) {
            JOptionPane.showMessageDialog(this, "Món này không có size " + size + "!");
            return;
        }
        
        GioHangItem item = new GioHangItem();
        item.setMaMon(maMon);
        item.setTenMon(tenMon);
        item.setSize(size);
        item.setSoLuong(soLuong);
        item.setDonGia(donGia);
        item.setToppings(new ArrayList<>(selectedToppings));
        
        gioHang.add(item);
        
        // Xóa topping đã chọn sau khi thêm
        selectedToppings.clear();
        
        loadGioHang();
        tinhTongTien();
        
        String toppingText = item.getToppingText();
        if (!toppingText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "✅ Đã thêm " + soLuong + " " + tenMon + " size " + size + "\n🍬 Topping: " + toppingText);
        } else {
            JOptionPane.showMessageDialog(this, "✅ Đã thêm " + soLuong + " " + tenMon + " size " + size);
        }
    }
    
    private void loadGioHang() {
        modelGioHang.setRowCount(0);
        for (GioHangItem item : gioHang) {
            String tenMonHienThi = item.getTenMon();
            String toppingText = item.getToppingText();
            
            if (!toppingText.isEmpty()) {
                tenMonHienThi = "<html>" + item.getTenMon() + "<br><font color='#0066cc'>+ " + toppingText + "</font></html>";
            }
            
            Object[] row = {
                item.getMaMon(),
                tenMonHienThi,
                item.getSize(),
                item.getSoLuong(),
                String.format("%,.0f", item.getDonGia()),
                String.format("%,.0f", item.getThanhTien())
            };
            modelGioHang.addRow(row);
        }
    }
    
    private void xoaMonKhoiGio() {
        int selectedRow = tblGioHang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!");
            return;
        }
        
        gioHang.remove(selectedRow);
        loadGioHang();
        tinhTongTien();
    }
    
    // ==================== TÍNH TIỀN ====================
    
    private void tinhTongTien() {
        double tong = 0;
        for (GioHangItem item : gioHang) {
            tong += item.getThanhTien();
        }
        txtTongTien.setText(String.format("%,.0f", tong));
        tinhThanhTien();
    }
    
    private void tinhThanhTien() {
        double tong = 0;
        try {
            String tongText = txtTongTien.getText().replace(",", "");
            if (!tongText.isEmpty()) {
                tong = Double.parseDouble(tongText);
            }
        } catch (NumberFormatException e) {
            tong = 0;
        }
        
        double giamGia = 0;
        try {
            String giamGiaText = txtGiamGia.getText().replace(",", "");
            if (!giamGiaText.isEmpty()) {
                giamGia = Double.parseDouble(giamGiaText);
            }
        } catch (NumberFormatException e) {
            giamGia = 0;
        }
        
        int diemSD = 0;
        try {
            String diemText = txtDiemSD.getText().replace(",", "");
            if (!diemText.isEmpty()) {
                diemSD = Integer.parseInt(diemText);
            }
        } catch (NumberFormatException e) {
            diemSD = 0;
        }
        
        double tienGiamTuDiem = diemSD * 1000;
        double thanhTien = tong - giamGia - tienGiamTuDiem;
        if (thanhTien < 0) thanhTien = 0;
        
        txtThanhTien.setText(String.format("%,.0f", thanhTien));
    }
    
    // ==================== HÓA ĐƠN ====================
    
    private void capNhatDiemKhachHang() {
        String selected = (String) cboKhachHang.getSelectedItem();
        if (selected != null && !selected.contains("Khách lẻ")) {
            String maKH = selected.split(" - ")[0];
            KhachHangDTO kh = khachHangBUS.getKhachHangById(maKH);
            if (kh != null) {
                lblDiemTichLuy.setText(kh.getDiemTichLuy() + " điểm");
                txtDiemSD.setText("0");
            }
        } else {
            lblDiemTichLuy.setText("0 điểm");
        }
    }
    
    private void taoHoaDonMoi() {
        maHDHienTai = GenerateID.generateMaHD();
        lblMaHD.setText(maHDHienTai);
        lblNgayLap.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        gioHang.clear();
        selectedToppings.clear();
        loadGioHang();
        txtTongTien.setText("0");
        txtGiamGia.setText("0");
        txtDiemSD.setText("0");
        txtThanhTien.setText("0");
    }
    
    private void thanhToan() {
        if (gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống! Vui lòng thêm món.");
            return;
        }
        
        String maKH = null;
        String tenKH = "Khách lẻ";
        String selectedKH = (String) cboKhachHang.getSelectedItem();
        if (selectedKH != null && !selectedKH.contains("Khách lẻ")) {
            maKH = selectedKH.split(" - ")[0];
            tenKH = selectedKH.split(" - ")[1];
        }
        
        double tongTien = 0;
        double giamGia = 0;
        int diemSD = 0;
        double thanhTien = 0;
        
        try {
            String tongText = txtTongTien.getText().replace(",", "");
            tongTien = tongText.isEmpty() ? 0 : Double.parseDouble(tongText);
            
            String giamText = txtGiamGia.getText().replace(",", "");
            giamGia = giamText.isEmpty() ? 0 : Double.parseDouble(giamText);
            
            String diemText = txtDiemSD.getText().replace(",", "");
            diemSD = diemText.isEmpty() ? 0 : Integer.parseInt(diemText);
            
            String thanhText = txtThanhTien.getText().replace(",", "");
            thanhTien = thanhText.isEmpty() ? 0 : Double.parseDouble(thanhText);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String message = String.format(
            "📋 THÔNG TIN HÓA ĐƠN\n\n" +
            "Mã HD: %s\n" +
            "Khách hàng: %s\n" +
            "Tổng tiền: %,.0f VNĐ\n" +
            "Giảm giá: %,.0f VNĐ\n" +
            "Điểm sử dụng: %d (%,d VNĐ)\n" +
            "Thành tiền: %,.0f VNĐ\n\n" +
            "Xác nhận thanh toán?",
            maHDHienTai, tenKH, tongTien, giamGia, diemSD, diemSD * 1000, thanhTien
        );
        
        int confirm = JOptionPane.showConfirmDialog(this, message, "Xác nhận thanh toán", 
                                                    JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        
        List<ChiTietHoaDonDTO> danhSachChiTiet = new ArrayList<>();
        for (GioHangItem item : gioHang) {
            ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();
            ct.setMaMon(item.getMaMon());
            ct.setTenMon(item.getTenMon());
            ct.setSize(item.getSize());
            ct.setSoLuong(item.getSoLuong());
            ct.setDonGia(item.getDonGia());
            ct.setThanhTien(item.getThanhTien());
            
            List<ChiTietToppingDTO> toppings = new ArrayList<>();
            for (ChiTietToppingDTO top : item.getToppings()) {
                ChiTietToppingDTO newTop = new ChiTietToppingDTO();
                newTop.setMaTopping(top.getMaTopping());
                newTop.setTenTopping(top.getTenTopping());
                newTop.setSoLuong(top.getSoLuong());
                newTop.setDonGia(top.getDonGia());
                newTop.setThanhTien(top.getThanhTien() * item.getSoLuong());
                toppings.add(newTop);
            }
            ct.setDanhSachTopping(toppings);
            ct.tinhThanhTien();
            
            danhSachChiTiet.add(ct);
        }
        
        HoaDonDTO hoaDon = new HoaDonDTO();
        hoaDon.setMaHD(maHDHienTai);
        hoaDon.setNgayLap(new Timestamp(System.currentTimeMillis()));
        hoaDon.setMaNV(maNVDangNhap);
        hoaDon.setMaKH(maKH);
        hoaDon.setTongTien(tongTien);
        hoaDon.setGiamGia(giamGia);
        hoaDon.setDiemSuDung(diemSD);
        hoaDon.setThanhTien(thanhTien);
        
        boolean success = hoaDonBUS.saveHoaDon(hoaDon, danhSachChiTiet);
        
        if (success) {
            if (maKH != null && !maKH.equals("KH000")) {
                int diemMoi = (int) (thanhTien / 10000);
                khachHangBUS.updateDiem(maKH, diemMoi);
                JOptionPane.showMessageDialog(this, "✅ Thanh toán thành công!\n✅ Tích lũy " + diemMoi + " điểm!");
            } else {
                JOptionPane.showMessageDialog(this, "✅ Thanh toán thành công!\nCảm ơn quý khách!");
            }
            taoHoaDonMoi();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi lưu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== INNER CLASS ====================
    
    class GioHangItem {
        private String maMon;
        private String tenMon;
        private String size;
        private int soLuong;
        private double donGia;
        private List<ChiTietToppingDTO> toppings;
        
        public GioHangItem() {
            toppings = new ArrayList<>();
        }
        
        public double getThanhTien() {
            double tong = soLuong * donGia;
            for (ChiTietToppingDTO topping : toppings) {
                tong += topping.getThanhTien() * soLuong;
            }
            return tong;
        }
        
        public String getToppingText() {
            if (toppings.isEmpty()) return "";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < toppings.size(); i++) {
                ChiTietToppingDTO top = toppings.get(i);
                sb.append(top.getTenTopping()).append(" x").append(top.getSoLuong());
                if (i < toppings.size() - 1) sb.append(", ");
            }
            return sb.toString();
        }
        
        // Getters and Setters
        public String getMaMon() { return maMon; }
        public void setMaMon(String maMon) { this.maMon = maMon; }
        public String getTenMon() { return tenMon; }
        public void setTenMon(String tenMon) { this.tenMon = tenMon; }
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        public int getSoLuong() { return soLuong; }
        public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
        public double getDonGia() { return donGia; }
        public void setDonGia(double donGia) { this.donGia = donGia; }
        public List<ChiTietToppingDTO> getToppings() { return toppings; }
        public void setToppings(List<ChiTietToppingDTO> toppings) { this.toppings = toppings; }
    }
}