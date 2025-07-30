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
import mission.application.domain.model.dto.IngredientRow;
import mission.application.port.in.IngredientPersistence;

public class IngredientCsvReader implements IngredientPersistence {
    private static final String fileName = "Ingredient.csv";
    private final List<IngredientRow> ingredients;

    public IngredientCsvReader() {
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = IngredientCsvReader.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            ingredients = lines.stream()
                    .map(row -> row.split(","))
                    .map(row -> new IngredientRow(Integer.parseInt(row[0]), row[1], Unit.valueOf(row[2])))
                    .toList();
        } catch (Exception e) {
            throw new CorruptedFileException();
        }
    }

    public IngredientRow findByName(String name) {
        return ingredients.stream()
                .filter(ingredient -> Objects.equals(ingredient.name(), name))
                .findFirst()
                .orElseThrow(IngredientNotFoundException::new);
    }
}