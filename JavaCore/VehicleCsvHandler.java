package JavaCore;

public class VehicleCsvHandler extends AbstractCsvHandler<Vehicle>{
    @Override
    protected Vehicle parseLine(String line) {
        String[] part = line.split(",");

        if (part.length != 5)
            return null;

        String id = part[0];
        VehicleType type = VehicleType.valueOf(part[1]);
        String brand = part[2];
        String model = part[3];
        VehicleStatus status = VehicleStatus.valueOf(part[4]);

        return switch (type) {
            case CAR -> new Car(id, brand, model, status);
            case MOTORBIKE -> new Motorbike(id, brand, model, status);
            case TRUCK -> new Truck(id, brand, model, status);
            default -> null;
        };
    }

    @Override
    protected String toCsv(Vehicle item) {
        return String.join(",",
                item.getId(),
                item.getType().name(),
                item.getBrand(),
                item.getModel(),
                item.getStatus().name());
    }
}
