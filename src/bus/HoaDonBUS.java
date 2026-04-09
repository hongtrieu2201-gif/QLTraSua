package bus;

import dao.HoaDonDAO;
import dao.ChiTietHoaDonDAO;
import dao.ChiTietToppingDAO;
import dto.HoaDonDTO;
import dto.ChiTietHoaDonDTO;
import util.GenerateID;
import java.sql.Timestamp;
import java.util.List;
import javax.swing.JOptionPane;

public class HoaDonBUS {
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private ChiTietHoaDonDAO chiTietDAO = new ChiTietHoaDonDAO();
    private ChiTietToppingDAO toppingDAO = new ChiTietToppingDAO();
    
    // Tạo mã hóa đơn mới
    public String taoMaHoaDonMoi() {
        return GenerateID.generateMaHD();
    }
    
    // Lưu hóa đơn hoàn chỉnh (khi thanh toán)
    public boolean saveHoaDon(HoaDonDTO hoaDon, List<ChiTietHoaDonDTO> danhSachChiTiet) {
        // Validate hóa đơn
        if (hoaDon.getMaHD() == null || hoaDon.getMaHD().isEmpty()) {
            hoaDon.setMaHD(GenerateID.generateMaHD());
        }
        
        if (hoaDon.getNgayLap() == null) {
            hoaDon.setNgayLap(new Timestamp(System.currentTimeMillis()));
        }
        
        if (hoaDon.getMaNV() == null || hoaDon.getMaNV().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Chưa chọn nhân viên lập hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate chi tiết
        if (danhSachChiTiet == null || danhSachChiTiet.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Hóa đơn không có sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Tính lại tổng tiền
        double tongTien = 0;
        for (ChiTietHoaDonDTO ct : danhSachChiTiet) {
            ct.setMaHD(hoaDon.getMaHD());
            ct.tinhThanhTien();
            tongTien += ct.getThanhTien();
        }
        hoaDon.setTongTien(tongTien);
        
        // Tính thành tiền sau giảm giá
        double tienGiamTuDiem = hoaDon.getDiemSuDung() * 1000;
        double thanhTien = tongTien - hoaDon.getGiamGia() - tienGiamTuDiem;
        if (thanhTien < 0) thanhTien = 0;
        hoaDon.setThanhTien(thanhTien);
        
        // Lưu hóa đơn - SỬ DỤNG PHƯƠNG THỨC insertAndGetId
        String maHD = hoaDonDAO.insertAndGetId(hoaDon);
        if (maHD == null) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Lưu chi tiết hóa đơn và topping
        for (ChiTietHoaDonDTO ct : danhSachChiTiet) {
            ct.setMaHD(maHD);
            
            // Lưu chi tiết hóa đơn và lấy mã
            int maCTHD = chiTietDAO.insert(ct);
            if (maCTHD == -1) {
                return false;
            }
            
            // Lưu topping cho từng chi tiết
            for (dto.ChiTietToppingDTO topping : ct.getDanhSachTopping()) {
                topping.setMaCTHD(maCTHD);
                if (!toppingDAO.insert(topping)) {
                    return false;
                }
            }
        }
        
        JOptionPane.showMessageDialog(null, "Lưu hóa đơn thành công!\nMã HD: " + maHD, 
                                      "Thành công", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    // Lấy tất cả hóa đơn
    public List<HoaDonDTO> getAllHoaDon() {
        return hoaDonDAO.getAllWithNames();
    }
    
    // Lấy hóa đơn theo mã
    public HoaDonDTO getHoaDonById(String maHD) {
        return hoaDonDAO.getHoaDonById(maHD);
    }
    
    // Tìm kiếm hóa đơn
    public List<HoaDonDTO> searchHoaDon(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllHoaDon();
        }
        return hoaDonDAO.search(keyword);
    }
    
    // Xóa hóa đơn
    public boolean deleteHoaDon(String maHD) {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa hóa đơn " + maHD + "?", 
                                                    "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Xóa chi tiết trước (cascade sẽ tự xóa topping)
            chiTietDAO.deleteByMaHD(maHD);
            // Xóa hóa đơn
            return hoaDonDAO.delete(maHD);
        }
        return false;
    }
    
    // Thống kê doanh thu theo ngày
    public double thongKeDoanhThuTheoNgay(String ngay) {
        return hoaDonDAO.thongKeDoanhThuTheoNgay(ngay);
    }
    
    // Thống kê số hóa đơn theo ngày
    public int thongKeSoHoaDonTheoNgay(String ngay) {
        return hoaDonDAO.thongKeSoHoaDonTheoNgay(ngay);
    }
}