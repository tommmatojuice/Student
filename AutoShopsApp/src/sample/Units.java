package sample;

public enum Units {
    METRE("м"),
    KILOGRAM("кг"),
    LITRE("л"),
    CENTIMETRE("см"),
    PIECE("шт");

    private final String value;

    private Units(String name) {
        this.value = name;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
