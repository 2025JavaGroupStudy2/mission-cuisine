package mission.adapter.in;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import mission.application.domain.exception.CuisineNotFoundException;
import mission.application.domain.model.Cuisine;
import mission.application.domain.model.dto.CuisineRow;
import mission.application.domain.model.dto.IngredientDto;
import mission.application.domain.model.dto.IngredientRow;
import mission.application.domain.model.dto.RecipeRow;
import mission.application.port.in.CuisinePersistence;

public class CuisineBuilder implements CuisinePersistence {
    private final List<Cuisine> cuisines;

    public CuisineBuilder() {
        cuisines = new ArrayList<>();
        CsvReader csvReader = CsvReader.getInstance();
        for(CuisineRow cuisineRow : csvReader.getCuisineRows()){
            List<RecipeRow> recipeRows = csvReader.findByCuisineId(cuisineRow.cuisineId());
            List<IngredientDto> ingredientDtos = new ArrayList<>();
            for(RecipeRow recipeRow :recipeRows){
                IngredientRow ingredientRow = csvReader.findByIngredientId(recipeRow.ingredientId());
                ingredientDtos.add(new IngredientDto(ingredientRow.ingredientId(), ingredientRow.name(), recipeRow.weight(), ingredientRow.unit()));
            }
            cuisines.add(new Cuisine(cuisineRow.cuisineId(), cuisineRow.chefName(), cuisineRow.cuisineName(), ingredientDtos));
        }
    }

    @Override
    public Cuisine findByName(String name){
        return cuisines.stream()
                .filter(item -> Objects.equals(item.cuisineName(), name))
                .findFirst()
                .orElseThrow(CuisineNotFoundException::new);
    }

    @Override
    public List<Cuisine> filter(List<String> ingredients){
        return cuisines.stream()
                .filter(cuisine -> cuisine.ingredients().stream()
                        .map(IngredientDto::name)
                        .toList()
                        .containsAll(ingredients)
                ).toList();
    }

}
