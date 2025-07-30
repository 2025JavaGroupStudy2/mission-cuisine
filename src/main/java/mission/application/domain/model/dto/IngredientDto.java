package mission.application.domain.model.dto;

import mission.application.domain.enums.Unit;

public record IngredientDto(int id, String name, int amount, Unit unit) {
}
