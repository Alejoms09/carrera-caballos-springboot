package com.caballito.juegodcaballos.model;

public record Card(Rank rank, Suit suit) {
    @Override
    public String toString() {
        return rank.label() + suit.symbol();
    }
}
