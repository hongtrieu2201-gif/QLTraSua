package bus;

import dao.ThongKeDAO;
import java.util.List;
import java.util.Map;

public class ThongKeBUS {
    private ThongKeDAO thongKeDAO = new ThongKeDAO();
    
    // Thống kê doanh thu
    public Map<String, Object> thongKeDoanhThu(String loai, int value1, int value2) {
        if (loai.equals("ngay")) {
            return thongKeDAO.thongKeTheoNgay(String.valueOf(value1));
        } else if (loai.equals("thang")) {
            return thongKeDAO.thongKeTheoThang(value1, value2);
        } else {
            return thongKeDAO.thongKeTheoNam(value1);
        }
    }
    
    // Chi tiết doanh thu
    public List<Object[]> chiTietDoanhThu(String loai, int value1, int value2) {
        if (loai.equals("ngay")) {
            return thongKeDAO.chiTietDoanhThuTheoNgay(String.valueOf(value1));
        } else if (loai.equals("thang")) {
            return thongKeDAO.chiTietDoanhThuTheoThang(value1, value2);
        } else {
            return thongKeDAO.chiTietDoanhThuTheoNam(value1);
        }
    }
    
    // Top món bán chạy
    public List<Object[]> topMonBanChay(String loai, int value1, int value2) {
        if (loai.equals("ngay")) {
            return thongKeDAO.topMonBanChayTheoNgay(String.valueOf(value1));
        } else if (loai.equals("thang")) {
            return thongKeDAO.topMonBanChayTheoThang(value1, value2);
        } else {
            return thongKeDAO.topMonBanChayTheoNam(value1);
        }
    }
    
    // Top khách hàng thân thiết
    public List<Object[]> topKhachHangThanThiet(String loai, int value1, int value2) {
        if (loai.equals("ngay")) {
            return thongKeDAO.topKhachHangTheoNgay(String.valueOf(value1));
        } else if (loai.equals("thang")) {
            return thongKeDAO.topKhachHangTheoThang(value1, value2);
        } else {
            return thongKeDAO.topKhachHangTheoNam(value1);
        }
    }
}