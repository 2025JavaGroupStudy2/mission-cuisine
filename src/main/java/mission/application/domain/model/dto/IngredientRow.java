package mission.application.domain.model.dto;

import mission.application.domain.enums.Unit;

public record IngredientRow(int ingredientId, String name, Unit unit) {
}
