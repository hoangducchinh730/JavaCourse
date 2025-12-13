package JavaCore;

import java.util.List;
import java.io.IOException;

public interface VehicleRegistry {
    void loadData() throws IOException;
    void saveData() throws IOException;
    void updateVehicleStatus(String id, VehicleStatus status) throws IOException;
    List<Vehicle> getAllAvailableVehicles();
    Vehicle getVehicleById(String id);
}
