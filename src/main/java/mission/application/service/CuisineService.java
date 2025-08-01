package mission.application.service;

import java.util.List;
import mission.application.domain.model.Cuisine;
import mission.application.port.in.CuisinePersistence;
import mission.application.port.in.IngredientPersistence;
import mission.application.port.in.Input;
import mission.application.port.out.Output;
import mission.utility.Validator;

public class CuisineService {
    private final Input input;
    private final Output output;
    private final CuisinePersistence cuisinePersistence;
    private final IngredientPersistence ingredientPersistence;

    public CuisineService(Input input, Output output, CuisinePersistence cuisinePersistence, IngredientPersistence ingredientPersistence){
        this.input = input;
        this.output = output;
        this.cuisinePersistence = cuisinePersistence;
        this.ingredientPersistence = ingredientPersistence;
    }

    public List<String> getInput(){
        List<String> ingredients = input.getIngredients();
        Validator.duplicateCheck(ingredients);
        ingredients.forEach(ingredientPersistence::findIngredientByName);
        return ingredients;
    }

    public void showAvailableCuisine(List<String> ingredients) {
        List<String> availableCuisines = cuisinePersistence.filter(ingredients).stream()
                .map(Cuisine::cuisineName)
                .toList();
        output.availableCuisine(availableCuisines);
    }
}
