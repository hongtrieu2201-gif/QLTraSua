package dao;

import dto.HoaDonDTO;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    
    // ==================== PHƯƠNG THỨC INSERT ====================
    
    /**
     * Thêm hóa đơn và trả về mã hóa đơn (QUAN TRỌNG - DÙNG TRONG BUS)
     * @param hoaDon Đối tượng hóa đơn cần thêm
     * @return Mã hóa đơn nếu thành công, null nếu thất bại
     */
    public String insertAndGetId(HoaDonDTO hoaDon) {
        String sql = "INSERT INTO HoaDon (MaHD, NgayLap, MaNV, MaKH, TongTien, GiamGia, DiemSuDung, ThanhTien) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hoaDon.getMaHD());
            ps.setTimestamp(2, hoaDon.getNgayLap());
            ps.setString(3, hoaDon.getMaNV());
            ps.setString(4, hoaDon.getMaKH());
            ps.setDouble(5, hoaDon.getTongTien());
            ps.setDouble(6, hoaDon.getGiamGia());
            ps.setInt(7, hoaDon.getDiemSuDung());
            ps.setDouble(8, hoaDon.getThanhTien());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                return hoaDon.getMaHD();  // Trả về mã hóa đơn
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Thêm hóa đơn (cách cũ - trả về boolean)
     */
    public boolean insertHoaDon(HoaDonDTO hoaDon) {
        String sql = "INSERT INTO HoaDon (MaHD, NgayLap, MaNV, MaKH, TongTien, GiamGia, DiemSuDung, ThanhTien) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hoaDon.getMaHD());
            ps.setTimestamp(2, hoaDon.getNgayLap());
            ps.setString(3, hoaDon.getMaNV());
            ps.setString(4, hoaDon.getMaKH());
            ps.setDouble(5, hoaDon.getTongTien());
            ps.setDouble(6, hoaDon.getGiamGia());
            ps.setInt(7, hoaDon.getDiemSuDung());
            ps.setDouble(8, hoaDon.getThanhTien());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== SELECT ====================
    
    /**
     * Lấy tất cả hóa đơn kèm tên nhân viên và khách hàng
     */
    public List<HoaDonDTO> getAllWithNames() {
        List<HoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT h.*, nv.HoTen as TenNV, kh.HoTen as TenKH "
                   + "FROM HoaDon h "
                   + "LEFT JOIN NhanVien nv ON h.MaNV = nv.MaNV "
                   + "LEFT JOIN KhachHang kh ON h.MaKH = kh.MaKH "
                   + "ORDER BY h.NgayLap DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                HoaDonDTO hd = new HoaDonDTO();
                hd.setMaHD(rs.getString("MaHD"));
                hd.setNgayLap(rs.getTimestamp("NgayLap"));
                hd.setMaNV(rs.getString("MaNV"));
                hd.setMaKH(rs.getString("MaKH"));
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setGiamGia(rs.getDouble("GiamGia"));
                hd.setDiemSuDung(rs.getInt("DiemSuDung"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                hd.setTenNV(rs.getString("TenNV"));
                hd.setTenKH(rs.getString("TenKH"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Lấy hóa đơn theo mã
     */
    public HoaDonDTO getHoaDonById(String maHD) {
        String sql = "SELECT h.*, nv.HoTen as TenNV, kh.HoTen as TenKH "
                   + "FROM HoaDon h "
                   + "LEFT JOIN NhanVien nv ON h.MaNV = nv.MaNV "
                   + "LEFT JOIN KhachHang kh ON h.MaKH = kh.MaKH "
                   + "WHERE h.MaHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HoaDonDTO hd = new HoaDonDTO();
                    hd.setMaHD(rs.getString("MaHD"));
                    hd.setNgayLap(rs.getTimestamp("NgayLap"));
                    hd.setMaNV(rs.getString("MaNV"));
                    hd.setMaKH(rs.getString("MaKH"));
                    hd.setTongTien(rs.getDouble("TongTien"));
                    hd.setGiamGia(rs.getDouble("GiamGia"));
                    hd.setDiemSuDung(rs.getInt("DiemSuDung"));
                    hd.setThanhTien(rs.getDouble("ThanhTien"));
                    hd.setTenNV(rs.getString("TenNV"));
                    hd.setTenKH(rs.getString("TenKH"));
                    return hd;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ==================== UPDATE ====================
    
    /**
     * Cập nhật hóa đơn
     */
    public boolean updateHoaDon(HoaDonDTO hd) {
        String sql = "UPDATE HoaDon SET NgayLap=?, MaNV=?, MaKH=?, TongTien=?, GiamGia=?, DiemSuDung=?, ThanhTien=? "
                   + "WHERE MaHD=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, hd.getNgayLap());
            ps.setString(2, hd.getMaNV());
            ps.setString(3, hd.getMaKH());
            ps.setDouble(4, hd.getTongTien());
            ps.setDouble(5, hd.getGiamGia());
            ps.setInt(6, hd.getDiemSuDung());
            ps.setDouble(7, hd.getThanhTien());
            ps.setString(8, hd.getMaHD());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== DELETE ====================
    
    /**
     * Xóa hóa đơn
     */
    public boolean delete(String maHD) {
        String sql = "DELETE FROM HoaDon WHERE MaHD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== SEARCH ====================
    
    /**
     * Tìm kiếm hóa đơn
     */
    public List<HoaDonDTO> search(String keyword) {
        List<HoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT h.*, nv.HoTen as TenNV, kh.HoTen as TenKH "
                   + "FROM HoaDon h "
                   + "LEFT JOIN NhanVien nv ON h.MaNV = nv.MaNV "
                   + "LEFT JOIN KhachHang kh ON h.MaKH = kh.MaKH "
                   + "WHERE h.MaHD LIKE ? OR nv.HoTen LIKE ? OR kh.HoTen LIKE ? "
                   + "ORDER BY h.NgayLap DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDonDTO hd = new HoaDonDTO();
                    hd.setMaHD(rs.getString("MaHD"));
                    hd.setNgayLap(rs.getTimestamp("NgayLap"));
                    hd.setMaNV(rs.getString("MaNV"));
                    hd.setMaKH(rs.getString("MaKH"));
                    hd.setTongTien(rs.getDouble("TongTien"));
                    hd.setGiamGia(rs.getDouble("GiamGia"));
                    hd.setDiemSuDung(rs.getInt("DiemSuDung"));
                    hd.setThanhTien(rs.getDouble("ThanhTien"));
                    hd.setTenNV(rs.getString("TenNV"));
                    hd.setTenKH(rs.getString("TenKH"));
                    list.add(hd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // ==================== THỐNG KÊ ====================
    
    /**
     * Thống kê doanh thu theo ngày
     */
    public double thongKeDoanhThuTheoNgay(String ngay) {
        String sql = "SELECT COALESCE(SUM(ThanhTien), 0) AS DoanhThu FROM HoaDon WHERE CAST(NgayLap AS DATE) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ngay);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("DoanhThu");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Thống kê số hóa đơn theo ngày
     */
    public int thongKeSoHoaDonTheoNgay(String ngay) {
        String sql = "SELECT COUNT(*) AS SoHoaDon FROM HoaDon WHERE CAST(NgayLap AS DATE) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ngay);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SoHoaDon");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}