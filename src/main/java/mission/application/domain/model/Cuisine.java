package mission.application.domain.model;

import java.util.List;
import mission.application.domain.model.dto.IngredientDto;

public record Cuisine(String chefName, String cuisineName, List<IngredientDto> ingredients) {
}
