package com.caballito.juegodcaballos.model;

public enum Suit {
    OROS("♦"),
    COPAS("♥"),
    BASTOS("♣"),
    ESPADAS("♠");

    private final String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }
}
