package dto;

public class NhanVienDTO {
    private String maNV;
    private String hoTen;
    private String soDienThoai;
    private String diaChi;
    private String chucVu;
    private String tenDangNhap;
    private boolean trangThai;
    
    // Constructors
    public NhanVienDTO() {}
    
    public NhanVienDTO(String maNV, String hoTen, String soDienThoai, 
                      String diaChi, String chucVu, String tenDangNhap, boolean trangThai) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
        this.tenDangNhap = tenDangNhap;
        this.trangThai = trangThai;
    }
    
    // Getters and Setters
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    
    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
    
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    
    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}