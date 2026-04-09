package dao;

import util.DBConnection;
import java.sql.*;
import java.util.*;

public class ThongKeDAO {
    
    // Thống kê theo ngày
    public Map<String, Object> thongKeTheoNgay(String ngay) {
        Map<String, Object> result = new HashMap<>();
        String sql = "SELECT "
                   + "COALESCE(SUM(ThanhTien), 0) AS DoanhThu, "
                   + "COUNT(*) AS SoHoaDon, "
                   + "COALESCE(AVG(ThanhTien), 0) AS TrungBinh "
                   + "FROM HoaDon "
                   + "WHERE CAST(NgayLap AS DATE) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ngay);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.put("doanhThu", rs.getLong("DoanhThu"));
                    result.put("soHoaDon", rs.getInt("SoHoaDon"));
                    result.put("trungBinh", rs.getDouble("TrungBinh"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    // Chi tiết doanh thu theo giờ trong ngày
    public List<Object[]> chiTietDoanhThuTheoNgay(String ngay) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT "
                   + "DATEPART(HOUR, NgayLap) AS Gio, "
                   + "COUNT(*) AS SoHoaDon, "
                   + "SUM(ThanhTien) AS DoanhThu "
                   + "FROM HoaDon "
                   + "WHERE CAST(NgayLap AS DATE) = ? "
                   + "GROUP BY DATEPART(HOUR, NgayLap) "
                   + "ORDER BY Gio";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ngay);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("Gio") + ":00",
                        rs.getInt("SoHoaDon"),
                        String.format("%,d VNĐ", rs.getLong("DoanhThu"))
                    };
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Thống kê theo tháng
    public Map<String, Object> thongKeTheoThang(int thang, int nam) {
        Map<String, Object> result = new HashMap<>();
        String sql = "SELECT "
                   + "COALESCE(SUM(ThanhTien), 0) AS DoanhThu, "
                   + "COUNT(*) AS SoHoaDon, "
                   + "COALESCE(AVG(ThanhTien), 0) AS TrungBinh "
                   + "FROM HoaDon "
                   + "WHERE MONTH(NgayLap) = ? AND YEAR(NgayLap) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.put("doanhThu", rs.getLong("DoanhThu"));
                    result.put("soHoaDon", rs.getInt("SoHoaDon"));
                    result.put("trungBinh", rs.getDouble("TrungBinh"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    // Chi tiết doanh thu theo ngày trong tháng
    public List<Object[]> chiTietDoanhThuTheoThang(int thang, int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT "
                   + "DAY(NgayLap) AS Ngay, "
                   + "COUNT(*) AS SoHoaDon, "
                   + "SUM(ThanhTien) AS DoanhThu "
                   + "FROM HoaDon "
                   + "WHERE MONTH(NgayLap) = ? AND YEAR(NgayLap) = ? "
                   + "GROUP BY DAY(NgayLap) "
                   + "ORDER BY Ngay";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        "Ngày " + rs.getInt("Ngay"),
                        rs.getInt("SoHoaDon"),
                        String.format("%,d VNĐ", rs.getLong("DoanhThu"))
                    };
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Thống kê theo năm
    public Map<String, Object> thongKeTheoNam(int nam) {
        Map<String, Object> result = new HashMap<>();
        String sql = "SELECT "
                   + "COALESCE(SUM(ThanhTien), 0) AS DoanhThu, "
                   + "COUNT(*) AS SoHoaDon, "
                   + "COALESCE(AVG(ThanhTien), 0) AS TrungBinh "
                   + "FROM HoaDon "
                   + "WHERE YEAR(NgayLap) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.put("doanhThu", rs.getLong("DoanhThu"));
                    result.put("soHoaDon", rs.getInt("SoHoaDon"));
                    result.put("trungBinh", rs.getDouble("TrungBinh"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    // Chi tiết doanh thu theo tháng trong năm
    public List<Object[]> chiTietDoanhThuTheoNam(int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT "
                   + "MONTH(NgayLap) AS Thang, "
                   + "COUNT(*) AS SoHoaDon, "
                   + "SUM(ThanhTien) AS DoanhThu "
                   + "FROM HoaDon "
                   + "WHERE YEAR(NgayLap) = ? "
                   + "GROUP BY MONTH(NgayLap) "
                   + "ORDER BY Thang";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        "Tháng " + rs.getInt("Thang"),
                        rs.getInt("SoHoaDon"),
                        String.format("%,d VNĐ", rs.getLong("DoanhThu"))
                    };
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Top món bán chạy theo ngày
    public List<Object[]> topMonBanChayTheoNgay(String ngay) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP 10 "
                   + "m.MaMon, m.TenMon, "
                   + "SUM(ct.SoLuong) AS SoLuong, "
                   + "SUM(ct.ThanhTien) AS DoanhThu "
                   + "FROM ChiTietHoaDon ct "
                   + "JOIN MonTraSua m ON ct.MaMon = m.MaMon "
                   + "JOIN HoaDon h ON ct.MaHD = h.MaHD "
                   + "WHERE CAST(h.NgayLap AS DATE) = ? "
                   + "GROUP BY m.MaMon, m.TenMon "
                   + "ORDER BY SoLuong DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ngay);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MaMon"),
                        rs.getString("TenMon"),
                        rs.getInt("SoLuong"),
                        String.format("%,d VNĐ", rs.getLong("DoanhThu"))
                    };
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Top món bán chạy theo tháng
    public List<Object[]> topMonBanChayTheoThang(int thang, int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP 10 "
                   + "m.MaMon, m.TenMon, "
                   + "SUM(ct.SoLuong) AS SoLuong, "
                   + "SUM(ct.ThanhTien) AS DoanhThu "
                   + "FROM ChiTietHoaDon ct "
                   + "JOIN MonTraSua m ON ct.MaMon = m.MaMon "
                   + "JOIN HoaDon h ON ct.MaHD = h.MaHD "
                   + "WHERE MONTH(h.NgayLap) = ? AND YEAR(h.NgayLap) = ? "
                   + "GROUP BY m.MaMon, m.TenMon "
                   + "ORDER BY SoLuong DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MaMon"),
                        rs.getString("TenMon"),
                        rs.getInt("SoLuong"),
                        String.format("%,d VNĐ", rs.getLong("DoanhThu"))
                    };
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Top món bán chạy theo năm
    public List<Object[]> topMonBanChayTheoNam(int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP 10 "
                   + "m.MaMon, m.TenMon, "
                   + "SUM(ct.SoLuong) AS SoLuong, "
                   + "SUM(ct.ThanhTien) AS DoanhThu "
                   + "FROM ChiTietHoaDon ct "
                   + "JOIN MonTraSua m ON ct.MaMon = m.MaMon "
                   + "JOIN HoaDon h ON ct.MaHD = h.MaHD "
                   + "WHERE YEAR(h.NgayLap) = ? "
                   + "GROUP BY m.MaMon, m.TenMon "
                   + "ORDER BY SoLuong DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MaMon"),
                        rs.getString("TenMon"),
                        rs.getInt("SoLuong"),
                        String.format("%,d VNĐ", rs.getLong("DoanhThu"))
                    };
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Top khách hàng thân thiết theo ngày
    public List<Object[]> topKhachHangTheoNgay(String ngay) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP 10 "
                   + "kh.MaKH, kh.HoTen, kh.SoDienThoai, kh.DiemTichLuy, "
                   + "SUM(h.ThanhTien) AS TongChiTieu "
                   + "FROM HoaDon h "
                   + "JOIN KhachHang kh ON h.MaKH = kh.MaKH "
                   + "WHERE CAST(h.NgayLap AS DATE) = ? AND h.MaKH IS NOT NULL "
                   + "GROUP BY kh.MaKH, kh.HoTen, kh.SoDienThoai, kh.DiemTichLuy "
                   + "ORDER BY TongChiTieu DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ngay);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MaKH"),
                        rs.getString("HoTen"),
                        rs.getString("SoDienThoai"),
                        rs.getInt("DiemTichLuy"),
                        String.format("%,d VNĐ", rs.getLong("TongChiTieu"))
                    };
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Top khách hàng thân thiết theo tháng
    public List<Object[]> topKhachHangTheoThang(int thang, int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP 10 "
                   + "kh.MaKH, kh.HoTen, kh.SoDienThoai, kh.DiemTichLuy, "
                   + "SUM(h.ThanhTien) AS TongChiTieu "
                   + "FROM HoaDon h "
                   + "JOIN KhachHang kh ON h.MaKH = kh.MaKH "
                   + "WHERE MONTH(h.NgayLap) = ? AND YEAR(h.NgayLap) = ? AND h.MaKH IS NOT NULL "
                   + "GROUP BY kh.MaKH, kh.HoTen, kh.SoDienThoai, kh.DiemTichLuy "
                   + "ORDER BY TongChiTieu DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MaKH"),
                        rs.getString("HoTen"),
                        rs.getString("SoDienThoai"),
                        rs.getInt("DiemTichLuy"),
                        String.format("%,d VNĐ", rs.getLong("TongChiTieu"))
                    };
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Top khách hàng thân thiết theo năm
    public List<Object[]> topKhachHangTheoNam(int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP 10 "
                   + "kh.MaKH, kh.HoTen, kh.SoDienThoai, kh.DiemTichLuy, "
                   + "SUM(h.ThanhTien) AS TongChiTieu "
                   + "FROM HoaDon h "
                   + "JOIN KhachHang kh ON h.MaKH = kh.MaKH "
                   + "WHERE YEAR(h.NgayLap) = ? AND h.MaKH IS NOT NULL "
                   + "GROUP BY kh.MaKH, kh.HoTen, kh.SoDienThoai, kh.DiemTichLuy "
                   + "ORDER BY TongChiTieu DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MaKH"),
                        rs.getString("HoTen"),
                        rs.getString("SoDienThoai"),
                        rs.getInt("DiemTichLuy"),
                        String.format("%,d VNĐ", rs.getLong("TongChiTieu"))
                    };
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}