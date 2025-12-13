package JavaAdvance;

@FunctionalInterface
public interface VehicleFilter {
    boolean test(Vehicle vehicle);
}