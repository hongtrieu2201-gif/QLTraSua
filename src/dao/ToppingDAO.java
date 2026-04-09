package dao;

import dto.ToppingDTO;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToppingDAO {
    
    public List<ToppingDTO> getAll() {
        List<ToppingDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Topping WHERE TrangThai = 1 ORDER BY MaTopping";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ToppingDTO top = new ToppingDTO();
                top.setMaTopping(rs.getString("MaTopping"));
                top.setTenTopping(rs.getString("TenTopping"));
                top.setGiaTopping(rs.getDouble("GiaTopping"));
                top.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(top);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<ToppingDTO> getAllIncludingDisabled() {
        List<ToppingDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Topping ORDER BY MaTopping";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ToppingDTO top = new ToppingDTO();
                top.setMaTopping(rs.getString("MaTopping"));
                top.setTenTopping(rs.getString("TenTopping"));
                top.setGiaTopping(rs.getDouble("GiaTopping"));
                top.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(top);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public ToppingDTO getById(String maTopping) {
        String sql = "SELECT * FROM Topping WHERE MaTopping = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maTopping);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ToppingDTO top = new ToppingDTO();
                    top.setMaTopping(rs.getString("MaTopping"));
                    top.setTenTopping(rs.getString("TenTopping"));
                    top.setGiaTopping(rs.getDouble("GiaTopping"));
                    top.setTrangThai(rs.getBoolean("TrangThai"));
                    return top;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean insert(ToppingDTO top) {
        String sql = "INSERT INTO Topping (MaTopping, TenTopping, GiaTopping, TrangThai) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, top.getMaTopping());
            ps.setString(2, top.getTenTopping());
            ps.setDouble(3, top.getGiaTopping());
            ps.setBoolean(4, top.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean update(ToppingDTO top) {
        String sql = "UPDATE Topping SET TenTopping=?, GiaTopping=?, TrangThai=? WHERE MaTopping=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, top.getTenTopping());
            ps.setDouble(2, top.getGiaTopping());
            ps.setBoolean(3, top.isTrangThai());
            ps.setString(4, top.getMaTopping());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(String maTopping) {
        String sql = "UPDATE Topping SET TrangThai=0 WHERE MaTopping=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maTopping);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<ToppingDTO> search(String keyword) {
        List<ToppingDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Topping WHERE TrangThai=1 AND (MaTopping LIKE ? OR TenTopping LIKE ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ToppingDTO top = new ToppingDTO();
                    top.setMaTopping(rs.getString("MaTopping"));
                    top.setTenTopping(rs.getString("TenTopping"));
                    top.setGiaTopping(rs.getDouble("GiaTopping"));
                    top.setTrangThai(rs.getBoolean("TrangThai"));
                    list.add(top);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}