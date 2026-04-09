package dto;

public class KhachHangDTO {
    private String maKH;
    private String hoTen;
    private String soDienThoai;
    private int diemTichLuy;
    
    public KhachHangDTO() {}
    
    public KhachHangDTO(String maKH, String hoTen, String soDienThoai, int diemTichLuy) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diemTichLuy = diemTichLuy;
    }
    
    // Getters
    public String getMaKH() { return maKH; }
    public String getHoTen() { return hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public int getDiemTichLuy() { return diemTichLuy; }
    
    // Setters
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }
}