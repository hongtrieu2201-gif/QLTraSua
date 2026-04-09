package dao;

import dto.ChiTietToppingDTO;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietToppingDAO {
    
    // ==================== PHƯƠNG THỨC INSERT ====================
    
    /**
     * Thêm topping vào chi tiết hóa đơn
     * @param topping Đối tượng topping cần thêm
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean insert(ChiTietToppingDTO topping) {
        String sql = "INSERT INTO ChiTietToppingHoaDon (MaCTHD, MaTopping, SoLuong, DonGia, ThanhTien) "
                   + "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, topping.getMaCTHD());
            ps.setString(2, topping.getMaTopping());
            ps.setInt(3, topping.getSoLuong());
            ps.setDouble(4, topping.getDonGia());
            ps.setDouble(5, topping.getThanhTien());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Thêm nhiều topping cùng lúc
     * @param toppings Danh sách topping cần thêm
     * @return true nếu tất cả thành công, false nếu có lỗi
     */
    public boolean insertAll(List<ChiTietToppingDTO> toppings) {
        String sql = "INSERT INTO ChiTietToppingHoaDon (MaCTHD, MaTopping, SoLuong, DonGia, ThanhTien) "
                   + "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Bắt đầu transaction
            conn.setAutoCommit(false);
            
            for (ChiTietToppingDTO topping : toppings) {
                ps.setInt(1, topping.getMaCTHD());
                ps.setString(2, topping.getMaTopping());
                ps.setInt(3, topping.getSoLuong());
                ps.setDouble(4, topping.getDonGia());
                ps.setDouble(5, topping.getThanhTien());
                ps.addBatch();
            }
            
            int[] results = ps.executeBatch();
            conn.commit();
            
            // Kiểm tra kết quả
            for (int result : results) {
                if (result == Statement.EXECUTE_FAILED) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== CÁC PHƯƠNG THỨC LẤY DỮ LIỆU ====================
    
    /**
     * Lấy danh sách topping theo mã chi tiết hóa đơn
     * @param maCTHD Mã chi tiết hóa đơn
     * @return Danh sách topping
     */
    public List<ChiTietToppingDTO> getByMaCTHD(int maCTHD) {
        List<ChiTietToppingDTO> list = new ArrayList<>();
        String sql = "SELECT ct.*, t.TenTopping FROM ChiTietToppingHoaDon ct "
                   + "JOIN Topping t ON ct.MaTopping = t.MaTopping "
                   + "WHERE ct.MaCTHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, maCTHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietToppingDTO topping = new ChiTietToppingDTO();
                    topping.setMaCTTopping(rs.getInt("MaCTTopping"));
                    topping.setMaCTHD(rs.getInt("MaCTHD"));
                    topping.setMaTopping(rs.getString("MaTopping"));
                    topping.setTenTopping(rs.getString("TenTopping"));
                    topping.setSoLuong(rs.getInt("SoLuong"));
                    topping.setDonGia(rs.getDouble("DonGia"));
                    topping.setThanhTien(rs.getDouble("ThanhTien"));
                    list.add(topping);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Lấy topping theo mã chi tiết topping
     * @param maCTTopping Mã chi tiết topping
     * @return Đối tượng topping hoặc null nếu không tìm thấy
     */
    public ChiTietToppingDTO getById(int maCTTopping) {
        String sql = "SELECT ct.*, t.TenTopping FROM ChiTietToppingHoaDon ct "
                   + "JOIN Topping t ON ct.MaTopping = t.MaTopping "
                   + "WHERE ct.MaCTTopping = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, maCTTopping);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChiTietToppingDTO topping = new ChiTietToppingDTO();
                    topping.setMaCTTopping(rs.getInt("MaCTTopping"));
                    topping.setMaCTHD(rs.getInt("MaCTHD"));
                    topping.setMaTopping(rs.getString("MaTopping"));
                    topping.setTenTopping(rs.getString("TenTopping"));
                    topping.setSoLuong(rs.getInt("SoLuong"));
                    topping.setDonGia(rs.getDouble("DonGia"));
                    topping.setThanhTien(rs.getDouble("ThanhTien"));
                    return topping;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ==================== CÁC PHƯƠNG THỨC CẬP NHẬT ====================
    
    /**
     * Cập nhật số lượng topping
     * @param maCTTopping Mã chi tiết topping
     * @param soLuong Số lượng mới
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean updateSoLuong(int maCTTopping, int soLuong) {
        String sql = "UPDATE ChiTietToppingHoaDon SET SoLuong = ?, ThanhTien = DonGia * ? WHERE MaCTTopping = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, soLuong);
            ps.setInt(2, soLuong);
            ps.setInt(3, maCTTopping);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật toàn bộ thông tin topping
     * @param topping Đối tượng topping mới
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean update(ChiTietToppingDTO topping) {
        String sql = "UPDATE ChiTietToppingHoaDon SET MaTopping=?, SoLuong=?, DonGia=?, ThanhTien=? WHERE MaCTTopping=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, topping.getMaTopping());
            ps.setInt(2, topping.getSoLuong());
            ps.setDouble(3, topping.getDonGia());
            ps.setDouble(4, topping.getThanhTien());
            ps.setInt(5, topping.getMaCTTopping());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== CÁC PHƯƠNG THỨC XÓA ====================
    
    /**
     * Xóa một topping theo mã
     * @param maCTTopping Mã chi tiết topping cần xóa
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean delete(int maCTTopping) {
        String sql = "DELETE FROM ChiTietToppingHoaDon WHERE MaCTTopping = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, maCTTopping);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả topping của một chi tiết hóa đơn
     * @param maCTHD Mã chi tiết hóa đơn
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean deleteByMaCTHD(int maCTHD) {
        String sql = "DELETE FROM ChiTietToppingHoaDon WHERE MaCTHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, maCTHD);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả topping của một hóa đơn (thông qua MaHD)
     * @param maHD Mã hóa đơn
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean deleteByMaHD(String maHD) {
        String sql = "DELETE FROM ChiTietToppingHoaDon WHERE MaCTHD IN "
                   + "(SELECT MaCTHD FROM ChiTietHoaDon WHERE MaHD = ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== CÁC PHƯƠNG THỨC THỐNG KÊ ====================
    
    /**
     * Thống kê tổng tiền topping theo hóa đơn
     * @param maHD Mã hóa đơn
     * @return Tổng tiền topping
     */
    public double thongKeTongTienToppingTheoHD(String maHD) {
        String sql = "SELECT COALESCE(SUM(ctt.ThanhTien), 0) AS TongTien "
                   + "FROM ChiTietToppingHoaDon ctt "
                   + "JOIN ChiTietHoaDon ct ON ctt.MaCTHD = ct.MaCTHD "
                   + "WHERE ct.MaHD = ?";
        
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
     * Thống kê top topping bán chạy nhất
     * @param limit Số lượng kết quả muốn lấy
     * @return Danh sách topping bán chạy
     */
    public List<Object[]> thongKeTopTopping(int limit) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP " + limit + " "
                   + "t.MaTopping, t.TenTopping, "
                   + "SUM(ctt.SoLuong) AS TongSoLuong, "
                   + "SUM(ctt.ThanhTien) AS TongDoanhThu "
                   + "FROM ChiTietToppingHoaDon ctt "
                   + "JOIN Topping t ON ctt.MaTopping = t.MaTopping "
                   + "GROUP BY t.MaTopping, t.TenTopping "
                   + "ORDER BY TongSoLuong DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaTopping"),
                    rs.getString("TenTopping"),
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