package mission.application.port.in;

import java.util.List;
import mission.application.domain.model.Cuisine;

public interface CuisinePersistence {
    Cuisine findByName(String name);
    List<Cuisine> filter(List<Cuisine> cuisines);
}
