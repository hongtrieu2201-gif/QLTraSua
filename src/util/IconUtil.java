package util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class IconUtil {

    public static ImageIcon getIcon(String fileName, int width, int height) {
        try {
            String path = "/icons/" + fileName;
            URL url = IconUtil.class.getResource(path);

            System.out.println("Đang load: " + path);
            System.out.println("URL = " + url);

            if (url != null) {
                ImageIcon original = new ImageIcon(url);
                Image scaledImage = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                System.err.println("Không tìm thấy ảnh: " + path);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi đọc ảnh: " + fileName);
            e.printStackTrace();
        }

        Icon defaultIcon = UIManager.getIcon("OptionPane.informationIcon");
        return defaultIcon instanceof ImageIcon ? (ImageIcon) defaultIcon : new ImageIcon();
    }

    public static ImageIcon getAddIcon() { return getIcon("add.png", 16, 16); }
    public static ImageIcon getBillIcon() { return getIcon("bill.png", 20, 20); }
    public static ImageIcon getCancelIcon() { return getIcon("cancel.png", 16, 16); }
    public static ImageIcon getCustomerIcon() { return getIcon("customer.png", 20, 20); }
    public static ImageIcon getDeleteIcon() { return getIcon("delete.png", 16, 16); }
    public static ImageIcon getEditIcon() { return getIcon("edit.png", 16, 16); }
    public static ImageIcon getHelpIcon() { return getIcon("help.png", 20, 20); }
    public static ImageIcon getLoginIcon() { return getIcon("login.png", 20, 20); }
    public static ImageIcon getLogoutIcon() { return getIcon("logout.png", 20, 20); }
    public static ImageIcon getProductIcon() { return getIcon("product.png", 20, 20); }
    public static ImageIcon getRefreshIcon() { return getIcon("refresh.png", 16, 16); }
    public static ImageIcon getSaveIcon() { return getIcon("save.png", 16, 16); }
    public static ImageIcon getSearchIcon() { return getIcon("search.png", 16, 16); }
    public static ImageIcon getStatisticsIcon() { return getIcon("statistics.png", 20, 20); }
    public static ImageIcon getToppingIcon() { return getIcon("topping.png", 20, 20); }
    public static ImageIcon getUserIcon() { return getIcon("user.png", 20, 20); }
}