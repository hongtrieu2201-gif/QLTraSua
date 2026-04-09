package dto;

public class MonTraSuaDTO {
    private String maMon;
    private String tenMon;
    private String loaiMon;
    private String hinhAnh;
    private boolean trangThai;
    
    // Không còn size và giaBan ở đây nữa
    
    public MonTraSuaDTO() {}
    
    // Getters
    public String getMaMon() { return maMon; }
    public String getTenMon() { return tenMon; }
    public String getLoaiMon() { return loaiMon; }
    public String getHinhAnh() { return hinhAnh; }
    public boolean isTrangThai() { return trangThai; }
    
    // Setters
    public void setMaMon(String maMon) { this.maMon = maMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }
    public void setLoaiMon(String loaiMon) { this.loaiMon = loaiMon; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}