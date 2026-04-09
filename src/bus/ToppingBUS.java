package bus;

import dao.ToppingDAO;
import dto.ToppingDTO;
import util.GenerateID;
import java.util.List;
import javax.swing.JOptionPane;

public class ToppingBUS {
    private ToppingDAO toppingDAO = new ToppingDAO();
    
    public List<ToppingDTO> getAllTopping() {
        return toppingDAO.getAll();
    }
    
    public List<ToppingDTO> getAllToppingIncludingDisabled() {
        return toppingDAO.getAllIncludingDisabled();
    }
    
    public ToppingDTO getToppingById(String maTopping) {
        return toppingDAO.getById(maTopping);
    }
    
    public boolean addTopping(ToppingDTO top) {
        if (top.getTenTopping() == null || top.getTenTopping().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên topping không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (top.getGiaTopping() <= 0) {
            JOptionPane.showMessageDialog(null, "Giá topping phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Kiểm tra trùng tên topping (không phân biệt hoa thường)
        List<ToppingDTO> allToppings = getAllToppingIncludingDisabled();
        for (ToppingDTO existing : allToppings) {
            if (existing.getTenTopping().trim().equalsIgnoreCase(top.getTenTopping().trim())) {
                JOptionPane.showMessageDialog(null, "Tên topping đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        top.setMaTopping(GenerateID.generateMaTopping());
        top.setTrangThai(true);
        
        return toppingDAO.insert(top);
    }
    
    public boolean updateTopping(ToppingDTO top) {
        if (top.getTenTopping() == null || top.getTenTopping().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên topping không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (top.getGiaTopping() <= 0) {
            JOptionPane.showMessageDialog(null, "Giá topping phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Kiểm tra trùng tên topping (không phân biệt hoa thường), loại trừ chính nó
        List<ToppingDTO> allToppings = getAllToppingIncludingDisabled();
        for (ToppingDTO existing : allToppings) {
            if (!existing.getMaTopping().equals(top.getMaTopping()) && 
                existing.getTenTopping().trim().equalsIgnoreCase(top.getTenTopping().trim())) {
                JOptionPane.showMessageDialog(null, "Tên topping đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return toppingDAO.update(top);
    }
    
    public boolean deleteTopping(String maTopping) {
        ToppingDTO top = getToppingById(maTopping);
        if (top == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy topping!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String tenTopping = top.getTenTopping() != null ? top.getTenTopping() : maTopping;
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn ngừng bán topping '" + tenTopping + "'?", 
                                                    "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            return toppingDAO.delete(maTopping);
        }
        return false;
    }
    
    public List<ToppingDTO> searchTopping(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTopping();
        }
        return toppingDAO.search(keyword);
    }
}