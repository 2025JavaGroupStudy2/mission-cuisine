package mission;

import java.util.List;
import java.util.Objects;
import mission.adapter.in.CsvDatabase;
import mission.application.domain.model.Cuisine;
import mission.application.port.in.CuisinePersistence;
import org.junit.jupiter.api.Test;

public class AdapterTest {
    @Test
    public void test() {
        CuisinePersistence pv = new CsvDatabase();
        List<Cuisine> result = pv.filter(List.of("스파게티면", "마늘", "올리브오일", "소금", "칠리 플레이크"));
        assert result.size() == 1;
        assert Objects.equals(result.getFirst().cuisineName(), "알리오 올리오");
    }
}
