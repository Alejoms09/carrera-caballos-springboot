package com.caballito.juegodcaballos;

import com.caballito.juegodcaballos.model.RaceConfig;
import com.caballito.juegodcaballos.model.RaceState;
import com.caballito.juegodcaballos.service.RaceService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RaceServiceTest {

    @Test
    void raceEventuallyHasWinner() {
        RaceService service = new RaceService();
        RaceState state = service.newRace(new RaceConfig(7, true));

        int guard = 500;
        while (state.getWinner() == null && guard-- > 0) {
            service.step(state);
        }
        assertNotNull(state.getWinner());
    }

    @Test
    void noNegativePositions() {
        RaceService service = new RaceService();
        RaceState state = service.newRace(new RaceConfig(7, true));

        for (int i = 0; i < 200; i++) {
            service.step(state);
            state.getHorses().forEach((suit, horse) -> assertTrue(horse.getPosition() >= 0));
            if (state.getWinner() != null) {
                break;
            }
        }
    }
}
