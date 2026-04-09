package bus;

import dao.MonTraSuaDAO;
import dto.MonTraSuaDTO;
import dto.SizeDTO;
import dto.GiaMonTheoSizeDTO;
import util.GenerateID;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class MonTraSuaBUS {
    private MonTraSuaDAO monDAO = new MonTraSuaDAO();
    
    // Lấy tất cả món
    public List<MonTraSuaDTO> getAllMon() {
        return monDAO.getAll();
    }
    
    // Lấy món theo ID
    public MonTraSuaDTO getMonById(String maMon) {
        List<MonTraSuaDTO> list = getAllMon();
        if (list != null) {
            for (MonTraSuaDTO mon : list) {
                if (mon.getMaMon().equals(maMon)) {
                    return mon;
                }
            }
        }
        return null;
    }
    
    // Lấy giá theo size
    public double getGiaTheoSize(String maMon, String maSize) {
        double gia = monDAO.getGiaTheoSize(maMon, maSize);
        if (gia == 0) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy giá cho món " + maMon + " size " + maSize, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return gia;
    }
    
    // Lấy tất cả size
    public List<SizeDTO> getAllSizes() {
        return monDAO.getAllSizes();
    }
    
    // Lấy tất cả giá theo size
    public List<GiaMonTheoSizeDTO> getAllGiaTheoSize() {
        return monDAO.getAllGiaTheoSize();
    }
    
    // Lưu giá theo size
    public boolean saveGiaTheoSize(String maMon, String maSize, double gia) {
        if (gia <= 0) {
            JOptionPane.showMessageDialog(null, "Giá phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return monDAO.saveGiaTheoSize(maMon, maSize, gia);
    }
    
    // ==================== THÊM MÓN ====================
    public boolean addMon(MonTraSuaDTO mon) {
        // Validate
        if (mon.getTenMon() == null || mon.getTenMon().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên món không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Tạo mã tự động
        mon.setMaMon(GenerateID.generateMaMon());
        mon.setTrangThai(true);
        
        return monDAO.insert(mon);
    }
    
    // ==================== CẬP NHẬT MÓN ====================
    public boolean updateMon(MonTraSuaDTO mon) {
        // Validate
        if (mon.getMaMon() == null || mon.getMaMon().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã món không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (mon.getTenMon() == null || mon.getTenMon().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên món không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return monDAO.update(mon);
    }
    
    // ==================== XÓA MÓN (NGỪNG BÁN) ====================
    public boolean deleteMon(String maMon) {
        if (maMon == null || maMon.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã món không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return monDAO.delete(maMon);
    }
    
    // ==================== TÌM KIẾM MÓN ====================
    public List<MonTraSuaDTO> searchMon(String keyword) {
        List<MonTraSuaDTO> all = getAllMon();
        List<MonTraSuaDTO> result = new ArrayList<>();
        
        if (all != null && keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            for (MonTraSuaDTO mon : all) {
                if (mon.getMaMon().toLowerCase().contains(lowerKeyword) ||
                    mon.getTenMon().toLowerCase().contains(lowerKeyword)) {
                    result.add(mon);
                }
            }
        } else if (all != null) {
            result.addAll(all);
        }
        
        return result;
    }
    
    // ==================== LỌC THEO LOẠI ====================
    public List<MonTraSuaDTO> filterByLoai(String loaiMon) {
        List<MonTraSuaDTO> all = getAllMon();
        List<MonTraSuaDTO> result = new ArrayList<>();
        
        if (all != null) {
            if (loaiMon == null || loaiMon.equals("Tất cả")) {
                result.addAll(all);
            } else {
                for (MonTraSuaDTO mon : all) {
                    if (mon.getLoaiMon() != null && mon.getLoaiMon().equals(loaiMon)) {
                        result.add(mon);
                    }
                }
            }
        }
        
        return result;
    }
}