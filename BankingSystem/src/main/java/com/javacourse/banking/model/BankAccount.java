//package com.javacourse.banking.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BankAccount {
//    private final String accountNumber;
//    private final String ownerName;
//    private final String email;
//    private double balance; // Số dư thay đổi nên không final
//    private final List<String> transactionHistory; // List lịch sử
//
//    public BankAccount(String accountNumber, String ownerName, String email, double balance) {
//        this.accountNumber = accountNumber;
//        this.ownerName = ownerName;
//        this.email = email;
//        this.balance = balance;
//        this.transactionHistory = new ArrayList<>();
//    }
//
//    // --- Các hàm Getters ---
//    public String getAccountNumber() { return accountNumber; }
//    public String getOwnerName() { return ownerName; }
//    public String getEmail() { return email; }
//
//    // Đọc số dư cũng nên synchronized nếu có nhiều luồng đọc/ghi cùng lúc
//    public synchronized double getBalance() {
//        return balance;
//    }
//
//    public List<String> getTransactionHistory() {
//        return new ArrayList<>(transactionHistory); // Trả về bản sao để an toàn
//    }
//
//    // --- Core Banking Logic (Thread-Safe) ---
//
//    // Nạp tiền: Cần synchronized để tránh 2 người nạp cùng lúc gây sai số dư
//    public synchronized void deposit(double amount) {
//        if (amount <= 0) {
//            throw new IllegalArgumentException("Số tiền nạp phải lớn hơn 0");
//        }
//        this.balance += amount;
//        transactionHistory.add("Nạp tiền: +" + amount + " | Số dư mới: " + balance);
//    }
//
//    // Rút tiền: Cần synchronized để tránh Race Condition (Rút quá số dư)
//    public void withdraw(double amount) {
//        if (amount <= 0) {
//            throw new IllegalArgumentException("Số tiền rút phải lớn hơn 0");
//        }
//        if (amount > this.balance) {
//            throw new IllegalStateException("Số dư không đủ! (Hiện tại: " + balance + ")");
//        }
//
//        // Mô phỏng độ trễ mạng để test Race Condition sau này
//        try {
//            Thread.sleep(100); // Ngủ 100ms
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        this.balance -= amount;
//        transactionHistory.add("Rút tiền: -" + amount + " | Số dư mới: " + balance);
//    }
//
//    @Override
//    public String toString() {
//        return String.format("[%s] %s - Số dư: %.2f", accountNumber, ownerName, balance);
//    }
//}

package com.javacourse.banking.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class BankAccount
{
    private final String accountNumber;
    private final String ownerName;
    private final String email;
    private double balance;
    private final List<String> transactionHistory;

    public BankAccount(
            String accountName,
            String ownerName,
            String email,
            double balance
    )
    {
        this.accountNumber = accountName;
        this.ownerName = ownerName;
        this.email = email;
        this.balance = balance;
        this.transactionHistory = new ArrayList<String>(); // Cái này có nên để là String không ta???
    }

    // Các hàm getter
    public String getAccountNumber() {
        return accountNumber;
    }
    public double getBalance()
    {
        return balance;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public String getEmail()
    {
        return email;
    }

    // Override hàm toString() để in ra cho đẹp
    @Override
    public String toString() {
        return String.format("[%s] %s - Số dư: %.0f", accountNumber, ownerName, balance);
    }

    public synchronized void deposit(double amount)
    {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền nạp phải lớn hơn 0");
        }
        this.balance += amount;
        System.out.println("❌ [DAO] Lỗi khi lưu tài khoản mới: Không có dòng nào được thêm.");
    }

    public synchronized void withdraw(double amount)
    {
        if (amount > this.balance)
            throw new IllegalArgumentException("Số dư không đủ! (Hiện tại: " + balance + ")");
        if (amount < 0)
            throw new IllegalArgumentException("Số tiền rút phải lớn hơn 0");

        this.balance -= amount;
        System.out.println("Rút tiền: -" + amount + " | Số dư mới: " + balance);
    }
}