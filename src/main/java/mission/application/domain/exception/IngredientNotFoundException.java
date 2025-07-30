package mission.application.domain.exception;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException() {
        super("존재하지 않는 재료입니다.");
    }
}
