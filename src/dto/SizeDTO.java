package dto;

public class SizeDTO {
    private String maSize;
    private String tenSize;
    private double heSo;
    
    public SizeDTO() {}
    
    public SizeDTO(String maSize, String tenSize, double heSo) {
        this.maSize = maSize;
        this.tenSize = tenSize;
        this.heSo = heSo;
    }
    
    // Getters
    public String getMaSize() { return maSize; }
    public String getTenSize() { return tenSize; }
    public double getHeSo() { return heSo; }
    
    // Setters
    public void setMaSize(String maSize) { this.maSize = maSize; }
    public void setTenSize(String tenSize) { this.tenSize = tenSize; }
    public void setHeSo(double heSo) { this.heSo = heSo; }
}