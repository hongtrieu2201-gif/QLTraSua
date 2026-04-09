package util;

import java.sql.*;

public class DBConnection {
    
    // CẬP NHẬT THÔNG TIN KẾT NỐI CỦA BẠN
    private static final String SERVER_NAME = "localhost"; // hoặc "127.0.0.1" hoặc tên máy tính
    private static final int PORT = 1433;
    private static final String DATABASE_NAME = "QuanLyTraSua";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123456"; // Mật khẩu SQL Server của bạn
    
    private static final String URL = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT 
            + ";databaseName=" + DATABASE_NAME 
            + ";encrypt=true;trustServerCertificate=true";
    
    // Không cache connection - mỗi lần gọi đều tạo mới
    // Điều này tránh lỗi connection closed
    
    /**
     * Lấy kết nối mới đến database
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✓ Kết nối database thành công!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Không tìm thấy driver SQL Server!");
            System.err.println("  Hãy thêm file sqljdbc4.jar vào project");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kết nối database: " + e.getMessage());
            System.err.println("  Kiểm tra lại:");
            System.err.println("  - SQL Server đã bật chưa?");
            System.err.println("  - Tên server: " + SERVER_NAME);
            System.err.println("  - Cổng: " + PORT);
            System.err.println("  - Tài khoản: " + USERNAME);
            System.err.println("  - Mật khẩu: " + PASSWORD);
            System.err.println("  - Database: " + DATABASE_NAME);
            return null;
        }
    }
    
    /**
     * Đóng kết nối
     * @param conn Connection cần đóng
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✓ Đã đóng kết nối database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Đóng tất cả resource
     */
    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Test kết nối
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Kết nối thành công!");
            closeConnection(conn);
        } else {
            System.out.println("Kết nối thất bại!");
        }
    }
}