package JavaAdvance;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ConsoleUserInteractor implements UserInteractor {

    private final Scanner scanner;
    private final VehicleRegistry vehicleRegistry;

    public ConsoleUserInteractor(
            VehicleRegistry vehicleRegistry
    )
    {
        scanner = new Scanner(System.in);
        this.vehicleRegistry = vehicleRegistry;
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    // Cập nhật thêm Menu cho phép in doanh thu
    @Override
    public void printMenu() {
        String menu = "--- JAVA WHEELS MENU ---\n"
                + "1. Rent a vehicle (Thuê xe)\n"
                + "2. Return a vehicle (Trả xe)\n"
                + "3. View transaction history (Lịch sử)\n"
                + "4. View Revenue Report (Báo cáo doanh thu) [NEW]\n"
                + "5. Exit\n"
                + "------------------------";
        printMessage(menu);
    }

    // Refactor sử dụng Method reference
//    @Override
//    public void printAvailableVehicles(List<Vehicle> availableVehicles) {
//        printMessage("Danh sách các xe có sẵn: ");
//        availableVehicles.forEach(System.out::println); // availableVehicles.forEach(vehicle -> System.out.println(vehicle.toString()))
//    }

    // Refactor sử dụng sorted để in xe có sẵn tăng dần theo basePrice
    @Override
    public void printAvailableVehicles(List<Vehicle> availableVehicles) {
        printMessage("Danh sách các xe có sẵn (Sắp xếp theo giá tăng dần): ");
        availableVehicles.stream()
                .sorted(Comparator.comparing(Vehicle::getBasePrice))
                .forEach(System.out::println);
    }

    @Override
    public String readRawInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    @Override
    public int readUserOption() {
        System.out.print("Chọn chức năng (1-4): ");
        String line = scanner.nextLine().trim();

        try {
            int option = Integer.parseInt(line);
            if (option >= 1 && option <= 4) {
                return option;
            }
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String readVehicleId() {
        String id;
        while(true)
        {
            id = readRawInput("Vui lòng nhập id của xe mà bạn muốn thuê: ");

            if (id.isEmpty())
            {
                printMessage("❌ Lỗi: ID xe không được để trống. Vui lòng nhập lại.");
                continue;
            }

            // Refactor cho phù hợp với cách Optional
            var vehicleOptional = vehicleRegistry.getVehicleById(id);
            if (vehicleOptional.isEmpty())
            {
                printMessage("❌ Lỗi: Không tìm thấy xe với ID '" + id + "'. Vui lòng kiểm tra lại.");
                continue;
            }
            Vehicle vehicle = vehicleOptional.get();

            if(vehicle.getStatus() == VehicleStatus.RENTED)
            {
                printMessage("❌ Lỗi: Xe '" + id + " - " + vehicle.getModel() + "' đang được thuê. Vui lòng chọn xe khác.");
                continue;
            }
            return id;
        }
    }

    @Override
    public int readRentalDays() {
        int days = 0;
        while(true)
        {
            String line = readRawInput("Nhập số ngày thuê (Vui lòng nhập một số nguyên dương): ");

            try
            {
                days = Integer.parseInt(line);
                if (days > 0)
                    return days;
                printMessage("❌ Lỗi: Số ngày thuê phải lớn hơn 0.");
            }
            catch (NumberFormatException e)
            {
                printMessage("❌ Lỗi: Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }

    @Override
    public Transaction readTransactionData(VehicleRegistry vehiclesRegister) {
        System.out.println("\n--- BẮT ĐẦU THUÊ XE ---");

        String vehicleId = readVehicleId();
        String customerName = readRawInput("Nhập tên khách hàng (vd: Nguyen Van A): ");
        int rentalDays = readRentalDays();

        // Refactor cho phù hợp với cách Optional
        Vehicle vehicle = vehiclesRegister.getVehicleById(vehicleId)
                .orElseThrow(() -> new IllegalStateException("Lỗi dữ liệu: Xe không tồn tại!"));

        double estimatedPrice = vehicle.calculatePrice(rentalDays);

        Transaction newTransaction = new Transaction(vehicleId, customerName);
        newTransaction.setTotalPrice(estimatedPrice);
        return newTransaction;
    }

    @Override
    public void printAllTransactions(List<Transaction> allTransactions) {
        printMessage("Danh sách các giao dịch gần đây: ");
        allTransactions.forEach(System.out::println);
    }

    @Override
    public void exit() {
        printMessage("Chương trình kết thúc. Nhấn ENTER để thoát.");
            scanner.nextLine();
    }

    @Override
    public boolean confirmTransaction(double actualPrice) {
        printMessage("Tổng thanh toán của bạn là: " + actualPrice);
            printMessage("Bạn có muốn tiếp tục giao dịch (Y/N) ?");
            String userChoice = scanner.nextLine().trim();
            return userChoice.equalsIgnoreCase("Y") || userChoice.equalsIgnoreCase("Yes");
    }
}
