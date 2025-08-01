package mission;

import api.Console;
import java.util.List;
import mission.application.service.CuisineService;

public class Application {
    public static void main(String[] args) {
        //TODO: 미션 구현
        AppConfig config = new AppConfig();
        CuisineService cuisineService = config.getCuisineService();
        List<String> response = cuisineService.getInput();
        cuisineService.showAvailableCuisine(response);
    }
}