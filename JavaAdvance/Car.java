package JavaAdvance;

public class Car extends Vehicle {
    private static final double CAR_BASE_PRICE = 500000.0;
    private static final double CAR_FACTOR = 1.0;

    public Car(String id, String brand, String model) {
        super(id, VehicleType.CAR, brand, model, CAR_BASE_PRICE, VehicleStatus.AVAILABLE);
    }

    public Car(String id, String brand, String model, VehicleStatus status) {
        super(id, VehicleType.CAR, brand, model, CAR_BASE_PRICE, status);
    }

    @Override
    public double calculatePrice(int days) {
        return (this.getBasePrice() * days) * CAR_FACTOR;
    }
}