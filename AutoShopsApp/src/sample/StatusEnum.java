package sample;

public enum  StatusEnum {
    //формируется, подписан, заключен, отменен, расторгнут
    //formed, signed, concluded, canceled, terminated
    FORMED("Формируется"),
    SIGNED("Подписан"),
    CONCLUDED("Заключен"),
    CANCELED("Отменен"),
    TERMINATED("Расторгнут");

    private final String value;

    private StatusEnum(String name) {
        this.value = name;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
