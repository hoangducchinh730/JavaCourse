package JavaCore;

public class Motorbike extends Vehicle {
    private static final double MOTORBIKE_BASE_PRICE = 150000.0;
    private static final double MOTORBIKE_FACTOR = 0.9;

    public Motorbike(String id, String brand, String model) {
        super(id, VehicleType.MOTORBIKE, brand, model, MOTORBIKE_BASE_PRICE, VehicleStatus.AVAILABLE);
    }

    public Motorbike(String id, String brand, String model, VehicleStatus status) {
        super(id, VehicleType.MOTORBIKE, brand, model, MOTORBIKE_BASE_PRICE, status);
    }

    @Override
    public double calculatePrice(int days) {
        return (this.getBasePrice() * days) * MOTORBIKE_FACTOR;
    }
}
