//package com.javacourse.banking.task;
//
//import com.javacourse.banking.service.BankService;
//
//// Extend Thread để class này tự là một luồng
//public class InterestCalculationTask extends Thread {
//
//    private final BankService bankService;
//    private final long delayInMillis; // Độ trễ giữa các lần tính (ví dụ: 10 giây)
//
//    public InterestCalculationTask(BankService bankService, long delayInMillis) {
//        this.bankService = bankService;
//        this.delayInMillis = delayInMillis;
//
//        // [MỚI] Đánh dấu luồng này là Daemon Thread
//        // Daemon Thread là luồng nền, nó sẽ tự động tắt khi tất cả các luồng chính (non-daemon) tắt.
//        setDaemon(true);
//        setName("INTEREST-CALCULATOR");
//    }
//
//    @Override
//    public void run() {
//        System.out.println("🌟 [SERVICE] Tính lãi suất: Bắt đầu chạy dịch vụ nền.");
//
//        // Vòng lặp vô hạn (chạy liên tục)
//        while (true) {
//            try {
//                // Tạm dừng luồng trong khoảng thời gian đã định
//                Thread.sleep(delayInMillis);
//
//                // Thực hiện logic tính lãi suất
//                bankService.applyInterest();
//
//                // Sau khi tính xong, lưu tất cả thay đổi xuống DB
//                bankService.saveAllAccounts();
//
//            } catch (InterruptedException e) {
//                // Xử lý nếu luồng bị ngắt (thường khi ứng dụng đóng)
//                System.out.println("--- Dịch vụ lãi suất đã bị ngắt.");
//                break;
//            }
//        }
//    }
//}

package com.javacourse.banking.task;

import com.javacourse.banking.service.BankService;

public class InterestCalculationTask extends Thread
{
    private final BankService bankService;
    private final long delayInMillis;

    public InterestCalculationTask(
            BankService bankService,
            long delayInMillis
    )
    {
        this.bankService = bankService;
        this.delayInMillis = delayInMillis;

        setDaemon(true);
        setName("INTEREST-CALCULATOR");
    }

    @Override
    public void run()
    {
        System.out.println("🌟 [SERVICE] Tính lãi suất: Bắt đầu chạy dịch vụ nền.");
        while(true)
        {
            try {
                bankService.applyInterest();
                Thread.sleep(delayInMillis);
                bankService.saveAllAccounts();
            }
            catch (InterruptedException e)
            {
                System.out.println("--- Dịch vụ lãi suất đã bị ngắt.");
                break;
            }
        }
    }
}