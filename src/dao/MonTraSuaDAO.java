package dao;

import dto.MonTraSuaDTO;
import dto.SizeDTO;
import dto.GiaMonTheoSizeDTO;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonTraSuaDAO {
    
    // ==================== MON TRA SUA ====================
    
    // Lấy tất cả món
    public List<MonTraSuaDTO> getAll() {
        List<MonTraSuaDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM MonTraSua ORDER BY MaMon";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return list;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                MonTraSuaDTO mon = new MonTraSuaDTO();
                mon.setMaMon(rs.getString("MaMon"));
                mon.setTenMon(rs.getString("TenMon"));
                mon.setLoaiMon(rs.getString("LoaiMon"));
                mon.setHinhAnh(rs.getString("HinhAnh"));
                mon.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(mon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, ps, rs);
        }
        return list;
    }
    
    // Thêm món
    public boolean insert(MonTraSuaDTO mon) {
        String sql = "INSERT INTO MonTraSua (MaMon, TenMon, LoaiMon, HinhAnh, TrangThai) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;
            ps = conn.prepareStatement(sql);
            ps.setString(1, mon.getMaMon());
            ps.setString(2, mon.getTenMon());
            ps.setString(3, mon.getLoaiMon());
            ps.setString(4, mon.getHinhAnh());
            ps.setBoolean(5, mon.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeAll(conn, ps, null);
        }
    }
    
    // Cập nhật món
    public boolean update(MonTraSuaDTO mon) {
        String sql = "UPDATE MonTraSua SET TenMon=?, LoaiMon=?, HinhAnh=?, TrangThai=? WHERE MaMon=?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;
            ps = conn.prepareStatement(sql);
            ps.setString(1, mon.getTenMon());
            ps.setString(2, mon.getLoaiMon());
            ps.setString(3, mon.getHinhAnh());
            ps.setBoolean(4, mon.isTrangThai());
            ps.setString(5, mon.getMaMon());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeAll(conn, ps, null);
        }
    }
    
    // Xóa món (ngừng bán)
    public boolean delete(String maMon) {
        String sql = "UPDATE MonTraSua SET TrangThai=0 WHERE MaMon=?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;
            ps = conn.prepareStatement(sql);
            ps.setString(1, maMon);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeAll(conn, ps, null);
        }
    }
    
    // ==================== SIZE ====================
    
    // Lấy tất cả size
    public List<SizeDTO> getAllSizes() {
        List<SizeDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Size ORDER BY MaSize";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return list;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                SizeDTO size = new SizeDTO();
                size.setMaSize(rs.getString("MaSize"));
                size.setTenSize(rs.getString("TenSize"));
                size.setHeSo(rs.getDouble("HeSo"));
                list.add(size);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, ps, rs);
        }
        return list;
    }
    
    // ==================== GIÁ THEO SIZE ====================
    
    // Lấy giá theo size
    public double getGiaTheoSize(String maMon, String maSize) {
        String sql = "SELECT GiaBan FROM GiaMonTheoSize WHERE MaMon = ? AND MaSize = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return 0;
            ps = conn.prepareStatement(sql);
            ps.setString(1, maMon);
            ps.setString(2, maSize);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("GiaBan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, ps, rs);
        }
        return 0;
    }
    
    // Lấy tất cả giá theo size
    public List<GiaMonTheoSizeDTO> getAllGiaTheoSize() {
        List<GiaMonTheoSizeDTO> list = new ArrayList<>();
        String sql = "SELECT g.MaMon, m.TenMon, g.MaSize, s.TenSize, g.GiaBan "
                   + "FROM GiaMonTheoSize g "
                   + "JOIN MonTraSua m ON g.MaMon = m.MaMon "
                   + "JOIN Size s ON g.MaSize = s.MaSize "
                   + "WHERE m.TrangThai = 1 "
                   + "ORDER BY m.MaMon, g.MaSize";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return list;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                GiaMonTheoSizeDTO gia = new GiaMonTheoSizeDTO();
                gia.setMaMon(rs.getString("MaMon"));
                gia.setTenMon(rs.getString("TenMon"));
                gia.setMaSize(rs.getString("MaSize"));
                gia.setTenSize(rs.getString("TenSize"));
                gia.setGiaBan(rs.getDouble("GiaBan"));
                list.add(gia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, ps, rs);
        }
        return list;
    }
    
    // Lưu giá theo size (Insert or Update)
    public boolean saveGiaTheoSize(String maMon, String maSize, double gia) {
        String sql = "MERGE INTO GiaMonTheoSize AS target "
                   + "USING (SELECT ? AS MaMon, ? AS MaSize) AS source "
                   + "ON target.MaMon = source.MaMon AND target.MaSize = source.MaSize "
                   + "WHEN MATCHED THEN UPDATE SET GiaBan = ? "
                   + "WHEN NOT MATCHED THEN INSERT (MaMon, MaSize, GiaBan) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;
            ps = conn.prepareStatement(sql);
            ps.setString(1, maMon);
            ps.setString(2, maSize);
            ps.setDouble(3, gia);
            ps.setString(4, maMon);
            ps.setString(5, maSize);
            ps.setDouble(6, gia);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeAll(conn, ps, null);
        }
    }
}