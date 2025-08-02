package mission.application.port.out;

import java.util.List;

public interface Output {
    void correctIngredients(List<String> ingredients, List<String> correctedIngredients);
    void availableCuisine(List<String> availableCuisines);
    void recommendCuisine(List<String> recommendCuisines);
}
