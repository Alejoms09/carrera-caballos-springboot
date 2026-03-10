package com.caballito.juegodcaballos;

import com.caballito.juegodcaballos.model.Deck;
import com.caballito.juegodcaballos.model.RaceConfig;
import com.caballito.juegodcaballos.model.RaceState;
import com.caballito.juegodcaballos.model.Suit;
import com.caballito.juegodcaballos.persistence.entity.UserAccount;
import com.caballito.juegodcaballos.persistence.repository.BetRecordRepository;
import com.caballito.juegodcaballos.persistence.repository.UserAccountRepository;
import com.caballito.juegodcaballos.service.BettingService;
import com.caballito.juegodcaballos.service.RaceService;
import com.caballito.juegodcaballos.service.dto.BetOutcome;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BettingServiceTest {

    @Test
    void winningBetPaysFiveTimes() {
        RaceService raceService = Mockito.mock(RaceService.class);
        UserAccountRepository userRepository = Mockito.mock(UserAccountRepository.class);
        BetRecordRepository betRepository = Mockito.mock(BetRecordRepository.class);
        BettingService bettingService = new BettingService(raceService, userRepository, betRepository);

        UserAccount user = new UserAccount();
        user.setId(1L);
        user.setPoints(1000);

        RaceState raceState = new RaceState(new RaceConfig(7, true), new Deck(), new ArrayList<>());
        raceState.setWinner(Suit.OROS);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(betRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(raceService.newRace(any(RaceConfig.class))).thenReturn(raceState);

        BetOutcome outcome = bettingService.play(1L, Suit.OROS, 100, 7, true);

        assertTrue(outcome.won());
        assertEquals(500, outcome.payoutPoints());
        assertEquals(1400, outcome.currentPoints());
    }

    @Test
    void losingBetDiscountsPoints() {
        RaceService raceService = Mockito.mock(RaceService.class);
        UserAccountRepository userRepository = Mockito.mock(UserAccountRepository.class);
        BetRecordRepository betRepository = Mockito.mock(BetRecordRepository.class);
        BettingService bettingService = new BettingService(raceService, userRepository, betRepository);

        UserAccount user = new UserAccount();
        user.setId(1L);
        user.setPoints(1000);

        RaceState raceState = new RaceState(new RaceConfig(7, true), new Deck(), new ArrayList<>());
        raceState.setWinner(Suit.COPAS);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(betRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(raceService.newRace(any(RaceConfig.class))).thenReturn(raceState);

        BetOutcome outcome = bettingService.play(1L, Suit.OROS, 100, 7, true);

        assertFalse(outcome.won());
        assertEquals(0, outcome.payoutPoints());
        assertEquals(900, outcome.currentPoints());
    }
}
