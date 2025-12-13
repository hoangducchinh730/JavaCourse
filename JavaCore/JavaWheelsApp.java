package JavaCore;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class JavaWheelsApp {
    private final TransactionRepository transactionRepository;
    private final VehicleRegistry vehicleRegistry;
    private final UserInteractor userInteractor;

    public JavaWheelsApp(
            TransactionRepository transactionRepository,
            VehicleRegistry vehicleRegistry,
            UserInteractor userInteractor
    ) {
        this.transactionRepository = transactionRepository;
        this.vehicleRegistry = vehicleRegistry;
        this.userInteractor = userInteractor;
    }

    public long calculateRentalDays(LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return Math.max(1, days);
    }

    public void run() throws IOException {
        while (true) {
            userInteractor.printMenu();
            int userOption = userInteractor.readUserOption();
            switch (userOption) {
                case 1:
                    var availableVehicles = vehicleRegistry.getAllAvailableVehicles();
                    userInteractor.printAvailableVehicles(availableVehicles);

                    var newTransaction = userInteractor.readTransactionData(vehicleRegistry);
                    double estimatedPrice = newTransaction.getTotalPrice();

                    if (userInteractor.confirmTransaction(estimatedPrice)) {
                        newTransaction.setTotalPrice(0.0);
                        transactionRepository.addTransaction(newTransaction);
                        vehicleRegistry.updateVehicleStatus(newTransaction.getVehicleId(), VehicleStatus.RENTED);
                        userInteractor.printMessage("Giao dịch đã được lưu.");
                    }
                    else {
                        userInteractor.printMessage("Giao dịch đã bị hủy.");
                    }

                    break;
                case 2:
                    String returnedId = userInteractor.readRawInput("Nhập ID của xe muốn trả: ");

                    Transaction returnedTransaction = transactionRepository.findOpenTransactionByVehicle(returnedId);
                    if (returnedTransaction == null) {
                        userInteractor.printMessage("❌ Lỗi: Xe này không có giao dịch thuê đang mở.");
                        break;
                    }

                    // Tính số ngày mượn thực tế
                    LocalDate startDate = returnedTransaction.getStartDate();
                    LocalDate currentDate = LocalDate.now();
                    long rentalDays = calculateRentalDays(startDate, currentDate);

                    // Trả xe
                    Vehicle vehicle = vehicleRegistry.getVehicleById(returnedId);
                    double actualPrice = vehicle.calculatePrice((int) rentalDays);

                    // Cập nhật trạng thái xe
                    transactionRepository.returnVehicle(returnedTransaction.getId(), actualPrice);
                    vehicleRegistry.updateVehicleStatus(returnedId, VehicleStatus.AVAILABLE);

                    // In thông báo
                    userInteractor.printMessage("✅ Hoàn tất trả xe! Tổng tiền: " + actualPrice + " VND.");
                    userInteractor.printMessage(String.format("   Chi tiết: %d ngày thuê, Tổng tiền: %.0f VND.", (int) rentalDays, actualPrice));

                    break;
                case 3:
                    var allTransactions = transactionRepository.readAllTransactions();
                    userInteractor.printAllTransactions(allTransactions);
                    break;
                case 4:
                    userInteractor.exit();
                    return;
                default:
                    userInteractor.printMessage("Lựa chọn không hợp lệ. Vui lòng chọn 1-4.");
            }
        }
    }
}
