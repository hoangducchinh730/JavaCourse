//package com.javacourse.banking.dao;
//
//import com.javacourse.banking.model.BankAccount;
//import com.javacourse.banking.util.DatabaseConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class AccountDAO {
//    public Map<String, BankAccount> loadAllAccounts() {
//        Map<String, BankAccount> mapAccounts = new HashMap<>();
//        String sql = "SELECT * FROM accounts";
//
//        // Sử dụng try-with-resources để tự động đóng kết nối (Connection, Statement)
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery())
//        {
//            while(rs.next())
//            {
//                String accNum = rs.getString("account_number");
//                String owner = rs.getString("owner_name");
//                String email = rs.getString("email");
//                double balance = rs.getDouble("balance");
//
//                BankAccount account = new BankAccount(accNum, owner, email, balance);
//                mapAccounts.put(accNum, account);
//            }
//            System.out.println("✅ [AccountDAO] Đã load thành công " + mapAccounts.size() + " tài khoản từ Database.");
//        }
//        catch (SQLException e)
//        {
//            System.err.println("❌ Lỗi khi load dữ liệu: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return mapAccounts;
//    }
//
//    public void updateBalance(BankAccount account) {
//        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)
//        )
//        {
//            stmt.setDouble(1, account.getBalance());
//            stmt.setString(2, account.getAccountNumber());
//
//            int rowsAffected = stmt.executeUpdate(); // Thực thi lệnh Update
//
//            if (rowsAffected > 0) {
//                // [MỚI] Thêm log để xác nhận đã có dòng bị ảnh hưởng
//                System.out.println("💾 [DAO] Cập nhật thành công số dư mới: " + account.getBalance());
//            } else {
//                System.err.println("❌ [DAO] Cập nhật thất bại: Không tìm thấy tài khoản " + account.getAccountNumber());
//            }
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void saveNewAccount(BankAccount account) {
//        String sql = "INSERT INTO accounts (account_number, owner_name, email, balance) VALUES (?, ?, ?, ?)";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            // 1. Gán các giá trị
//            stmt.setString(1, account.getAccountNumber());
//            stmt.setString(2, account.getOwnerName());
//            stmt.setString(3, account.getEmail());
//            stmt.setDouble(4, account.getBalance());
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected > 0) {
//                System.out.println("💾 [DAO] Tài khoản mới đã được lưu thành công: " + account.getAccountNumber());
//            } else {
//                System.err.println("❌ [DAO] Lỗi khi lưu tài khoản mới: Không có dòng nào được thêm.");
//            }
//
//        } catch (SQLException e) {
//            System.err.println("❌ [DAO] Lỗi DB khi tạo tài khoản: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}


package com.javacourse.banking.dao;

import com.javacourse.banking.model.BankAccount;
import com.javacourse.banking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountDAO
{
    public Map<String, BankAccount> loadAllAccounts()
    {
        Map<String, BankAccount> mapAccounts = new HashMap<>();
        String sql = "SELECT * FROM accounts";
        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        )
        {
            while(rs.next())
            {
                String accountNumber = rs.getString("account_number");
                String ownerName = rs.getString("owner_name");
                String email = rs.getString("email");
                double balance = rs.getDouble("balance");

                BankAccount account = new BankAccount(accountNumber, ownerName, email, balance);
                mapAccounts.put(accountNumber, account);
            }
            System.out.println("✅ [AccountDAO] Đã load thành công " + mapAccounts.size() + " tài khoản từ Database.");
        }
        catch (SQLException e)
        {
            System.err.println("❌ Lỗi khi load dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
        return mapAccounts;
    }

    public void updateBalance(BankAccount account)
    {
        String sql = "UPDATE accounts SET balance = ? WHERE accountNumber = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
            stmt.setDouble(1, account.getBalance());
            stmt.setString(2, account.getAccountNumber());

            int rowAffected = stmt.executeUpdate();
            if (rowAffected > 0)
                System.out.println("💾 [DAO] Cập nhật thành công số dư mới: " + account.getBalance());
            else
                System.err.println("❌ [DAO] Cập nhật thất bại: Không tìm thấy tài khoản \" + account.getAccountNumber()");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void saveNewAccount(BankAccount account)
    {
        String sql = "INSERT INTO accounts (account_number, owner_name, email, balance) VALUES (?,?,?,?)";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getOwnerName());
            stmt.setString(3, account.getEmail());
            stmt.setDouble(4, account.getBalance());

            int rowAffected = stmt.executeUpdate();
            if (rowAffected > 0)
                System.out.println("💾 [DAO] Tài khoản mới đã được lưu thành công: + account.getAccountNumber()");
            else
                System.err.println("❌ [DAO] Lỗi khi lưu tài khoản mới: Không có dòng nào được thêm.");
        }
        catch (SQLException e)
        {
            System.err.println("❌ [DAO] Lỗi khi lưu tài khoản mới: Không có dòng nào được thêm.");
            e.printStackTrace();
        }
    }
}