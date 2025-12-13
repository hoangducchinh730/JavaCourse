package JavaAdvance;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TransactionFileRepository implements TransactionRepository {
    private final StorageService<Transaction> fileHandler;
    private final String filePath;

    public TransactionFileRepository(
            StorageService<Transaction> fileHandler,
            String filePath
    )
    {
        this.fileHandler = fileHandler;
        this.filePath = filePath;
    }

    @Override
    public List<Transaction> readAllTransactions() throws IOException {
        return fileHandler.read(filePath);
    }

    @Override
    public void addTransaction(Transaction newTransaction) throws IOException {
        List<Transaction> allTransactions = fileHandler.read(filePath);
        allTransactions.add(newTransaction);
        fileHandler.write(filePath,allTransactions);
    }

    // Refactor sử dụng stream
    @Override
    public Transaction findOpenTransactionByVehicle(String vehicleId) throws IOException {
        List<Transaction> allTransactions = fileHandler.read(filePath);
        return allTransactions.stream()
                .filter(trans -> trans.getVehicleId().equals(vehicleId))
                .filter(trans -> !trans.isReturned())
                .findFirst()
                .orElse(null);
    }

    // Refactor code
    @Override
    public void returnVehicle(String transactionId, double actualPrice) throws IOException {
        List<Transaction> allTransactions = fileHandler.read(filePath);

        Optional<Transaction> transactionToUpdate =
                allTransactions.stream()
                        .filter(trans -> trans.getId().equals(transactionId))
                        .findFirst();

        if (transactionToUpdate.isPresent())
        {
            Transaction trans = transactionToUpdate.get();
            trans.returnVehicle(actualPrice);
            fileHandler.write(filePath, allTransactions);
        }
    }

    @Override
    public double getTotalRevenue() throws IOException {
        var allTransactions = readAllTransactions();
        return allTransactions.stream()
                .filter(Transaction::isReturned)
                .map(Transaction::getTotalPrice)
                .reduce(0.0, Double::sum);
    }
}