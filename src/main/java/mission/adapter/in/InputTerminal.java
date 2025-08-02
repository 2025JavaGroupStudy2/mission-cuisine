package mission.adapter.in;

import api.Console;
import java.util.List;
import java.util.stream.Stream;
import mission.application.port.in.Input;
import mission.utility.Validator;

public class InputTerminal implements Input {
    public List<String> getIngredients() {
        System.out.println("재료를 입력해주세요.");
        List<String> inputs = Stream.of(Console.readLine().split(","))
                .map(String::trim)
                .toList();
        Validator.duplicateCheck(inputs);
        return inputs;
    }
}
