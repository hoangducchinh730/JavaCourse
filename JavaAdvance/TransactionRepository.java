package JavaAdvance;

import java.io.IOException;
import java.util.List;

public interface TransactionRepository
{
    List<Transaction> readAllTransactions() throws IOException;
    void addTransaction(Transaction transaction) throws IOException;
    Transaction findOpenTransactionByVehicle(String vehicleId) throws IOException;
    void returnVehicle(String transactionId, double actualPrice) throws IOException;
    double getTotalRevenue() throws IOException;
}
