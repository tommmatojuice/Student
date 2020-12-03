package sample;

public enum Types {
    OIL("Масло"),
    FILTER("Фильтр"),
    PAINT("Краска"),
    BELT ("Ремень"),
    VALVE("Клапан");

    private final String value;

    private Types(String name) {
        this.value = name;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
