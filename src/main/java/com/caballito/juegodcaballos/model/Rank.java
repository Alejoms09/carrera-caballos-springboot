package com.caballito.juegodcaballos.model;

public enum Rank {
    A("A"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    J("J"),
    Q("Q"),
    K("K");

    private final String label;

    Rank(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
