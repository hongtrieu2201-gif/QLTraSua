package bus;

import dao.TaiKhoanDAO;
import dto.TaiKhoanDTO;
import javax.swing.JOptionPane;

public class TaiKhoanBUS {
    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    
    // Đăng nhập
    public TaiKhoanDTO login(String tenDangNhap, String matKhau) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        if (matKhau == null || matKhau.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        TaiKhoanDTO tk = taiKhoanDAO.login(tenDangNhap, matKhau);
        if (tk == null) {
            JOptionPane.showMessageDialog(null, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return tk;
    }
    
    // Đổi mật khẩu
    public boolean changePassword(String tenDangNhap, String oldPassword, String newPassword, String confirmPassword) {
        // Validate
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mật khẩu cũ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mật khẩu mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(null, "Mật khẩu mới phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Mật khẩu mới và xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Kiểm tra mật khẩu cũ
        TaiKhoanDTO tk = taiKhoanDAO.login(tenDangNhap, oldPassword);
        if (tk == null) {
            JOptionPane.showMessageDialog(null, "Mật khẩu cũ không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Đổi mật khẩu
        boolean result = taiKhoanDAO.updatePassword(tenDangNhap, newPassword);
        if (result) {
            JOptionPane.showMessageDialog(null, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Đổi mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }
    
    // Thêm tài khoản
    public boolean addTaiKhoan(TaiKhoanDTO tk) {
        if (tk.getTenDangNhap() == null || tk.getTenDangNhap().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (tk.getMatKhau() == null || tk.getMatKhau().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (taiKhoanDAO.isExist(tk.getTenDangNhap())) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return taiKhoanDAO.insert(tk);
    }
    
    // Cập nhật tài khoản
    public boolean updateTaiKhoan(TaiKhoanDTO tk) {
        if (tk.getTenDangNhap() == null || tk.getTenDangNhap().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên đăng nhập không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return taiKhoanDAO.update(tk);
    }
    
    // Xóa tài khoản
    public boolean deleteTaiKhoan(String tenDangNhap) {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn khóa tài khoản " + tenDangNhap + "?", 
                                                    "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            return taiKhoanDAO.delete(tenDangNhap);
        }
        return false;
    }
    
    // Lấy tài khoản theo tên đăng nhập
    public TaiKhoanDTO getTaiKhoanById(String tenDangNhap) {
        return taiKhoanDAO.getById(tenDangNhap);
    }
}