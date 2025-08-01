package mission;

import mission.adapter.in.CsvDatabase;
import mission.adapter.in.InputTerminal;
import mission.adapter.out.OutputTerminal;
import mission.application.domain.model.Cuisine;
import mission.application.port.in.CuisinePersistence;
import mission.application.port.in.IngredientPersistence;
import mission.application.port.in.Input;
import mission.application.port.out.Output;
import mission.application.service.CuisineService;

public class AppConfig {
    public CuisineService getCuisineService() {
        return new CuisineService(
                getInput(),
                getOutput(),
                getCuisinePersistence(),
                getIngredientPersistence()
        );
    }

    public Input getInput() {
        return new InputTerminal();
    }

    public Output getOutput() {
        return new OutputTerminal();
    }

    public CuisinePersistence getCuisinePersistence() {
        return new CsvDatabase();
    }

    public IngredientPersistence getIngredientPersistence() {
        return new CsvDatabase();
    }
}
