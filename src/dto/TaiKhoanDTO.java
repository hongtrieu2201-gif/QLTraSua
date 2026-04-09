package dto;

public class TaiKhoanDTO {
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro;
    private boolean trangThai;
    
    public TaiKhoanDTO() {}
    
    public TaiKhoanDTO(String tenDangNhap, String matKhau, String vaiTro, boolean trangThai) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
    }
    
    // Getters
    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public String getVaiTro() { return vaiTro; }
    public boolean isTrangThai() { return trangThai; }
    
    // Setters
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}