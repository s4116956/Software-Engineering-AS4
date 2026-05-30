package rmit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class BusRepository {
    
    private final String filePath;     // null => in-memory only
    private final List<Bus> memory;    // backing store used when in-memory
    private static final String DELIMITER = "|||";

    // In-memory repository (no file persistence).
    public BusRepository() {
        this.filePath = null;
        this.memory = new ArrayList<>();
    }

    // File-backed repository (real TXT file).
    public BusRepository(String filePath) {
        this.filePath = filePath;
        this.memory = null;
        try {
            Path path = Paths.get(filePath);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create data directory");
        }
    }

    // Add: rejects null, invalid ID format (B1) and duplicate IDs (B1 uniqueness)
    public boolean add(Bus bus) {
        if (bus == null || !Bus.isValidBusID(bus.getBusID())) return false;
        try {
            List<Bus> buses = loadAll();
            if (findById(buses, bus.getBusID()) != null) return false;
            buses.add(bus);
            saveAll(buses);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Retrieve a bus by ID (null if not found)
    public Bus retrieve(String busID) {
        try {
            return findById(loadAll(), busID);
        } catch (IOException e) {
            return null;
        }
    }

    // Update existing bus details; enforces B2 (capacity must not increase)
    public boolean update(Bus updatedBus) {
        if (updatedBus == null) return false;
        try {
            List<Bus> buses = loadAll();
            Bus existing = findById(buses, updatedBus.getBusID());
            if (existing == null) return false;
            if (updatedBus.getCapacity() > existing.getCapacity()) return false; // B2
            int index = buses.indexOf(existing);
            buses.set(index, updatedBus);
            saveAll(buses);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Return number of stored buses
    public int count() {
        try {
            return loadAll().size();
        } catch (IOException e) {
            return 0;
        }
    }

    // ---- persistence ----

    private List<Bus> loadAll() throws IOException {
        if (filePath == null) {
            return new ArrayList<>(memory);
        }
        List<Bus> list = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return list;
        for (String line : Files.readAllLines(path)) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split("\\|\\|\\|", -1);
            if (parts.length == 4) {
                try {
                    list.add(new Bus(parts[0], Integer.parseInt(parts[1]),
                            Double.parseDouble(parts[2]), parts[3]));
                } catch (Exception ignored) {}
            }
        }
        return list;
    }

    private void saveAll(List<Bus> buses) throws IOException {
        if (filePath == null) {
            memory.clear();
            memory.addAll(buses);
            return;
        }
        Path path = Paths.get(filePath);
        List<String> lines = new ArrayList<>();
        for (Bus b : buses) {
            lines.add(String.join(DELIMITER, b.getBusID(),
                    String.valueOf(b.getCapacity()),
                    String.valueOf(b.getFuelLevel()),
                    b.getFuelType()));
        }
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private Bus findById(List<Bus> list, String id) {
        for (Bus b : list) {
            if (b.getBusID().equals(id)) return b;
        }
        return null;
    }
}

