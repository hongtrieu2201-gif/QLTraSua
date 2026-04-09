package dto;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDTO {
    
    // Fields
    private int maCTHD;
    private String maHD;
    private String maMon;
    private String tenMon;
    private String size;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private String ghiChu;
    private List<ChiTietToppingDTO> danhSachTopping;
    
    private static final DecimalFormat DF = new DecimalFormat("#,###");
    
    // Constructor mặc định
    public ChiTietHoaDonDTO() {
        this.danhSachTopping = new ArrayList<>();
        this.soLuong = 1;
        this.donGia = 0;
        this.thanhTien = 0;
    }
    
    // Constructor đầy đủ
    public ChiTietHoaDonDTO(String maHD, String maMon, String tenMon, String size, 
                            int soLuong, double donGia) {
        this.maHD = maHD;
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.size = size;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia;
        this.danhSachTopping = new ArrayList<>();
    }
    
    // Constructor từ database
    public ChiTietHoaDonDTO(int maCTHD, String maHD, String maMon, String tenMon, 
                            String size, int soLuong, double donGia, double thanhTien) {
        this.maCTHD = maCTHD;
        this.maHD = maHD;
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.size = size;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.danhSachTopping = new ArrayList<>();
    }
    
    // Tính lại thành tiền
    public void tinhThanhTien() {
        // Tính tiền món chính
        this.thanhTien = this.soLuong * this.donGia;
        
        // Cộng thêm tiền topping
        if (this.danhSachTopping != null) {
            for (ChiTietToppingDTO topping : this.danhSachTopping) {
                if (topping != null) {
                    this.thanhTien += topping.getThanhTien();
                }
            }
        }
    }
    
    // Thêm topping
    public void addTopping(ChiTietToppingDTO topping) {
        if (this.danhSachTopping == null) {
            this.danhSachTopping = new ArrayList<>();
        }
        this.danhSachTopping.add(topping);
        tinhThanhTien();
    }
    
    // Xóa topping
    public void removeTopping(ChiTietToppingDTO topping) {
        if (this.danhSachTopping != null) {
            this.danhSachTopping.remove(topping);
            tinhThanhTien();
        }
    }
    
    // Xóa tất cả topping
    public void clearToppings() {
        if (this.danhSachTopping != null) {
            this.danhSachTopping.clear();
            tinhThanhTien();
        }
    }
    
    // Format giá
    public String getDonGiaFormatted() {
        return DF.format(donGia) + " VNĐ";
    }
    
    public String getThanhTienFormatted() {
        return DF.format(thanhTien) + " VNĐ";
    }
    
    // Lấy text topping để hiển thị
    public String getToppingText() {
        if (danhSachTopping == null || danhSachTopping.isEmpty()) {
            return "Không có topping";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < danhSachTopping.size(); i++) {
            ChiTietToppingDTO top = danhSachTopping.get(i);
            if (top != null) {
                sb.append(top.getTenTopping());
                if (top.getSoLuong() > 1) {
                    sb.append(" x").append(top.getSoLuong());
                }
                if (i < danhSachTopping.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }
    
    // Kiểm tra hợp lệ
    public boolean isValid() {
        return maHD != null && !maHD.trim().isEmpty()
                && maMon != null && !maMon.trim().isEmpty()
                && soLuong > 0
                && donGia >= 0;
    }
    
    // Getters
    public int getMaCTHD() { return maCTHD; }
    public String getMaHD() { return maHD; }
    public String getMaMon() { return maMon; }
    public String getTenMon() { return tenMon; }
    public String getSize() { return size; }
    public int getSoLuong() { return soLuong; }
    public double getDonGia() { return donGia; }
    public double getThanhTien() { return thanhTien; }
    public String getGhiChu() { return ghiChu; }
    public List<ChiTietToppingDTO> getDanhSachTopping() { return danhSachTopping; }
    
    // Setters
    public void setMaCTHD(int maCTHD) { this.maCTHD = maCTHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public void setMaMon(String maMon) { this.maMon = maMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }
    public void setSize(String size) { this.size = size; }
    public void setSoLuong(int soLuong) { 
        this.soLuong = soLuong; 
        tinhThanhTien();
    }
    public void setDonGia(double donGia) { 
        this.donGia = donGia; 
        tinhThanhTien();
    }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public void setDanhSachTopping(List<ChiTietToppingDTO> danhSachTopping) { 
        this.danhSachTopping = danhSachTopping; 
        tinhThanhTien();
    }
    
    @Override
    public String toString() {
        return tenMon + " (" + size + ") x" + soLuong + " - " + getThanhTienFormatted();
    }
}