package com.caballito.juegodcaballos.model;

public class Horse {
    private final Suit suit;
    private int position = 0;

    public Horse(Suit suit) {
        this.suit = suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getPosition() {
        return position;
    }

    public void moveForward() {
        position++;
    }

    public void moveBack() {
        position = Math.max(0, position - 1);
    }

    public void reset() {
        position = 0;
    }
}
