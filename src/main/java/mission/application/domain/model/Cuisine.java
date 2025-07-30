package mission.application.domain.model;

import java.util.List;
import mission.application.domain.model.dto.IngredientDto;

public record Cuisine(int id, String chefName, String cuisineName, List<IngredientDto> ingredients) {
}
