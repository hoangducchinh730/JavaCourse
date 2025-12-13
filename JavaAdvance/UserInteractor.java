package JavaAdvance;

import java.util.List;

public interface UserInteractor
{
    void printMessage(String message);
    void printMenu();
    void printAvailableVehicles(List<Vehicle> availableVehicles);
    int readUserOption();
    String readVehicleId(); // Hàm cũ, dùng cho logic thuê xe phức tạp
    String readRawInput(String prompt);
    int readRentalDays();
    Transaction readTransactionData(VehicleRegistry vehiclesRegister);
    void printAllTransactions(List<Transaction> allTransactions);
    void exit();
    boolean confirmTransaction(double actualPrice);
}
