package util;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.lang.reflect.Method;

public class TableModelUtil {
    
    // Tạo table model từ list object
    public static <T> DefaultTableModel createTableModel(List<T> list, String[] columnNames, String[] fieldNames) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho edit trực tiếp trên table
            }
        };
        
        if (list != null) {
            for (T obj : list) {
                Object[] row = new Object[fieldNames.length];
                for (int i = 0; i < fieldNames.length; i++) {
                    try {
                        String methodName = "get" + capitalize(fieldNames[i]);
                        Method method = obj.getClass().getMethod(methodName);
                        row[i] = method.invoke(obj);
                    } catch (Exception e) {
                        row[i] = "";
                    }
                }
                model.addRow(row);
            }
        }
        return model;
    }
    
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}