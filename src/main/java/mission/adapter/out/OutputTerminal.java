package mission.adapter.out;

import java.util.List;
import mission.application.port.out.Output;

public class OutputTerminal implements Output {
    public void availableCuisine(List<String> availableCuisines) {
        System.out.println("[만들 수 있는 요리]");
        availableCuisines.forEach(cuisine -> System.out.println(" - " + cuisine));
    }
}
