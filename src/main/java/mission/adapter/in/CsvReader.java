package mission.adapter.in;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mission.application.domain.enums.Unit;
import mission.application.domain.exception.CorruptedFileException;
import mission.application.domain.exception.IngredientNotFoundException;
import mission.application.domain.model.dto.CuisineRow;
import mission.application.domain.model.dto.IngredientRow;
import mission.application.domain.model.dto.RecipeRow;

public class CsvReader {
    private static final String RecipeFileName = "Recipe.csv";
    private static final String IngredientFileName = "Ingredient.csv";
    private static final String CuisineFileName = "Cuisine.csv";

    private final List<IngredientRow> ingredientRows;
    private final List<RecipeRow> recipeRows;
    private final List<CuisineRow> cuisineRows;

    private static CsvReader csvReader;

    private CsvReader(){
        ingredientRows = readFile(IngredientFileName).stream()
                .map(row -> row.split(","))
                .map(row -> new IngredientRow(Integer.parseInt(row[0]), row[1], Unit.valueOf(row[2])))
                .toList();
        recipeRows = readFile(RecipeFileName).stream()
                .map(row -> row.split(","))
                .map(row -> new RecipeRow(Integer.parseInt(row[0]), Integer.parseInt(row[1]), Integer.parseInt(row[2])))
                .toList();
        cuisineRows = readFile(CuisineFileName).stream()
                .map(row -> row.split(","))
                .map(row -> new CuisineRow(Integer.parseInt(row[0]), row[1], row[2]))
                .toList();
    }

    public static CsvReader getInstance(){
        if(csvReader==null){
            csvReader = new CsvReader();
        }
        return csvReader;
    }

    public List<CuisineRow> getCuisineRows(){
        return cuisineRows;
    }

    public IngredientRow findByIngredientName(String name) {
        return ingredientRows.stream()
                .filter(ingredient -> Objects.equals(ingredient.name(), name))
                .findFirst()
                .orElseThrow(IngredientNotFoundException::new);
    }

    public IngredientRow findByIngredientId(int ingredientId){
        return ingredientRows.stream()
                .filter(ingredient -> Objects.equals(ingredient.ingredientId(), ingredientId))
                .findFirst()
                .orElseThrow(IngredientNotFoundException::new);
    }

    public List<RecipeRow> findByCuisineId(int cuisineId){
        return recipeRows.stream()
                .filter(recipe -> Objects.equals(recipe.cuisineId(), cuisineId))
                .toList();
    }

    private static List<String> readFile(String fileName){
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = CsvReader.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            throw new CorruptedFileException();
        }
        return lines;
    }
}
