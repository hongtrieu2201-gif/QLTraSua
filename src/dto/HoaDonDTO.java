package dto;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class HoaDonDTO {
    private String maHD;
    private Timestamp ngayLap;
    private String maNV;
    private String maKH;
    private double tongTien;      // double, không phải String
    private double giamGia;       // double, không phải String
    private int diemSuDung;       // int, không phải String
    private double thanhTien;     // double, không phải String
    private String tenNV;
    private String tenKH;
    
    public HoaDonDTO() {}
    
    // Getters
    public String getMaHD() { return maHD; }
    public Timestamp getNgayLap() { return ngayLap; }
    public String getMaNV() { return maNV; }
    public String getMaKH() { return maKH; }
    public double getTongTien() { return tongTien; }
    public double getGiamGia() { return giamGia; }
    public int getDiemSuDung() { return diemSuDung; }
    public double getThanhTien() { return thanhTien; }
    public String getTenNV() { return tenNV; }
    public String getTenKH() { return tenKH; }
    
    // Setters
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public void setNgayLap(Timestamp ngayLap) { this.ngayLap = ngayLap; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    public void setGiamGia(double giamGia) { this.giamGia = giamGia; }
    public void setDiemSuDung(int diemSuDung) { this.diemSuDung = diemSuDung; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }
    
    // Helper methods
    public String getNgayLapFormatted() {
        if (ngayLap == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ngayLap);
    }
    
    public String getTongTienFormatted() {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(tongTien) + " VNĐ";
    }
    
    public String getThanhTienFormatted() {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(thanhTien) + " VNĐ";
    }
}