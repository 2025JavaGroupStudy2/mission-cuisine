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
    public String search(String input){
        char HANGUL_BASE = 0xAC00;
        int JUNGSEONG_COUNT = 21;
        int JONGSEONG_COUNT = 28;

        int highestMatchingCount = 0;
        List<String> candidates = new ArrayList<>();
        List<Integer> wrongAreas = new ArrayList<>();
        input = input.replace(" ", "");
        for(IngredientRow ingredient : ingredients){
            String ingredientName = ingredient.name();
            int wrongCount = 0;
            int matchingCount = 0;

            for(int i=0; i<input.length()-1; i++){
                char checkAlphabet = input.charAt(i);
                if(i<ingredientName.length()){
                    char correctAlphabet = ingredientName.charAt(i);
                    if(correctAlphabet==checkAlphabet){
                        matchingCount+=2;
                    }else{
                        int checkCode = checkAlphabet - HANGUL_BASE;
                        int checkChoseong = checkCode / (JUNGSEONG_COUNT * JONGSEONG_COUNT);
                        int correctCode = correctAlphabet - HANGUL_BASE;
                        int correctChoseong = correctCode / (JUNGSEONG_COUNT * JONGSEONG_COUNT);
                        wrongCount+=2;
                        if(checkChoseong==correctChoseong){
                            matchingCount++;
                            wrongCount--;
                        }
                    }
                }
            }

            int isCorrectNameMoreLong = ingredientName.length() - input.length();
            wrongCount += (Math.max(isCorrectNameMoreLong, 0));

            if(matchingCount>=ingredientName.length()&&matchingCount>=highestMatchingCount){
                if(matchingCount>highestMatchingCount){
                    highestMatchingCount = matchingCount;
                    candidates.clear();
                    wrongAreas.clear();
                }
                wrongAreas.add(wrongCount);
                candidates.add(ingredientName);
            }
        }
        if(candidates.isEmpty()) {
            return input;
        }else{
            int minIndex = IntStream.range(0, wrongAreas.size())
                    .boxed()
                    .min(Comparator.comparingInt(wrongAreas::get))
                    .orElse(-1);
            System.out.println(candidates.get(minIndex));
            return candidates.get(minIndex);
        }
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
