package sample;

public enum UnitsEnum {
    METRE("м"),
    KILOGRAM("кг"),
    LITRE("л"),
    CENTIMETRE("см"),
    PIECE("шт");

    private final String value;

    private UnitsEnum(String name) {
        this.value = name;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
