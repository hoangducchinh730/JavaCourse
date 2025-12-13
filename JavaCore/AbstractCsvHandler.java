package JavaCore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCsvHandler<T> implements StorageService<T>
{
    protected abstract T parseLine(String line);
    public List<T> read(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path))
            return Collections.emptyList();

        List<String> lines = Files.readAllLines(path);
        List<T> result = new ArrayList<>();

        for (String line : lines) {
            T item = parseLine(line);
            if (item != null)
                result.add(item);
        }
        return result;
    }

    protected abstract String toCsv(T item);

    public void write(String filePath, List<T> data) throws IOException
    {
        Path path = Paths.get(filePath);
        List<String> lines = new ArrayList<String>();
        for(T item: data)
        {
            String line = toCsv(item);
            lines.add(line);
        }
        Files.write(path, lines);
    }
}
