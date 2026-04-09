package dto;

import java.text.DecimalFormat;


public class ChiTietToppingDTO {
    
    // ==================== CÁC TRƯỜNG DỮ LIỆU ====================
    
    private int maCTTopping;        
    private int maCTHD;              
    private String maTopping;        
    private String tenTopping;       
    private int soLuong;             
    private double donGia;           
    private double thanhTien;        
    
    
    private static final DecimalFormat DF = new DecimalFormat("#,###");
    
    
    
    
     
     
    public ChiTietToppingDTO() {
        this.soLuong = 1;
        this.donGia = 0;
        this.thanhTien = 0;
    }
    
    /**
     * Constructor đầy đủ tham số
     * @param maTopping Mã topping
     * @param tenTopping Tên topping
     * @param soLuong Số lượng
     * @param donGia Đơn giá
     */
    public ChiTietToppingDTO(String maTopping, String tenTopping, int soLuong, double donGia) {
        this.maTopping = maTopping;
        this.tenTopping = tenTopping;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia;
    }
    
    /**
     * Constructor dùng khi lấy dữ liệu từ database
     * @param maCTTopping Mã chi tiết topping
     * @param maCTHD Mã chi tiết hóa đơn
     * @param maTopping Mã topping
     * @param tenTopping Tên topping
     * @param soLuong Số lượng
     * @param donGia Đơn giá
     * @param thanhTien Thành tiền
     */
    public ChiTietToppingDTO(int maCTTopping, int maCTHD, String maTopping, 
                             String tenTopping, int soLuong, double donGia, double thanhTien) {
        this.maCTTopping = maCTTopping;
        this.maCTHD = maCTHD;
        this.maTopping = maTopping;
        this.tenTopping = tenTopping;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }
    
    // ==================== PHƯƠNG THỨC TÍNH TOÁN ====================
    
    /**
     * Tính lại thành tiền dựa trên số lượng và đơn giá
     * Gọi phương thức này sau khi thay đổi số lượng hoặc đơn giá
     */
    public void tinhThanhTien() {
        this.thanhTien = this.soLuong * this.donGia;
    }
    
    /**
     * Cập nhật số lượng và tự động tính lại thành tiền
     * @param soLuong Số lượng mới
     */
    public void setSoLuongAndUpdate(int soLuong) {
        this.soLuong = soLuong;
        tinhThanhTien();
    }
    
    /**
     * Cập nhật đơn giá và tự động tính lại thành tiền
     * @param donGia Đơn giá mới
     */
    public void setDonGiaAndUpdate(double donGia) {
        this.donGia = donGia;
        tinhThanhTien();
    }
    
    // ==================== PHƯƠNG THỨC HIỂN THỊ ====================
    
    /**
     * Lấy đơn giá đã được format (có dấu phân cách và đơn vị VNĐ)
     * @return Chuỗi đơn giá đã format (VD: "5,000 VNĐ")
     */
    public String getDonGiaFormatted() {
        return DF.format(donGia) + " VNĐ";
    }
    
    /**
     * Lấy thành tiền đã được format (có dấu phân cách và đơn vị VNĐ)
     * @return Chuỗi thành tiền đã format (VD: "10,000 VNĐ")
     */
    public String getThanhTienFormatted() {
        return DF.format(thanhTien) + " VNĐ";
    }
    
    /**
     * Lấy thông tin topping dạng text để hiển thị trong giỏ hàng
     * @return Chuỗi hiển thị (VD: "Trân châu x1" hoặc "Trân châu x2")
     */
    public String getDisplayText() {
        if (soLuong == 1) {
            return tenTopping;
        } else {
            return tenTopping + " x" + soLuong;
        }
    }
    
    /**
     * Lấy thông tin topping chi tiết để hiển thị trong hóa đơn
     * @return Chuỗi chi tiết (VD: "Trân châu x1 - 5,000 VNĐ")
     */
    public String getDetailText() {
        return getDisplayText() + " - " + getThanhTienFormatted();
    }
    
    /**
     * Kiểm tra xem topping có hợp lệ không
     * @return true nếu topping hợp lệ (có mã, tên, số lượng > 0, đơn giá >= 0)
     */
    public boolean isValid() {
        return maTopping != null && !maTopping.trim().isEmpty()
                && tenTopping != null && !tenTopping.trim().isEmpty()
                && soLuong > 0
                && donGia >= 0;
    }
    
    // ==================== GETTERS ====================
    
    public int getMaCTTopping() {
        return maCTTopping;
    }
    
    public int getMaCTHD() {
        return maCTHD;
    }
    
    public String getMaTopping() {
        return maTopping;
    }
    
    public String getTenTopping() {
        return tenTopping;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public double getDonGia() {
        return donGia;
    }
    
    public double getThanhTien() {
        return thanhTien;
    }
    
    // ==================== SETTERS ====================
    
    public void setMaCTTopping(int maCTTopping) {
        this.maCTTopping = maCTTopping;
    }
    
    public void setMaCTHD(int maCTHD) {
        this.maCTHD = maCTHD;
    }
    
    public void setMaTopping(String maTopping) {
        this.maTopping = maTopping;
    }
    
    public void setTenTopping(String tenTopping) {
        this.tenTopping = tenTopping;
    }
    
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        tinhThanhTien();
    }
    
    public void setDonGia(double donGia) {
        this.donGia = donGia;
        tinhThanhTien();
    }
    
    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
    
    // ==================== OVERRIDE METHODS ====================
    
    @Override
    public String toString() {
        return getDetailText();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ChiTietToppingDTO that = (ChiTietToppingDTO) obj;
        
        if (maCTTopping != that.maCTTopping) return false;
        if (maCTHD != that.maCTHD) return false;
        if (soLuong != that.soLuong) return false;
        if (Double.compare(that.donGia, donGia) != 0) return false;
        if (Double.compare(that.thanhTien, thanhTien) != 0) return false;
        if (maTopping != null ? !maTopping.equals(that.maTopping) : that.maTopping != null)
            return false;
        return tenTopping != null ? tenTopping.equals(that.tenTopping) : that.tenTopping == null;
    }
    
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = maCTTopping;
        result = 31 * result + maCTHD;
        result = 31 * result + (maTopping != null ? maTopping.hashCode() : 0);
        result = 31 * result + (tenTopping != null ? tenTopping.hashCode() : 0);
        result = 31 * result + soLuong;
        temp = Double.doubleToLongBits(donGia);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(thanhTien);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}