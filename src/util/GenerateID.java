package util;

import java.sql.*;

public class GenerateID {
    
    // Tạo mã nhân viên: NV + số thứ tự (VD: NV001)
    public static String generateMaNV() {
        String sql = "SELECT TOP 1 MaNV FROM NhanVien ORDER BY MaNV DESC";
        return generateID(sql, "NV");
    }
    
    // Tạo mã khách hàng: KH + số thứ tự
    public static String generateMaKH() {
        String sql = "SELECT TOP 1 MaKH FROM KhachHang ORDER BY MaKH DESC";
        return generateID(sql, "KH");
    }
    
    // Tạo mã món: MON + số thứ tự
    public static String generateMaMon() {
        String sql = "SELECT TOP 1 MaMon FROM MonTraSua ORDER BY MaMon DESC";
        return generateID(sql, "MON");
    }
    
    // Tạo mã hóa đơn: HD + số thứ tự
    public static String generateMaHD() {
        String sql = "SELECT TOP 1 MaHD FROM HoaDon ORDER BY MaHD DESC";
        return generateID(sql, "HD");
    }
    
    // Tạo mã topping: TP + số thứ tự
    public static String generateMaTopping() {
        String sql = "SELECT TOP 1 MaTopping FROM Topping ORDER BY MaTopping DESC";
        return generateID(sql, "TP");
    }
    
    // Hàm xử lý chung
    private static String generateID(String sql, String prefix) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String lastID = rs.getString(1);
                int number = Integer.parseInt(lastID.substring(prefix.length()));
                number++;
                return String.format("%s%03d", prefix, number);
            } else {
                return prefix + "001";
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return prefix + "001";
        }
    }
}