package bus;

import dao.KhachHangDAO;
import dto.KhachHangDTO;
import util.GenerateID;
import java.util.List;
import javax.swing.JOptionPane;

public class KhachHangBUS {
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    
    public List<KhachHangDTO> getAllKhachHang() {
        return khachHangDAO.getAll();
    }
    
    public KhachHangDTO getKhachHangById(String maKH) {
        return khachHangDAO.getById(maKH);
    }
    
    public boolean addKhachHang(KhachHangDTO kh) {
        if (kh.getHoTen() == null || kh.getHoTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (kh.getSoDienThoai() == null || !kh.getSoDienThoai().matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10-11 số)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        kh.setMaKH(GenerateID.generateMaKH());
        kh.setDiemTichLuy(0);
        
        return khachHangDAO.insert(kh);
    }
    
    public boolean updateKhachHang(KhachHangDTO kh) {
        if (kh.getHoTen() == null || kh.getHoTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return khachHangDAO.update(kh);
    }
    
    public boolean deleteKhachHang(String maKH) {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa khách hàng " + maKH + "?", 
                                                    "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            return khachHangDAO.delete(maKH);
        }
        return false;
    }
    
    public List<KhachHangDTO> searchKhachHang(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllKhachHang();
        }
        return khachHangDAO.search(keyword);
    }
    
    public boolean updateDiem(String maKH, int diem) {
        return khachHangDAO.updateDiem(maKH, diem);
    }
}