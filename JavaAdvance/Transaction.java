package JavaAdvance;

import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    private final String id;
    private final String vehicleId;
    private final String customerName;
    private final LocalDate startDate;
    private LocalDate returnDate; // Không dùng final vì nó thay dổi
    private double totalPrice; // Không dùng final vì nó thay dổi
    boolean isReturned; // Không dùng final vì nó thay dổi

    // Constructor khi tạo mới (Mượn xe)
    public Transaction(String vehicleId, String customerName) {
        this.id = UUID.randomUUID().toString();
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.startDate = LocalDate.now();
        this.returnDate = null;
        this.totalPrice = 0.0;
        this.isReturned = false;
    }

    // Constructor đầy đủ (Dùng khi load từ file)
    public Transaction(String id, String vehicleId,
                       String customerName, LocalDate startDate,
                       LocalDate returnDate, double totalPrice, boolean isReturned) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.returnDate = returnDate;
        this.totalPrice = totalPrice;
        this.isReturned = isReturned;
    }

    public void returnVehicle(double actualPrice)
    {
        returnDate = LocalDate.now();
        isReturned = true;
        totalPrice = actualPrice;
    }

    // Getters
    public String getId() { return id; }
    public String getVehicleId() { return vehicleId; }
    public String getCustomerName() { return customerName; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getTotalPrice() { return totalPrice; }
    public boolean isReturned() {return isReturned; }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return String.format("TransID: %s | Vehicle: %s | Customer: %s | Date: %s", id, vehicleId, customerName, startDate);
    }
}
