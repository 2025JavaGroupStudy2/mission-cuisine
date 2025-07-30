package mission.adapter.in;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mission.application.domain.exception.CorruptedFileException;
import mission.application.domain.model.Cuisine;
import mission.application.port.in.CuisinePersistence;

public class CuisineCsvReader implements CuisinePersistence {
    private static final String CuisineFileName = "Cuisine.csv";
    private static final String RecipeFileName = "Recipe.csv";
    private final List<Cuisine> cuisines;

    private record recipe(int cuisine_id, int ingredientId, int weight) {
    }

    CuisineCsvReader() {
        public IngredientCsvReader() {
            List<String> lines = new ArrayList<>();
            try (InputStream inputStream = IngredientCsvReader.class.getClassLoader().getResourceAsStream(
                    CuisineFileName);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                cuisines = lines.stream()
                        .map(row -> row.split(","))
                        .map(row -> new Cuisine(Integer.parseInt(row[0]), row[1], row[2]))
                        .toList();
            } catch (Exception e) {
                throw new CorruptedFileException();
            }
        }

        private getRecipe() {

        }
    }
}
