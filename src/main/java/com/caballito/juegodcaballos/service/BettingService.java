package com.caballito.juegodcaballos.service;

import com.caballito.juegodcaballos.model.RaceConfig;
import com.caballito.juegodcaballos.model.RaceState;
import com.caballito.juegodcaballos.model.Suit;
import com.caballito.juegodcaballos.persistence.entity.BetRecord;
import com.caballito.juegodcaballos.persistence.entity.UserAccount;
import com.caballito.juegodcaballos.persistence.repository.BetRecordRepository;
import com.caballito.juegodcaballos.persistence.repository.UserAccountRepository;
import com.caballito.juegodcaballos.service.dto.BetOutcome;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BettingService {

    private final RaceService raceService;
    private final UserAccountRepository userAccountRepository;
    private final BetRecordRepository betRecordRepository;

    public BettingService(
            RaceService raceService,
            UserAccountRepository userAccountRepository,
            BetRecordRepository betRecordRepository
    ) {
        this.raceService = raceService;
        this.userAccountRepository = userAccountRepository;
        this.betRecordRepository = betRecordRepository;
    }

    @Transactional
    public BetOutcome play(Long userId, Suit chosenSuit, long betPoints, int trackLength, boolean falteringEnabled) {
        if (chosenSuit == null) {
            throw new BusinessException("Debes escoger un caballo para apostar.");
        }
        if (betPoints <= 0) {
            throw new BusinessException("La apuesta debe ser mayor a 0 puntos.");
        }

        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado."));

        if (betPoints > user.getPoints()) {
            throw new BusinessException("No tienes puntos suficientes para esta apuesta.");
        }

        RaceConfig raceConfig;
        try {
            raceConfig = new RaceConfig(trackLength, falteringEnabled);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ex.getMessage());
        }

        user.setPoints(user.getPoints() - betPoints);

        RaceState race = raceService.newRace(raceConfig);
        List<String> log = new ArrayList<>();
        int guard = 500;
        while (race.getWinner() == null && guard-- > 0) {
            log.add(raceService.step(race));
        }

        if (race.getWinner() == null) {
            throw new BusinessException("No fue posible terminar la carrera.");
        }

        boolean won = race.getWinner() == chosenSuit;
        long payout = 0L;
        if (won) {
            payout = betPoints * 5;
            user.setPoints(user.getPoints() + payout);
        }

        BetRecord record = new BetRecord();
        record.setUser(user);
        record.setChosenSuit(chosenSuit);
        record.setWinnerSuit(race.getWinner());
        record.setTrackLength(trackLength);
        record.setFalteringEnabled(falteringEnabled);
        record.setBetPoints(betPoints);
        record.setPayoutPoints(payout);
        record.setWon(won);

        userAccountRepository.save(user);
        betRecordRepository.save(record);

        return new BetOutcome(
                won,
                chosenSuit,
                race.getWinner(),
                betPoints,
                trackLength,
                payout,
                user.getPoints(),
                List.copyOf(log)
        );
    }

    @Transactional(readOnly = true)
    public List<BetRecord> recentBets(Long userId, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 50));
        return betRecordRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, safeLimit));
    }
}
