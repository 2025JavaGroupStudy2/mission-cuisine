package mission.application.port.in;

import mission.application.domain.model.dto.IngredientRow;

public interface IngredientPersistence {
    IngredientRow findByName(String name);
}
