package JavaAdvance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Class này implement Interface VehicleRegistry
public class VehiclesRegistry implements VehicleRegistry {
    private final List<Vehicle> fleets; // fleets là final tức là ta không gán nó được nhưng list bên trong thì thêm bớt xóa sửa được
    private final StorageService<Vehicle> fileHandler;
    private final String filePath;

    public VehiclesRegistry(
            StorageService<Vehicle> fileHandler,
            String filePath
    ) throws IOException {
        this.fileHandler = fileHandler;
        this.filePath = filePath;
        this.fleets = new ArrayList<Vehicle>();

        loadData();
    }

    private void seedData() {
        this.fleets.add(new Car("C01", "Toyota", "Vios"));
        this.fleets.add(new Car("C02", "Honda", "City"));

        // SỬA: Thêm Motorbike và Truck
        this.fleets.add(new Motorbike("M01", "Yamaha", "Exciter"));
        this.fleets.add(new Motorbike("M02", "Honda", "Wave"));

        this.fleets.add(new Truck("T01", "Hino", "Series 300"));
    }

    @Override
    public void loadData() throws IOException {
        List<Vehicle> loadedVehicles = fileHandler.read(filePath);
        if (loadedVehicles.isEmpty()) {
            seedData();
            saveData();
        } else {
            fleets.clear();
            fleets.addAll(loadedVehicles);
        }
    }

    @Override
    public void saveData() throws IOException {
        fileHandler.write(filePath, fleets);
    }

    // Hàm mới
    @Override
    public List<Vehicle> filter(VehicleFilter filter) {
        return fleets.stream()
                .filter(v -> filter.test(v))
                .collect(Collectors.toList());
    }

    // Refactor sử dụng Stream
    @Override
    public void updateVehicleStatus(String id, VehicleStatus status) throws IOException {
        var foundVehicle = fleets.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (foundVehicle != null)
        {
            foundVehicle.setStatus(status);
            saveData();
        }
    }

    // Refactor sử dụng filter() + lambda
    @Override
    public List<Vehicle> getAllAvailableVehicles()
    {
        return filter(v -> v.getStatus() == VehicleStatus.AVAILABLE);
    }

    // Refactor sử dụng Stream + Refactor sử dụng Option thay vì .orElse(null)
    @Override
    public Optional<Vehicle> getVehicleById(String id)
    {
        return fleets.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }
}