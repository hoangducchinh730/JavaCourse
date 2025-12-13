package JavaCore;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

    @Override
    public Transaction findOpenTransactionByVehicle(String vehicleId) throws IOException {
        List<Transaction> allTransactions = fileHandler.read(filePath);
        for(Transaction transaction: allTransactions){
            if(transaction.getVehicleId().equals(vehicleId) && !transaction.isReturned()){
                return transaction;
            }
        }
        return null;
    }

    @Override
    public void returnVehicle(String transactionId, double actualPrice) throws IOException {
        List<Transaction> allTransactions = fileHandler.read(filePath);
        for(Transaction transaction: allTransactions)
        {
            if(transaction.getId().equals(transactionId))
            {
                transaction.returnVehicle(actualPrice);
                fileHandler.write(filePath, allTransactions);
                return;
            }
        }
    }
}