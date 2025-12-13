package JavaCore;

public class Truck extends Vehicle {
    private static final double TRUCK_BASE_PRICE = 800000.0;
    private static final double TRUCK_FACTOR = 1.5;

    public Truck(String id, String brand, String model) {
        super(id, VehicleType.TRUCK, brand, model, TRUCK_BASE_PRICE, VehicleStatus.AVAILABLE);
    }

    public Truck(String id, String brand, String model, VehicleStatus status) {
        super(id, VehicleType.TRUCK, brand, model, TRUCK_BASE_PRICE, status);
    }

    @Override
    public double calculatePrice(int days) {
        return (this.getBasePrice() * days) * TRUCK_FACTOR;
    }
}