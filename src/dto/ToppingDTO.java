package dto;

public class ToppingDTO {
    private String maTopping;
    private String tenTopping;
    private double giaTopping;
    private boolean trangThai;
    
    public ToppingDTO() {}
    
    public ToppingDTO(String maTopping, String tenTopping, double giaTopping, boolean trangThai) {
        this.maTopping = maTopping;
        this.tenTopping = tenTopping;
        this.giaTopping = giaTopping;
        this.trangThai = trangThai;
    }
    
    // Getters
    public String getMaTopping() { return maTopping; }
    public String getTenTopping() { return tenTopping; }
    public double getGiaTopping() { return giaTopping; }
    public boolean isTrangThai() { return trangThai; }
    
    // Setters
    public void setMaTopping(String maTopping) { this.maTopping = maTopping; }
    public void setTenTopping(String tenTopping) { this.tenTopping = tenTopping; }
    public void setGiaTopping(double giaTopping) { this.giaTopping = giaTopping; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}