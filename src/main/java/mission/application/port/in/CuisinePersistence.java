package mission.application.port.in;

import java.util.List;
import mission.application.domain.model.Cuisine;

public interface CuisinePersistence {
    Cuisine findCuisineByName(String name);
    List<Cuisine> filter(List<String> ingredients);
    List<Cuisine> sortPerRate(List<String> inputIngredients);
}
