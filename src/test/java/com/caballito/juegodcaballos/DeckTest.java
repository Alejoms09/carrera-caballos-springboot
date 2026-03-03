package com.caballito.juegodcaballos;

import com.caballito.juegodcaballos.model.Deck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckTest {
    @Test
    void deckStartsWith52() {
        Deck deck = new Deck();
        assertEquals(52, deck.size());
    }
}
