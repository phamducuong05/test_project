package model.core.enums;

public enum Suit {
    HEARTS("♥"), DIAMONDS("♦"), CLUBS("♣"), SPADES("♠");

    private final String value;

    Suit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
