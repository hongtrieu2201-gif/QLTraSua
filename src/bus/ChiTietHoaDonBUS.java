package bus;

import dao.ChiTietHoaDonDAO;
import dao.ChiTietToppingDAO;
import dto.ChiTietHoaDonDTO;
import dto.ChiTietToppingDTO;
import java.util.List;
import javax.swing.JOptionPane;

public class ChiTietHoaDonBUS {
    private ChiTietHoaDonDAO chiTietDAO = new ChiTietHoaDonDAO();
    private ChiTietToppingDAO toppingDAO = new ChiTietToppingDAO();
    
    /**
     * Lưu chi tiết hóa đơn (khi thanh toán)
     */
    public boolean saveChiTietHoaDon(ChiTietHoaDonDTO ct) {
        // Validate
        if (ct.getMaHD() == null || ct.getMaHD().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã hóa đơn không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (ct.getMaMon() == null || ct.getMaMon().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã món không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (ct.getSoLuong() <= 0) {
            JOptionPane.showMessageDialog(null, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Lưu chi tiết hóa đơn
        int maCTHD = chiTietDAO.insert(ct);
        if (maCTHD == -1) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu chi tiết hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Lưu danh sách topping
        for (ChiTietToppingDTO topping : ct.getDanhSachTopping()) {
            topping.setMaCTHD(maCTHD);
            if (!toppingDAO.insert(topping)) {  // ← PHƯƠNG THỨC insert này PHẢI CÓ
                JOptionPane.showMessageDialog(null, "Lỗi khi lưu topping!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    // Lấy chi tiết hóa đơn theo mã hóa đơn
    public List<ChiTietHoaDonDTO> getByMaHD(String maHD) {
        if (maHD == null || maHD.isEmpty()) {
            return null;
        }
        List<ChiTietHoaDonDTO> list = chiTietDAO.getByMaHD(maHD);
        
        // Load topping cho từng chi tiết
        for (ChiTietHoaDonDTO ct : list) {
            List<ChiTietToppingDTO> toppings = toppingDAO.getByMaCTHD(ct.getMaCTHD());
            ct.setDanhSachTopping(toppings);
        }
        
        return list;
    }
    
    // Xóa chi tiết hóa đơn
    public boolean deleteChiTiet(int maCTHD) {
        // Xóa topping trước
        toppingDAO.deleteByMaCTHD(maCTHD);
        // Xóa chi tiết
        return chiTietDAO.delete(maCTHD);
    }
    
    // Xóa tất cả chi tiết của hóa đơn
    public boolean deleteByMaHD(String maHD) {
        List<ChiTietHoaDonDTO> list = getByMaHD(maHD);
        for (ChiTietHoaDonDTO ct : list) {
            toppingDAO.deleteByMaCTHD(ct.getMaCTHD());
        }
        return chiTietDAO.deleteByMaHD(maHD);
    }
    
    // Tính tổng tiền của hóa đơn
    public double tinhTongTien(List<ChiTietHoaDonDTO> danhSach) {
        double tong = 0;
        for (ChiTietHoaDonDTO ct : danhSach) {
            tong += ct.getThanhTien();
        }
        return tong;
    }
    
    // Cập nhật số lượng chi tiết
    public boolean updateSoLuong(int maCTHD, int soLuong, double donGia) {
        if (soLuong <= 0) {
            JOptionPane.showMessageDialog(null, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        double thanhTien = soLuong * donGia;
        return chiTietDAO.updateSoLuong(maCTHD, soLuong, thanhTien);
    }
}