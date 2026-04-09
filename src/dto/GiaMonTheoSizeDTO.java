package dto;

public class GiaMonTheoSizeDTO {
    private String maMon;
    private String maSize;
    private double giaBan;
    private String tenMon;
    private String tenSize;
    
    public GiaMonTheoSizeDTO() {}
    
    // Getters
    public String getMaMon() { return maMon; }
    public String getMaSize() { return maSize; }
    public double getGiaBan() { return giaBan; }
    public String getTenMon() { return tenMon; }
    public String getTenSize() { return tenSize; }
    
    // Setters
    public void setMaMon(String maMon) { this.maMon = maMon; }
    public void setMaSize(String maSize) { this.maSize = maSize; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }
    public void setTenSize(String tenSize) { this.tenSize = tenSize; }
}