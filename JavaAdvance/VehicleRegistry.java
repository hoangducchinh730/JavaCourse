package JavaAdvance;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface VehicleRegistry {
    void loadData() throws IOException;
    void saveData() throws IOException;
    void updateVehicleStatus(String id, VehicleStatus status) throws IOException;

    List<Vehicle> getAllAvailableVehicles();
    Optional<Vehicle> getVehicleById(String id);

    // Hàm mới
    List<Vehicle> filter(VehicleFilter filter);
}
