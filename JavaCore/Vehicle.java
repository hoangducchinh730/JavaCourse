package JavaCore;

public abstract class Vehicle {
    private String id;
    private VehicleType type;
    private String brand;
    private String model;
    private double basePrice;
    private VehicleStatus status;

    public Vehicle(String id,
                   VehicleType type,
                   String brand,
                   String model,
                   double basePrice,
                   VehicleStatus status) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.basePrice = basePrice;
        this.status = status;
    }

    public abstract double calculatePrice(int days);

    // Getters / Setters
    public String getId() { return id; }
    public VehicleType getType() { return type; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getBasePrice() { return basePrice; }
    public VehicleStatus getStatus() { return status; }

    public void setStatus(VehicleStatus status) { this.status = status; }

    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%.0f,%s",
                id, type, brand, model, basePrice, status);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s - %.0f VND/ngày - Trạng thái: %s",
                id, brand, model, basePrice, status);
    }
}
