package JavaCore;

import javax.imageio.spi.IIOServiceProvider;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

// Class này implement Interface VehicleRegistry
public class VehiclesRegistry implements VehicleRegistry {
    private List<Vehicle> fleets;
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
        this.fleets.add(new Car("C02", "Toyota", "Vios"));
        this.fleets.add(new Car("C03", "Toyota", "Vios"));
        this.fleets.add(new Car("M01", "Toyota", "Vios"));
        this.fleets.add(new Car("M02", "Toyota", "Vios"));
        this.fleets.add(new Car("T01", "Toyota", "Vios"));
    }

    @Override
    public void loadData() throws IOException {
        List<Vehicle> loadedVehicles = fileHandler.read(filePath);
        if (loadedVehicles.isEmpty()) {
            seedData();
            saveData();
        } else {
            this.fleets = loadedVehicles;
        }
    }

    @Override
    public void saveData() throws IOException {
        fileHandler.write(filePath, fleets);
    }

    @Override
    public void updateVehicleStatus(String id, VehicleStatus status) throws IOException {
        for (Vehicle vehicle : fleets) {
            if (vehicle.getId().equals(id)) {
                vehicle.setStatus(status);
                saveData();
                return;
            }
        }
    }

    @Override
    public List<Vehicle> getAllAvailableVehicles() {
        List<Vehicle> availableVehicles = new ArrayList<>();
        for (Vehicle vehicle : fleets) {
            if (vehicle.getStatus() == VehicleStatus.AVAILABLE)
                availableVehicles.add(vehicle);
        }
        return availableVehicles;
    }

    @Override
    public Vehicle getVehicleById(String id) {
        for (Vehicle vehicle : fleets) {
            if (vehicle.getId().equals(id))
                return vehicle;
        }
        return null;
    }
}