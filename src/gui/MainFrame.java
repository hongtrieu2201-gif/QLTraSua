package gui;

import util.IconUtil;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private String role;
    private String username;
    private JTabbedPane tabbedPane;
    private JLabel lblUserInfo;
    
    public MainFrame(String role, String username) {
        this.role = role;
        this.username = username;
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("☕ Phần mềm Quản lý Trà Sữa - TeaShop Management");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          getContentPane().setBackground(new Color(240, 248, 255));
        // Set icon cho Frame
        try {
            setIconImage(IconUtil.getIcon("logo.png", 32, 32).getImage());
        } catch (Exception e) {}
        
        // Menu Bar với ICON
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(245, 245, 220));
        
        // ========== HỆ THỐNG MENU ==========
        JMenu mnHeThong = new JMenu(" 📋 Hệ thống");
        mnHeThong.setFont(new Font("Segoe UI", Font.BOLD, 13));
        try {
            mnHeThong.setIcon(IconUtil.getIcon("system.png", 20, 20));
        } catch (Exception e) {}
        
        JMenuItem miDoiMatKhau = new JMenuItem(" 🔐 Đổi mật khẩu");
        JMenuItem miDangXuat = new JMenuItem(" 🚪 Đăng xuất");
        JMenuItem miThoat = new JMenuItem(" ❌ Thoát");
        
        try {
            miDoiMatKhau.setIcon(IconUtil.getIcon("lock.png", 16, 16));
            miDangXuat.setIcon(IconUtil.getIcon("logout.png", 16, 16));
            miThoat.setIcon(IconUtil.getIcon("exit.png", 16, 16));
        } catch (Exception e) {}
        
        // ========== BÁN HÀNG MENU ==========
        JMenu mnBanHang = new JMenu(" 🛒 Bán hàng");
        mnBanHang.setFont(new Font("Segoe UI", Font.BOLD, 13));
        try {
            mnBanHang.setIcon(IconUtil.getIcon("cart.png", 20, 20));
        } catch (Exception e) {}
        
        JMenuItem miTaoHoaDon = new JMenuItem(" 🧾 Tạo hóa đơn mới");
        JMenuItem miXemHoaDon = new JMenuItem(" 📄 Xem hóa đơn");
        
        try {
            miTaoHoaDon.setIcon(IconUtil.getIcon("bill.png", 16, 16));
            miXemHoaDon.setIcon(IconUtil.getIcon("view.png", 16, 16));
        } catch (Exception e) {}
        
        // ========== DANH MỤC MENU ==========
        JMenu mnDanhMuc = new JMenu(" 📦 Danh mục");
        mnDanhMuc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        try {
            mnDanhMuc.setIcon(IconUtil.getIcon("category.png", 20, 20));
        } catch (Exception e) {}
        
        JMenuItem miNhanVien = new JMenuItem(" 👥 Nhân viên");
        JMenuItem miKhachHang = new JMenuItem(" 👤 Khách hàng");
        JMenuItem miMon = new JMenuItem(" 🥤 Món trà sữa");
        JMenuItem miTopping = new JMenuItem(" 🍬 Topping");
        
        try {
            miNhanVien.setIcon(IconUtil.getIcon("employee.png", 16, 16));
            miKhachHang.setIcon(IconUtil.getIcon("customer.png", 16, 16));
            miMon.setIcon(IconUtil.getIcon("drink.png", 16, 16));
            miTopping.setIcon(IconUtil.getIcon("topping.png", 16, 16));
        } catch (Exception e) {}
        
        // ========== THỐNG KÊ MENU ==========
        JMenu mnThongKe = new JMenu(" 📊 Thống kê");
        mnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        try {
            mnThongKe.setIcon(IconUtil.getIcon("statistics.png", 20, 20));
        } catch (Exception e) {}
        
        JMenuItem miDoanhThu = new JMenuItem(" 💰 Doanh thu");
        try {
            miDoanhThu.setIcon(IconUtil.getIcon("revenue.png", 16, 16));
        } catch (Exception e) {}
        
        // ========== TRỢ GIÚP MENU ==========
        JMenu mnTroGiup = new JMenu(" ❓ Trợ giúp");
        mnTroGiup.setFont(new Font("Segoe UI", Font.BOLD, 13));
        try {
            mnTroGiup.setIcon(IconUtil.getIcon("help.png", 20, 20));
        } catch (Exception e) {}
        
        JMenuItem miHuongDan = new JMenuItem(" 📖 Hướng dẫn sử dụng");
        JMenuItem miGioiThieu = new JMenuItem(" ℹ️ Giới thiệu");
        
        try {
            miHuongDan.setIcon(IconUtil.getIcon("guide.png", 16, 16));
            miGioiThieu.setIcon(IconUtil.getIcon("about.png", 16, 16));
        } catch (Exception e) {}
        
        // Add menu items
        mnHeThong.add(miDoiMatKhau);
        mnHeThong.addSeparator();
        mnHeThong.add(miDangXuat);
        mnHeThong.add(miThoat);
        
        mnBanHang.add(miTaoHoaDon);
        mnBanHang.add(miXemHoaDon);
        
        mnDanhMuc.add(miNhanVien);
        mnDanhMuc.add(miKhachHang);
        mnDanhMuc.addSeparator();
        mnDanhMuc.add(miMon);
        mnDanhMuc.add(miTopping);
        
        mnThongKe.add(miDoanhThu);
        
        mnTroGiup.add(miHuongDan);
        mnTroGiup.add(miGioiThieu);
        
        // Add to menu bar
        menuBar.add(mnHeThong);
        menuBar.add(mnBanHang);
        menuBar.add(mnDanhMuc);
        menuBar.add(mnThongKe);
        menuBar.add(mnTroGiup);
        
        // User info
        menuBar.add(Box.createHorizontalGlue());
        lblUserInfo = new JLabel(" 👤 " + username + " | 🔑 " + role + " ");
        lblUserInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUserInfo.setForeground(new Color(0, 100, 0));
        menuBar.add(lblUserInfo);
        
        setJMenuBar(menuBar);
        
        // ========== TABBED PANE VỚI ICON ==========
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Thêm các tab với icon
        BanHangGUI banHangGUI = new BanHangGUI();
        tabbedPane.addTab(" 🛒 Bán hàng", banHangGUI);
        try {
            tabbedPane.setIconAt(0, IconUtil.getIcon("cart.png", 20, 20));
        } catch (Exception e) {}
        
        NhanVienGUI nhanVienGUI = new NhanVienGUI();
        tabbedPane.addTab(" 👥 Quản lý nhân viên", nhanVienGUI);
        try {
            tabbedPane.setIconAt(1, IconUtil.getIcon("employee.png", 20, 20));
        } catch (Exception e) {}
        
        KhachHangGUI khachHangGUI = new KhachHangGUI();
        tabbedPane.addTab(" 👤 Quản lý khách hàng", khachHangGUI);
        try {
            tabbedPane.setIconAt(2, IconUtil.getIcon("customer.png", 20, 20));
        } catch (Exception e) {}
        
        MonTraSuaGUI monGUI = new MonTraSuaGUI();
        tabbedPane.addTab(" 🥤 Quản lý món", monGUI);
        try {
            tabbedPane.setIconAt(3, IconUtil.getIcon("drink.png", 20, 20));
        } catch (Exception e) {}
        
        ToppingGUI toppingGUI = new ToppingGUI();
        tabbedPane.addTab(" 🍬 Quản lý topping", toppingGUI);
        try {
            tabbedPane.setIconAt(4, IconUtil.getIcon("topping.png", 20, 20));
        } catch (Exception e) {}
        
        ThongKeGUI thongKeGUI = new ThongKeGUI();
        tabbedPane.addTab(" 📊 Thống kê doanh thu", thongKeGUI);
        try {
            tabbedPane.setIconAt(5, IconUtil.getIcon("statistics.png", 20, 20));
        } catch (Exception e) {}
        
        add(tabbedPane);
         QuanLyHoaDonGUI quanLyHoaDonGUI = new QuanLyHoaDonGUI();
    tabbedPane.addTab("📄 Quản lý hóa đơn", quanLyHoaDonGUI);
    try {
        tabbedPane.setIconAt(6, IconUtil.getIcon("bill.png", 20, 20));
    } catch (Exception e) {}
        
       
    }
}