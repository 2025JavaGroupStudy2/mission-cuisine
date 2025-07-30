package mission.application.domain.model.dto;

import mission.application.domain.enums.Unit;

public record IngredientRow(int id, String name, Unit unit) {
}
