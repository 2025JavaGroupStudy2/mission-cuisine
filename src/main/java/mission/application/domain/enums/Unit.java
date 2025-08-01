package mission.application.domain.enums;

public enum Unit {
    G, ML;

    public static Unit parse(String value) {
        if ("g".equals(value)) {
            return G;
        }
        if ("ml".equals(value)) {
            return ML;
        }
        return null;
    }
}
