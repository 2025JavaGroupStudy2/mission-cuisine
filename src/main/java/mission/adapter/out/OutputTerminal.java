package mission.adapter.out;

import java.util.List;
import mission.application.port.out.Output;

public class OutputTerminal implements Output {

    @Override
    public void correctIngredients(List<String> ingredients, List<String> correctedIngredients){
        for(int i=0; i<ingredients.size(); i++){
            String text = String.format("(%s -> %s)", ingredients.get(i), correctedIngredients.get(i));
            System.out.println(text);
        }


    }

    @Override
    public void availableCuisine(List<String> availableCuisines) {
        System.out.println("[만들 수 있는 요리]");
        availableCuisines.forEach(cuisine -> System.out.println(" - " + cuisine));
    }

    @Override
    public void recommendCuisine(List<String> recommendCuisines) {
        System.out.println("[추천 요리]");
        recommendCuisines.forEach(cuisine -> System.out.println(" - " + cuisine));
    }
}
