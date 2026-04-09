package bus;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import dto.NhanVienDTO;
import dto.TaiKhoanDTO;
import util.GenerateID;
import java.util.List;
import javax.swing.JOptionPane;

public class NhanVienBUS {
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    
    public List<NhanVienDTO> getAllNhanVien() {
        return nhanVienDAO.getAll();
    }
    
    public NhanVienDTO getNhanVienById(String maNV) {
        return nhanVienDAO.getById(maNV);
    }
    
    public boolean addNhanVien(NhanVienDTO nv, String password) {
        // Validate dữ liệu
        if (nv.getHoTen() == null || nv.getHoTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (nv.getSoDienThoai() == null || !nv.getSoDienThoai().matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10-11 số)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (nhanVienDAO.isPhoneExists(nv.getSoDienThoai(), null)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (nv.getTenDangNhap() == null || nv.getTenDangNhap().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Tạo mã tự động
        nv.setMaNV(GenerateID.generateMaNV());
        nv.setTrangThai(true);
        
        // Tạo tài khoản
        TaiKhoanDTO tk = new TaiKhoanDTO();
        tk.setTenDangNhap(nv.getTenDangNhap());
        tk.setMatKhau(password);
        tk.setVaiTro(nv.getChucVu());
        tk.setTrangThai(true);
        
        if (!taiKhoanDAO.insert(tk)) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return nhanVienDAO.insert(nv);
    }
    
    public boolean updateNhanVien(NhanVienDTO nv) {
        if (nv.getHoTen() == null || nv.getHoTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (nv.getSoDienThoai() == null || !nv.getSoDienThoai().matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10-11 số)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (nhanVienDAO.isPhoneExists(nv.getSoDienThoai(), nv.getMaNV())) {
            JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return nhanVienDAO.update(nv);
    }
    
    public boolean deleteNhanVien(String maNV) {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa nhân viên " + maNV + "?", 
                                                    "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            return nhanVienDAO.delete(maNV);
        }
        return false;
    }
    
    public List<NhanVienDTO> searchNhanVien(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllNhanVien();
        }
        return nhanVienDAO.search(keyword);
    }
}