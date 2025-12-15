//package com.javacourse.banking.service;
//
//import com.javacourse.banking.dao.AccountDAO;
//import com.javacourse.banking.model.BankAccount;
//import com.javacourse.banking.task.InterestCalculationTask;
//
//import java.util.Collections;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class BankService {
//    private final Map<String, BankAccount> accounts;
//    private final AccountDAO accountDAO;
//    private final Set<String> registeredEmails;
//    private final AtomicLong transactionIdCounter = new AtomicLong(0);
//
//    public BankService() {
//        this.accountDAO = new AccountDAO();
//        Map<String, BankAccount> loadedAccounts = accountDAO.loadAllAccounts();
//        this.accounts = new ConcurrentHashMap<>(loadedAccounts);
//
//        this.registeredEmails = Collections.newSetFromMap(new ConcurrentHashMap<>());
//        for (BankAccount acc : loadedAccounts.values()) {
//            this.registeredEmails.add(acc.getEmail());
//        }
//
//        System.out.println("✅ [BankService] Đã khởi tạo dịch vụ, sẵn sàng giao dịch.");
//
//        InterestCalculationTask interestTask = new InterestCalculationTask(this, 10000);
//        interestTask.start();
//    }
//
//    public BankAccount createAccount(String ownerName, String email, double initialDeposit) {
//        if (registeredEmails.contains(email)) { // Check O(1) nhờ Set
//            throw new IllegalArgumentException("❌ Email đã được đăng ký!");
//        }
//
//        // Sinh số tài khoản ngẫu nhiên
//        String newAccNum = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
//
//        // Tạo đối tượng BankAccount
//        BankAccount newAccount = new BankAccount(newAccNum, ownerName, email, initialDeposit);
//
//        // Thêm vào Map (để tra cứu nhanh) và Set (để check trùng)
//        accounts.put(newAccNum, newAccount);
//        registeredEmails.add(email);
//
//        // [MỚI] Hàm DAO để lưu tài khoản mới này xuống DB
//        accountDAO.saveNewAccount(newAccount);
//
//        return newAccount;
//    }
//
//    // [MỚI] 2. Logic tính lãi suất (Dùng Map.values())
//    public void applyInterest() {
//        double interestRate = 0.0001; // 0.01%
//
//        // Lặp qua tất cả tài khoản trong Map
//        // Lệnh này tự động khóa từng BankAccount nhờ synchronized trong deposit/withdraw
//        for (BankAccount account : accounts.values()) {
//            double currentBalance = account.getBalance();
//            double interest = currentBalance * interestRate;
//
//            // Dùng hàm deposit để cộng lãi suất
//            if (interest > 0) {
//                account.deposit(interest);
//                // System.out.printf("🏦 Lãi suất: Cộng %.2f vào tài khoản %s\n", interest, account.getAccountNumber());
//            }
//        }
//    }
//
//    public BankAccount getAccount(String accountNumber)
//    {
//        return accounts.get(accountNumber);
//    }
//
//    public synchronized void transferMoney(String fromAcc, String toAcc, double amount) {
//        BankAccount source = getAccount(fromAcc);
//        BankAccount target = getAccount(toAcc);
//
//        if (source == null || target == null)
//        {
//            throw new IllegalArgumentException("Lỗi: Tài khoản nguồn hoặc đích không tồn tại.");
//        }
//        if (amount <= 0)
//        {
//            throw new IllegalArgumentException("Lỗi: Số tiền chuyển phải lớn hơn 0.");
//        }
//
//        try {
//            source.withdraw(amount);
//            target.deposit(amount);
//
//            accountDAO.updateBalance(source);
//            accountDAO.updateBalance(target);
//
//            long txId = transactionIdCounter.incrementAndGet();
//            System.out.printf("💸 [TX#%d] Chuyển thành công %.2f từ %s sang %s.\n", txId, amount, fromAcc, toAcc);
//        }
//        catch (IllegalArgumentException e)
//        {
//            System.err.printf("❌ [TX FAILED] Giao dịch thất bại: %s (Từ %s).\n", e.getMessage(), fromAcc);
//        }
//        catch(Exception e)
//        {
//            System.err.printf("❌ [TX FAILED] Lỗi hệ thống khi chuyển tiền: " + e.getMessage());
//        }
//    }
//
//    public void saveAllAccounts() {
//        for(BankAccount account: accounts.values())
//        {
//            accountDAO.updateBalance(account);
//        }
//    }
//
//    public void printAllAccountsStatus() {
//        System.out.println("\n--- TRẠNG THÁI TÀI KHOẢN (RAM) ---");
//        accounts.values().forEach(System.out::println);
//        System.out.println("------------------------------------");
//    }
//
//    public void syncAccountToDatabase(BankAccount account) {
//        accountDAO.updateBalance(account);
//    }
//}

package com.javacourse.banking.service;

import com.javacourse.banking.dao.AccountDAO;
import com.javacourse.banking.model.BankAccount;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class BankService
{
    private final AccountDAO accountDAO;
    private final Map<String, BankAccount> mapAccounts;
    private final Set<String> registeredEmail;
    private final AtomicLong transactionIdCounter = new AtomicLong(0);

    public BankService()
    {
        accountDAO = new AccountDAO();
        Map<String, BankAccount> loadedAccounts = accountDAO.loadAllAccounts();
        mapAccounts = new ConcurrentHashMap<>(loadedAccounts);

        registeredEmail = Collections.newSetFromMap(new ConcurrentHashMap<>());
        for (var acc: loadedAccounts.values())
        {
            registeredEmail.add(acc.getEmail());
        }
        System.out.println("✅ [BankService] Đã khởi tạo dịch vụ, sẵn sàng giao dịch.");

        //
    }

    public BankAccount createAccount(String ownerName, String email, double initialDeposit)
    {
        if (registeredEmail.contains(email))
            throw new IllegalArgumentException("❌ Email đã được đăng ký!");
        if (initialDeposit < 0)
            throw new IllegalArgumentException("❌ Số dư ban đầu không được âm.");

        String  newAccNum = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        BankAccount newAccount = new BankAccount(newAccNum, ownerName, email, initialDeposit);

        mapAccounts.put(newAccNum, newAccount);
        registeredEmail.add(email);

        accountDAO.saveNewAccount(newAccount);
        return newAccount;
    }

    public void applyInterest()
    {
        double interestRate = 0.0001;
        for (var acc: mapAccounts.values())
        {
            double currentBalance = acc.getBalance();
            double interest = currentBalance * interestRate;

            if (interest > 0)
                acc.deposit(interest);
        }
    }

    public BankAccount getAccount(String accountNumber)
    {
        return mapAccounts.get(accountNumber);
    }

    public synchronized void transferMoney(String fromAcc, String toAcc, double amount)
    {
        var sourceAcc = mapAccounts.get(fromAcc);
        var targetAcc = mapAccounts.get(toAcc);

        if (sourceAcc == null || targetAcc == null)
            throw new IllegalArgumentException("Tài khoản nguồn hoặc đích không tồn tại.");
        if (amount <= 0)
            throw new IllegalArgumentException("Số tiền chuyền phải lớn hơn 0");

        try
        {
            sourceAcc.withdraw(amount);
            targetAcc.deposit(amount);

            accountDAO.updateBalance(sourceAcc);
            accountDAO.updateBalance(targetAcc);

            long txId = transactionIdCounter.incrementAndGet();
            System.out.printf("💸 [TX#%d] Chuyển thành công %.2f từ %s sang %s.\n", txId, amount, fromAcc, toAcc);
        }
        catch (IllegalArgumentException e)
        {
            System.err.printf("❌ [TX FAILED] Giao dịch thất bại: %s (Từ %s).\n", e.getMessage(), fromAcc);
        }
        catch(Exception e)
        {
            System.err.printf("❌ [TX FAILED] Lỗi hệ thống khi chuyển tiền: " + e.getMessage());
        }
    }

    public void syncAccountToDatabase(BankAccount account)
    {
        accountDAO.updateBalance(account);
    }

    public void saveAllAccounts()
    {
        for (var acc: mapAccounts.values())
        {
            accountDAO.updateBalance(acc);
        }
    }

    public void printAllAccountsStatus()
    {
        System.out.println("\n--- TRẠNG THÁI TÀI KHOẢN (RAM) ---");
        mapAccounts.values().forEach(System.out::println);
        System.out.println("------------------------------------");
    }
}