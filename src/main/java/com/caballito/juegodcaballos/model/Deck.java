package com.caballito.juegodcaballos.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final SecureRandom rnd = new SecureRandom();

    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards, rnd);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No hay cartas en el mazo.");
        }
        return cards.remove(cards.size() - 1);
    }
}
