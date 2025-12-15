//package com.javacourse.banking.task;
//
//import com.javacourse.banking.service.BankService;
//
//public class WithdrawalTask implements Runnable {
//
//    private final BankService bankService;
//    private final String sourceAccount;
//    private final String targetAccount;
//    private final double amount;
//    private final String threadName;
//
//    public WithdrawalTask(BankService bankService, String sourceAccount, String targetAccount, double amount, String threadName) {
//        this.bankService = bankService;
//        this.sourceAccount = sourceAccount;
//        this.targetAccount = targetAccount;
//        this.amount = amount;
//        this.threadName = threadName;
//    }
//
//    @Override
//    public void run() {
//        Thread.currentThread().setName(threadName);
//
//        System.out.println("🤖 " + threadName + " bắt đầu chuyển tiền...");
//
//        try {
//            // Thực hiện giao dịch (Chuyển tiền là nghiệp vụ có nguy cơ Race Condition)
//            bankService.transferMoney(sourceAccount, targetAccount, amount);
//
//        } catch (Exception e) {
//            System.err.println("❌ " + threadName + " lỗi: " + e.getMessage());
//        }
//    }
//}

package com.javacourse.banking.task;

import com.javacourse.banking.model.BankAccount;
import com.javacourse.banking.service.BankService;

public class WithdrawalTask implements Runnable
{
    private final BankService bankService;
    private final String sourceAcc;
    private final String targetAcc;
    private final double amount;
    private final String threadName;

    public WithdrawalTask(
            BankService bankService,
            String sourceAcc,
            String targetAcc,
            double amount,
            String threadName
    )
    {
        this.bankService = bankService;
        this.sourceAcc = sourceAcc;
        this.targetAcc = targetAcc;
        this.amount = amount;
        this.threadName = threadName;
    }

    @Override
    public void run()
    {
        Thread.currentThread().setName(threadName);
        System.out.println("🤖 " + threadName + " bắt đầu chuyển tiền...");
        try
        {
            bankService.transferMoney(sourceAcc, targetAcc, amount);
        }
        catch (Exception e)
        {
            System.err.println("❌ " + threadName + " lỗi: " + e.getMessage());
        }

    }
}