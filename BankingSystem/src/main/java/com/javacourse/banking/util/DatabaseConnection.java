//package com.javacourse.banking.util;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DatabaseConnection {
//    private static final String URL = "jdbc:postgresql://localhost:5432/banking_db";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "password"; // Điền password PostgreSQL của bạn vào đây
//
//    // Biến static để giữ kết nối duy nhất (Singleton - tùy chọn, ở đây ta mở mới cho đơn giản)
//
//    public static Connection getConnection() throws SQLException {
//        try {
//            // Load driver (Maven đã tải về rồi)
//            return DriverManager.getConnection(URL, USER, PASSWORD);
//        } catch (SQLException e) {
//            System.err.println("❌ Kết nối Database thất bại!");
//            throw e;
//        }
//    }
//
//    // Hàm main để test kết nối ngay lập tức
//    public static void main(String[] args) {
//        try (Connection conn = getConnection()) {
//            if (conn != null) {
//                System.out.println("✅ Kết nối PostgreSQL thành công!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
//


package com.javacourse.banking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:portgres://localhost:5432/banking_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException
    {
        try
        {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (SQLException e)
        {
            System.err.println("❌ Kết nối database thất bại.");
            throw e;
        }
    }
}