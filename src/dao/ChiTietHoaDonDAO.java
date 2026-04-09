package dao;

import dto.ChiTietHoaDonDTO;
import dto.ChiTietToppingDTO;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    
    // ==================== INSERT ====================
    
    /**
     * Thêm chi tiết hóa đơn và trả về mã tự động tăng
     */
    public int insert(ChiTietHoaDonDTO ct) {
        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaMon, Size, SoLuong, DonGia, ThanhTien) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, ct.getMaHD());
            ps.setString(2, ct.getMaMon());
            ps.setString(3, ct.getSize());
            ps.setInt(4, ct.getSoLuong());
            ps.setDouble(5, ct.getDonGia());
            ps.setDouble(6, ct.getThanhTien());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int maCTHD = rs.getInt(1);
                        ct.setMaCTHD(maCTHD);
                        return maCTHD;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    // ==================== SELECT ====================
    
    /**
     * Lấy tất cả chi tiết của một hóa đơn
     */
    public List<ChiTietHoaDonDTO> getByMaHD(String maHD) {
        List<ChiTietHoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT ct.*, m.TenMon FROM ChiTietHoaDon ct "
                   + "INNER JOIN MonTraSua m ON ct.MaMon = m.MaMon "
                   + "WHERE ct.MaHD = ? "
                   + "ORDER BY ct.MaCTHD";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();
                    ct.setMaCTHD(rs.getInt("MaCTHD"));
                    ct.setMaHD(rs.getString("MaHD"));
                    ct.setMaMon(rs.getString("MaMon"));
                    ct.setTenMon(rs.getString("TenMon"));
                    ct.setSize(rs.getString("Size"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getDouble("DonGia"));
                    ct.setThanhTien(rs.getDouble("ThanhTien"));
                    list.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Lấy chi tiết hóa đơn theo mã
     */
    public ChiTietHoaDonDTO getById(int maCTHD) {
        String sql = "SELECT ct.*, m.TenMon FROM ChiTietHoaDon ct "
                   + "INNER JOIN MonTraSua m ON ct.MaMon = m.MaMon "
                   + "WHERE ct.MaCTHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, maCTHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();
                    ct.setMaCTHD(rs.getInt("MaCTHD"));
                    ct.setMaHD(rs.getString("MaHD"));
                    ct.setMaMon(rs.getString("MaMon"));
                    ct.setTenMon(rs.getString("TenMon"));
                    ct.setSize(rs.getString("Size"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getDouble("DonGia"));
                    ct.setThanhTien(rs.getDouble("ThanhTien"));
                    return ct;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ==================== UPDATE ====================
    
    /**
     * Cập nhật số lượng chi tiết hóa đơn
     */
    public boolean updateSoLuong(int maCTHD, int soLuong, double thanhTien) {
        String sql = "UPDATE ChiTietHoaDon SET SoLuong = ?, ThanhTien = ? WHERE MaCTHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, soLuong);
            ps.setDouble(2, thanhTien);
            ps.setInt(3, maCTHD);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật toàn bộ chi tiết hóa đơn
     */
    public boolean update(ChiTietHoaDonDTO ct) {
        String sql = "UPDATE ChiTietHoaDon SET Size = ?, SoLuong = ?, DonGia = ?, ThanhTien = ? "
                   + "WHERE MaCTHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ct.getSize());
            ps.setInt(2, ct.getSoLuong());
            ps.setDouble(3, ct.getDonGia());
            ps.setDouble(4, ct.getThanhTien());
            ps.setInt(5, ct.getMaCTHD());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== DELETE ====================
    
    /**
     * Xóa một chi tiết hóa đơn
     */
    public boolean delete(int maCTHD) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaCTHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, maCTHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả chi tiết của một hóa đơn
     */
    public boolean deleteByMaHD(String maHD) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== THỐNG KÊ ====================
    
    /**
     * Tính tổng tiền của hóa đơn
     */
    public double tinhTongTienHoaDon(String maHD) {
        String sql = "SELECT COALESCE(SUM(ThanhTien), 0) AS TongTien FROM ChiTietHoaDon WHERE MaHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TongTien");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Thống kê món bán chạy nhất
     */
    public List<Object[]> thongKeMonBanChay(int limit) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP " + limit + " "
                   + "m.MaMon, m.TenMon, "
                   + "SUM(ct.SoLuong) AS TongSoLuong, "
                   + "SUM(ct.ThanhTien) AS TongDoanhThu "
                   + "FROM ChiTietHoaDon ct "
                   + "INNER JOIN MonTraSua m ON ct.MaMon = m.MaMon "
                   + "GROUP BY m.MaMon, m.TenMon "
                   + "ORDER BY TongSoLuong DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaMon"),
                    rs.getString("TenMon"),
                    rs.getInt("TongSoLuong"),
                    rs.getDouble("TongDoanhThu")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}