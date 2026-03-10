package com.caballito.juegodcaballos;

import com.caballito.juegodcaballos.persistence.entity.UserAccount;
import com.caballito.juegodcaballos.persistence.repository.PointPurchaseRepository;
import com.caballito.juegodcaballos.persistence.repository.UserAccountRepository;
import com.caballito.juegodcaballos.service.PointStoreService;
import com.caballito.juegodcaballos.service.dto.PointPurchaseOutcome;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PointStoreServiceTest {

    @Test
    void buyPackagesAddsPointsAndCalculatesAmount() {
        UserAccountRepository userRepository = Mockito.mock(UserAccountRepository.class);
        PointPurchaseRepository purchaseRepository = Mockito.mock(PointPurchaseRepository.class);
        PointStoreService pointStoreService = new PointStoreService(userRepository, purchaseRepository, 1000, 10000);

        UserAccount user = new UserAccount();
        user.setId(5L);
        user.setPoints(1200);

        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(purchaseRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PointPurchaseOutcome outcome = pointStoreService.buyPackages(5L, 3);

        assertEquals(3000, outcome.pointsAdded());
        assertEquals(30000, outcome.amountCop());
        assertEquals(4200, outcome.currentPoints());
    }
}
