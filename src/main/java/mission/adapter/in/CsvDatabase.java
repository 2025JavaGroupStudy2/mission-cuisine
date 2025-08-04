package mission.adapter.in;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    @Override
    public String search(String input) {
        final char HANGUL_BASE = 0xAC00;
        final int JUNGSEONG_COUNT = 21;
        final int JONGSEONG_COUNT = 28;

        input = input.replace(" ", "");

        int highestMatchingScore = 0;
        List<String> candidates = new ArrayList<>();
        List<Integer> wrongScores = new ArrayList<>();

        for (IngredientRow ingredient : ingredients) {
            String name = ingredient.name();
            int matchingScore = 0;
            int wrongScore = 0;

            for (int i = 0; i < input.length() - 1 && i < name.length(); i++) {
                char inputChar = input.charAt(i);
                char nameChar = name.charAt(i);

                if (inputChar == nameChar) {
                    matchingScore += 2;
                } else {
                    matchingScore += compareChoseong(inputChar, nameChar, HANGUL_BASE, JUNGSEONG_COUNT, JONGSEONG_COUNT);
                    wrongScore += 2;
                }
            }

            wrongScore += Math.max(name.length() - input.length(), 0);

            if (matchingScore >= name.length() && matchingScore >= highestMatchingScore) {
                if (matchingScore > highestMatchingScore) {
                    highestMatchingScore = matchingScore;
                    candidates.clear();
                    wrongScores.clear();
                }
                candidates.add(name);
                wrongScores.add(wrongScore);
            }
        }

        if (candidates.isEmpty()) {
            return input;
        }

        int bestIndex = IntStream.range(0, wrongScores.size())
                .boxed()
                .min(Comparator.comparingInt(wrongScores::get))
                .orElse(0);

        return candidates.get(bestIndex);
    }

    private int compareChoseong(char inputChar, char targetChar, char base, int jung, int jong) {
        int inputCode = inputChar - base;
        int targetCode = targetChar - base;

        int inputChoseong = inputCode / (jung * jong);
        int targetChoseong = targetCode / (jung * jong);

        return (inputChoseong == targetChoseong) ? 1 : 0;
    }


    //개수를 가지고 비중이 50퍼 안되는건 거르기
    //전체 그람수랑 있는재료 그람수 비중 구하기
    //비중으로 노출된 가중치 정렬
    public List<Cuisine> sortPerRate(List<String> inputIngredients){
        return cuisines.stream()
                .filter(cuisine -> cuisine.ingredients().stream()
                        .map(IngredientDto::name)
                        .anyMatch(inputIngredients::contains)
                ).sorted(Comparator.comparingDouble(
                        (Cuisine cuisine)->getWeight(inputIngredients, cuisine.ingredients())
                ).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private double getWeight(List<String> inputIngredients, List<IngredientDto> ingredients){
        int allAmount = ingredients.stream()
                .mapToInt(IngredientDto::amount)
                .sum();

        return ingredients.stream()
                .filter(ingredient -> inputIngredients.contains(ingredient.name()))
                .mapToDouble(item-> (double) item.amount() / allAmount)
                .sum();
    }

}
