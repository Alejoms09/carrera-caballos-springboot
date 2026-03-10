package com.caballito.juegodcaballos.service.dto;

import com.caballito.juegodcaballos.model.Suit;

import java.util.List;

public record BetOutcome(
        boolean won,
        Suit chosenSuit,
        Suit winnerSuit,
        long betPoints,
        int trackLength,
        long payoutPoints,
        long currentPoints,
        List<String> raceLog
) {
}
