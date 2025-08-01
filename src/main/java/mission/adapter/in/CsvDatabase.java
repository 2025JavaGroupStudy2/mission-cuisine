package mission.adapter.in;

import java.util.List;
import java.util.Objects;
import mission.application.domain.exception.CuisineNotFoundException;
import mission.application.domain.exception.IngredientNotFoundException;
import mission.application.domain.model.Cuisine;
import mission.application.domain.model.dto.IngredientDto;
import mission.application.domain.model.dto.IngredientRow;
import mission.application.port.in.CuisinePersistence;
import mission.application.port.in.IngredientPersistence;

public class CsvDatabase implements CuisinePersistence, IngredientPersistence {
    private final List<Cuisine> cuisines;
    private final List<IngredientRow> ingredients;

    public CsvDatabase() {
        CsvReader csvReader = CsvReader.getInstance();

        cuisines = csvReader.getCuisineRows().stream()
                .map(cuisineRow -> {
                    List<IngredientDto> ingredientDtoList = csvReader.findByCuisineId(cuisineRow.cuisineId()).stream()
                            .map(recipeRow -> {
                                IngredientRow ingredientRow = csvReader.findByIngredientId(recipeRow.ingredientId());
                                return new IngredientDto(ingredientRow.ingredientId(), ingredientRow.name(), recipeRow.weight(), ingredientRow.unit());
                            }).toList();
                    return new Cuisine(cuisineRow.cuisineId(), cuisineRow.chefName(), cuisineRow.cuisineName(),
                            ingredientDtoList);
                }).toList();

        ingredients = csvReader.getIngredientRows();
    }

    @Override
    public Cuisine findCuisineByName(String name) {
        return cuisines.stream()
                .filter(item -> Objects.equals(item.cuisineName(), name))
                .findFirst()
                .orElseThrow(CuisineNotFoundException::new);
    }

    @Override
    public IngredientRow findIngredientByName(String name) {
        return ingredients.stream()
                .filter(item -> Objects.equals(item.name(), name))
                .findFirst()
                .orElseThrow(IngredientNotFoundException::new);
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
