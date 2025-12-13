package JavaCore;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // --- 1. KHAI BÁO CÁC HẰNG SỐ ---
        final String VEHICLES_FILE_PATH = "JavaAdvance/vehicles.csv";
        final String TRANSACTIONS_FILE_PATH = "JavaCore/transactions.csv";

        // --- 2. KHỞI TẠO CÁC DEPENDENCY (BOTTOM-UP) ---

        // 2a. File Handlers (Storage Service)
        // Chúng ta sử dụng CsvFileHandler cho cả 2 loại dữ liệu
        // Cần tạo 2 instance vì chúng làm việc với 2 kiểu Object khác nhau (Vehicle, Transaction)
        AbstractCsvHandler<Vehicle> vehicleFileHandler = new VehicleCsvHandler();
        AbstractCsvHandler<Transaction> transactionFileHandler = new TransactionCsvHandler();

        // 2b. Vehicle Registry (Cần File Handler của Vehicle)
        VehicleRegistry vehicleRegistry;
        try {
            // Tự động load data nếu có, hoặc tạo data mẫu và lưu xuống file
            vehicleRegistry = new VehiclesRegistry(vehicleFileHandler, VEHICLES_FILE_PATH);
        } catch (IOException e) {
            System.err.println("❌ Lỗi nghiêm trọng khi khởi tạo dữ liệu xe: " + e.getMessage());
            e.printStackTrace();
            return; // Dừng chương trình nếu lỗi data
        }

        // 2c. Transaction Repository (Cần File Handler của Transaction)
        TransactionRepository transactionRepo = new TransactionFileRepository(
                transactionFileHandler,
                TRANSACTIONS_FILE_PATH
        );

        // 2d. User Interactor (Cần Vehicle Registry để kiểm tra ID)
        UserInteractor userInteractor = new ConsoleUserInteractor(vehicleRegistry);


        // --- 3. KHỞI CHẠY ỨNG DỤNG LÕI ---

        JavaWheelsApp app = new JavaWheelsApp(
                transactionRepo,
                vehicleRegistry,
                userInteractor
        );

        try
        {
            app.run();
        }
        catch (IOException e) {
            // Xử lý lỗi IO không lường trước được trong suốt quá trình chạy
            userInteractor.printMessage("❌ Lỗi IO không lường trước: " + e.getMessage());
            e.printStackTrace();
        }
    }
}