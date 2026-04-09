package dao;

import dto.NguyenLieuDTO;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NguyenLieuDAO {
    
    // Lấy tất cả nguyên liệu
    public List<NguyenLieuDTO> getAll() {
        List<NguyenLieuDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NguyenLieu WHERE TrangThai = 1 ORDER BY MaNL";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                NguyenLieuDTO nl = new NguyenLieuDTO();
                nl.setMaNL(rs.getString("MaNL"));
                nl.setTenNguyenLieu(rs.getString("TenNguyenLieu"));
                nl.setDonViTinh(rs.getString("DonViTinh"));
                nl.setSoLuongTon(rs.getInt("SoLuongTon"));
                nl.setMucCanhBao(rs.getInt("MucCanhBao"));
                nl.setGiaNhap(rs.getDouble("GiaNhap"));
                nl.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(nl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy nguyên liệu theo mã
    public NguyenLieuDTO getById(String maNL) {
        String sql = "SELECT * FROM NguyenLieu WHERE MaNL = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNL);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NguyenLieuDTO nl = new NguyenLieuDTO();
                    nl.setMaNL(rs.getString("MaNL"));
                    nl.setTenNguyenLieu(rs.getString("TenNguyenLieu"));
                    nl.setDonViTinh(rs.getString("DonViTinh"));
                    nl.setSoLuongTon(rs.getInt("SoLuongTon"));
                    nl.setMucCanhBao(rs.getInt("MucCanhBao"));
                    nl.setGiaNhap(rs.getDouble("GiaNhap"));
                    nl.setTrangThai(rs.getBoolean("TrangThai"));
                    return nl;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm nguyên liệu
    public boolean insert(NguyenLieuDTO nl) {
        String sql = "INSERT INTO NguyenLieu (MaNL, TenNguyenLieu, DonViTinh, SoLuongTon, MucCanhBao, GiaNhap, TrangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nl.getMaNL());
            ps.setString(2, nl.getTenNguyenLieu());
            ps.setString(3, nl.getDonViTinh());
            ps.setInt(4, nl.getSoLuongTon());
            ps.setInt(5, nl.getMucCanhBao());
            ps.setDouble(6, nl.getGiaNhap());
            ps.setBoolean(7, nl.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật nguyên liệu
    public boolean update(NguyenLieuDTO nl) {
        String sql = "UPDATE NguyenLieu SET TenNguyenLieu=?, DonViTinh=?, SoLuongTon=?, MucCanhBao=?, GiaNhap=?, TrangThai=? "
                   + "WHERE MaNL=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nl.getTenNguyenLieu());
            ps.setString(2, nl.getDonViTinh());
            ps.setInt(3, nl.getSoLuongTon());
            ps.setInt(4, nl.getMucCanhBao());
            ps.setDouble(5, nl.getGiaNhap());
            ps.setBoolean(6, nl.isTrangThai());
            ps.setString(7, nl.getMaNL());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa (cập nhật trạng thái)
    public boolean delete(String maNL) {
        String sql = "UPDATE NguyenLieu SET TrangThai=0 WHERE MaNL=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNL);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật tồn kho
    public boolean updateTonKho(String maNL, int soLuong) {
        String sql = "UPDATE NguyenLieu SET SoLuongTon = SoLuongTon + ? WHERE MaNL = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, soLuong);
            ps.setString(2, maNL);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy danh sách nguyên liệu sắp hết
    public List<NguyenLieuDTO> getWarningList() {
        List<NguyenLieuDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NguyenLieu WHERE SoLuongTon <= MucCanhBao AND TrangThai = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                NguyenLieuDTO nl = new NguyenLieuDTO();
                nl.setMaNL(rs.getString("MaNL"));
                nl.setTenNguyenLieu(rs.getString("TenNguyenLieu"));
                nl.setDonViTinh(rs.getString("DonViTinh"));
                nl.setSoLuongTon(rs.getInt("SoLuongTon"));
                nl.setMucCanhBao(rs.getInt("MucCanhBao"));
                nl.setGiaNhap(rs.getDouble("GiaNhap"));
                nl.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(nl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Tìm kiếm nguyên liệu
    public List<NguyenLieuDTO> search(String keyword) {
        List<NguyenLieuDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NguyenLieu WHERE TrangThai=1 AND (MaNL LIKE ? OR TenNguyenLieu LIKE ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NguyenLieuDTO nl = new NguyenLieuDTO();
                    nl.setMaNL(rs.getString("MaNL"));
                    nl.setTenNguyenLieu(rs.getString("TenNguyenLieu"));
                    nl.setDonViTinh(rs.getString("DonViTinh"));
                    nl.setSoLuongTon(rs.getInt("SoLuongTon"));
                    nl.setMucCanhBao(rs.getInt("MucCanhBao"));
                    nl.setGiaNhap(rs.getDouble("GiaNhap"));
                    nl.setTrangThai(rs.getBoolean("TrangThai"));
                    list.add(nl);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}