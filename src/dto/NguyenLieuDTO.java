package dto;

public class NguyenLieuDTO {
    private String maNL;
    private String tenNguyenLieu;
    private String donViTinh;
    private int soLuongTon;
    private int mucCanhBao;
    private double giaNhap;
    private boolean trangThai;
    
    public NguyenLieuDTO() {}
    
    // Getters
    public String getMaNL() { return maNL; }
    public String getTenNguyenLieu() { return tenNguyenLieu; }
    public String getDonViTinh() { return donViTinh; }
    public int getSoLuongTon() { return soLuongTon; }
    public int getMucCanhBao() { return mucCanhBao; }
    public double getGiaNhap() { return giaNhap; }
    public boolean isTrangThai() { return trangThai; }
    
    // Setters
    public void setMaNL(String maNL) { this.maNL = maNL; }
    public void setTenNguyenLieu(String tenNguyenLieu) { this.tenNguyenLieu = tenNguyenLieu; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }
    public void setMucCanhBao(int mucCanhBao) { this.mucCanhBao = mucCanhBao; }
    public void setGiaNhap(double giaNhap) { this.giaNhap = giaNhap; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}