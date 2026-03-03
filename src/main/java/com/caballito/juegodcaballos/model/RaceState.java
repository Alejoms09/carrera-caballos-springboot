package com.caballito.juegodcaballos.model;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RaceState {
    private final RaceConfig config;
    private final Deck deck;
    private final Map<Suit, Horse> horses;
    private final List<Card> trackEvents;
    private final boolean[] eventRevealed;

    private Card lastDrawn;
    private Suit winner;

    public RaceState(RaceConfig config, Deck deck, List<Card> trackEvents) {
        this.config = config;
        this.deck = deck;
        this.trackEvents = trackEvents;
        this.eventRevealed = new boolean[config.trackLength()];
        this.horses = new EnumMap<>(Suit.class);
        for (Suit suit : Suit.values()) {
            horses.put(suit, new Horse(suit));
        }
    }

    public RaceConfig getConfig() {
        return config;
    }

    public Deck getDeck() {
        return deck;
    }

    public Map<Suit, Horse> getHorses() {
        return horses;
    }

    public List<Card> getTrackEvents() {
        return trackEvents;
    }

    public boolean[] getEventRevealed() {
        return eventRevealed;
    }

    public Card getLastDrawn() {
        return lastDrawn;
    }

    public void setLastDrawn(Card lastDrawn) {
        this.lastDrawn = lastDrawn;
    }

    public Suit getWinner() {
        return winner;
    }

    public void setWinner(Suit winner) {
        this.winner = winner;
    }
}
