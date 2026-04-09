package dao;

import dto.NhanVienDTO;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    
    // Lấy tất cả nhân viên (chưa xóa)
    public List<NhanVienDTO> getAll() {
        List<NhanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE TrangThai = 1 ORDER BY MaNV";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setChucVu(rs.getString("ChucVu"));
                nv.setTenDangNhap(rs.getString("TenDangNhap"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy nhân viên theo mã
    public NhanVienDTO getById(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhanVienDTO nv = new NhanVienDTO();
                    nv.setMaNV(rs.getString("MaNV"));
                    nv.setHoTen(rs.getString("HoTen"));
                    nv.setSoDienThoai(rs.getString("SoDienThoai"));
                    nv.setDiaChi(rs.getString("DiaChi"));
                    nv.setChucVu(rs.getString("ChucVu"));
                    nv.setTenDangNhap(rs.getString("TenDangNhap"));
                    nv.setTrangThai(rs.getBoolean("TrangThai"));
                    return nv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm nhân viên
    public boolean insert(NhanVienDTO nv) {
        String sql = "INSERT INTO NhanVien (MaNV, HoTen, SoDienThoai, DiaChi, ChucVu, TenDangNhap, TrangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getSoDienThoai());
            ps.setString(4, nv.getDiaChi());
            ps.setString(5, nv.getChucVu());
            ps.setString(6, nv.getTenDangNhap());
            ps.setBoolean(7, nv.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật nhân viên
    public boolean update(NhanVienDTO nv) {
        String sql = "UPDATE NhanVien SET HoTen=?, SoDienThoai=?, DiaChi=?, ChucVu=?, TrangThai=? WHERE MaNV=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getSoDienThoai());
            ps.setString(3, nv.getDiaChi());
            ps.setString(4, nv.getChucVu());
            ps.setBoolean(5, nv.isTrangThai());
            ps.setString(6, nv.getMaNV());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa mềm
    public boolean delete(String maNV) {
        String sql = "UPDATE NhanVien SET TrangThai=0 WHERE MaNV=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Tìm kiếm
    public List<NhanVienDTO> search(String keyword) {
        List<NhanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE TrangThai=1 AND (MaNV LIKE ? OR HoTen LIKE ? OR SoDienThoai LIKE ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NhanVienDTO nv = new NhanVienDTO();
                    nv.setMaNV(rs.getString("MaNV"));
                    nv.setHoTen(rs.getString("HoTen"));
                    nv.setSoDienThoai(rs.getString("SoDienThoai"));
                    nv.setDiaChi(rs.getString("DiaChi"));
                    nv.setChucVu(rs.getString("ChucVu"));
                    nv.setTenDangNhap(rs.getString("TenDangNhap"));
                    nv.setTrangThai(rs.getBoolean("TrangThai"));
                    list.add(nv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Kiểm tra số điện thoại đã tồn tại chưa
    public boolean isPhoneExists(String soDienThoai, String maNV) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE SoDienThoai = ? AND MaNV != ? AND TrangThai = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, soDienThoai);
            ps.setString(2, maNV == null ? "" : maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}