package com.javacourse.banking.app;

import com.javacourse.banking.model.BankAccount;
import com.javacourse.banking.service.BankService;
import com.javacourse.banking.task.WithdrawalTask;

public class BankingSimulationApp {

    public static void main(String[] args) throws InterruptedException {
        BankService bankService = new BankService();
        bankService.printAllAccountsStatus(); // In trạng thái ban đầu

        //  ---------------------------------------------------------------------
        //  TEST CASE 1: Kiểm tra tính năng tạo tài khoản và Set (Duy nhất Email)
        //  ---------------------------------------------------------------------
        //  testCreateAccount(bankService);

        // ---------------------------------------------------------------------
        // TEST CASE 2: Kiểm tra Đa luồng & Đồng bộ hóa (Race Condition)
        // ---------------------------------------------------------------------
        // testConcurrency(bankService);

        // ---------------------------------------------------------------------
        // TEST CASE 3: Kiểm tra Task nền (Lãi suất) và Dữ liệu Map
        // ---------------------------------------------------------------------
          testBackgroundTask(bankService);


        Thread.sleep(15000);
        System.out.println("\n--- TRẠNG THÁI CUỐI CÙNG SAU KHI KẾT THÚC ---");
        bankService.printAllAccountsStatus();

        // Lệnh này đảm bảo tất cả thay đổi trên RAM được lưu xuống DB trước khi thoát
        bankService.saveAllAccounts();
        System.out.println("✅ Đã lưu toàn bộ thay đổi xuống Database.");
    }

    // =================================================================
    // KỊCH BẢN 1: TEST TÍNH NĂNG TẠO TÀI KHOẢN (Sử dụng Set để check trùng)
    // =================================================================
    //    private static void testCreateAccount(BankService bankService) {
    //        System.out.println("\n===== BẮT ĐẦU TEST CASE 1: TẠO TÀI KHOẢN (SET) =====");
    //        try {
    //            // Lần 1: Thành công
    //            BankAccount acc1 = bankService.createAccount("Tuan Anh", "tuan.anh@mail.com", 100000.00);
    //            System.out.println("✅ Tạo tài khoản mới thành công: " + acc1.getAccountNumber());
    //
    //            // Lần 2: Thất bại vì trùng Email (Logic Set)
    //            bankService.createAccount("Hoang B", "tuan.anh@mail.com", 50000.00);
    //
    //        } catch (IllegalArgumentException e) {
    //            System.err.println("❌ Lỗi Mong Muốn: " + e.getMessage());
    //        }
    //    }

    // =================================================================
    // KỊCH BẢN 2: TEST ĐA LUỒNG VÀ ĐỒNG BỘ HÓA (Race Condition)
    // =================================================================
    //    private static void testConcurrency(BankService bankService) throws InterruptedException {
    //        System.out.println("\n===== BẮT ĐẦU TEST CASE 2: ĐA LUỒNG & RACE CONDITION =====");
    //
    //        String sourceAcc = "112233";
    //        String targetAcc = "445566";
    //        double amountToTransfer = 4000000.00;
    //
    //        // [QUAN TRỌNG] Đảm bảo tài khoản có đủ 5M trước khi chạy test
    //        resetAccountBalance(bankService, bankService.getAccount(sourceAcc), 5000000.00);
    //
    //        // 1. Tạo 2 Task rút tiền cùng một tài khoản
    //        Runnable task1 = new WithdrawalTask(bankService, sourceAcc, targetAcc, amountToTransfer, "ATM-WIFE");
    //        Runnable task2 = new WithdrawalTask(bankService, sourceAcc, targetAcc, amountToTransfer, "APP-HUSBAND");
    //
    //        // 2. Tạo 2 Threads và chạy gần như đồng thời
    //        Thread thread1 = new Thread(task1);
    //        Thread thread2 = new Thread(task2);
    //
    //        System.out.println("--- Trạng thái BAN ĐẦU ---");
    //        System.out.println("Tài khoản NGUỒN: " + bankService.getAccount(sourceAcc).toString());
    //        System.out.println("--------------------------");
    //
    //        thread1.start();
    //        thread2.start();
    //
    //        // 3. Dùng join() để Thread chính đợi 2 luồng con chạy xong.
    //        thread1.join();
    //        thread2.join();
    //
    //        System.out.println("===== KẾT THÚC TEST CASE 2 (Rút tiền) =====");
    //    }

    // =================================================================
    // KỊCH BẢN 3: TEST TASK NỀN (Daemon Thread)
    // =================================================================
    private static void testBackgroundTask(BankService bankService) throws InterruptedException {
        System.out.println("\n===== BẮT ĐẦU TEST CASE 3: TASK NỀN (LÃI SUẤT) =====");
        System.out.println("Đang đợi 15 giây để Task nền chạy 1-2 lần...");

        // In trạng thái trước khi đợi
        BankAccount accA = bankService.getAccount("112233");
        double balanceBefore = accA.getBalance();

        // Đợi 15 giây (Task chạy mỗi 10s, nên nó sẽ chạy ít nhất 1 lần)
        Thread.sleep(15000);

        // In trạng thái sau khi đợi
        double balanceAfter = accA.getBalance();
        System.out.printf("Số dư trước: %.2f | Số dư sau: %.2f | Tăng: %.2f\n",
                balanceBefore, balanceAfter, (balanceAfter - balanceBefore));
        System.out.println("===== KẾT THÚC TEST CASE 3 (Lãi suất) =====");
    }

    // =================================================================
    // HÀM HỖ TRỢ (PRIVATE)
    // =================================================================

    // Hàm reset số dư về một giá trị cụ thể (Đồng bộ RAM và DB)
    //    private static void resetAccountBalance(BankService bankService, BankAccount account, double targetBalance) {
    //        if (account == null) return;
    //
    //        double currentBalance = account.getBalance();
    //        double diff = targetBalance - currentBalance;
    //
    //        // Dùng Deposit để cộng/trừ (chuyển về targetBalance)
    //        if (diff != 0) {
    //            account.deposit(diff);
    //            bankService.syncAccountToDatabase(account);
    //        }
    //    }
}