package rmit;

import java.util.ArrayList;
import java.util.List;

public class BusRepository {
    private List<Bus> buses = new ArrayList<>();

    // Add: rejects duplicate bus IDs (B1)
    public boolean add(Bus bus) {
        for (Bus b : buses) {
            if (b.getBusID().equals(bus.getBusID())) {
                return false;
            }
        }
        buses.add(bus);
        return true;
    }

    public int count() {
        return buses.size();
    }
}

