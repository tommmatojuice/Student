package sample;

public enum TypesEnum {
    OIL("Масло"),
    FILTER("Фильтр"),
    PAINT("Краска"),
    BELT ("Ремень"),
    VALVE("Клапан");

    private final String value;

    private TypesEnum(String name) {
        this.value = name;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
