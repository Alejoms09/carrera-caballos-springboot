package com.caballito.juegodcaballos.service;

import com.caballito.juegodcaballos.model.Card;
import com.caballito.juegodcaballos.model.Deck;
import com.caballito.juegodcaballos.model.Horse;
import com.caballito.juegodcaballos.model.RaceConfig;
import com.caballito.juegodcaballos.model.RaceState;
import com.caballito.juegodcaballos.model.Suit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RaceService {

    public RaceState newRace(RaceConfig config) {
        Deck deck = new Deck();

        List<Card> trackEvents = new ArrayList<>();
        for (int i = 0; i < config.trackLength(); i++) {
            trackEvents.add(deck.draw());
        }

        return new RaceState(config, deck, trackEvents);
    }

    public String step(RaceState state) {
        if (state.getWinner() != null) {
            return "La carrera ya termino.";
        }

        if (state.getDeck().isEmpty()) {
            return "Se acabo el mazo (poco comun).";
        }

        Card drawn = state.getDeck().draw();
        state.setLastDrawn(drawn);

        Horse horse = state.getHorses().get(drawn.suit());
        horse.moveForward();

        StringBuilder log = new StringBuilder("Sale ")
                .append(drawn)
                .append(" -> avanza ")
                .append(drawn.suit());

        if (horse.getPosition() >= state.getConfig().trackLength()) {
            state.setWinner(horse.getSuit());
            log.append(" | GANA ").append(horse.getSuit()).append("!");
            return log.toString();
        }

        if (state.getConfig().falteringEnabled()) {
            int posIndex = horse.getPosition() - 1;
            if (posIndex >= 0
                    && posIndex < state.getConfig().trackLength()
                    && !state.getEventRevealed()[posIndex]) {

                state.getEventRevealed()[posIndex] = true;
                Card event = state.getTrackEvents().get(posIndex);
                Horse penalized = state.getHorses().get(event.suit());
                penalized.moveBack();

                log.append(" | Evento casilla ")
                        .append(posIndex + 1)
                        .append(": ")
                        .append(event)
                        .append(" -> retrocede ")
                        .append(event.suit());
            }
        }

        return log.toString();
    }
}
