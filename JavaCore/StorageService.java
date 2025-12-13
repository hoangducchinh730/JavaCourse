package JavaCore;

import java.io.IOException;
import java.util.List;

public interface StorageService<T>
{
    List<T> read(String filePath) throws IOException; // vì sao phải có cái này ??
    void write(String filePath, List<T> strings) throws IOException;
}
