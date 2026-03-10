package com.caballito.juegodcaballos.service;

import com.caballito.juegodcaballos.persistence.entity.PointPurchase;
import com.caballito.juegodcaballos.persistence.entity.UserAccount;
import com.caballito.juegodcaballos.persistence.repository.PointPurchaseRepository;
import com.caballito.juegodcaballos.persistence.repository.UserAccountRepository;
import com.caballito.juegodcaballos.service.dto.PointPurchaseOutcome;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PointStoreService {

    private final UserAccountRepository userAccountRepository;
    private final PointPurchaseRepository pointPurchaseRepository;
    private final int packageSize;
    private final long packagePriceCop;

    public PointStoreService(
            UserAccountRepository userAccountRepository,
            PointPurchaseRepository pointPurchaseRepository,
            @Value("${app.points-package-size:1000}") int packageSize,
            @Value("${app.points-package-price-cop:10000}") long packagePriceCop
    ) {
        this.userAccountRepository = userAccountRepository;
        this.pointPurchaseRepository = pointPurchaseRepository;
        this.packageSize = packageSize;
        this.packagePriceCop = packagePriceCop;
    }

    @Transactional
    public PointPurchaseOutcome buyPackages(Long userId, int packagesCount) {
        if (packagesCount <= 0) {
            throw new BusinessException("Debes comprar minimo 1 paquete.");
        }

        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado."));

        long pointsAdded = (long) packagesCount * packageSize;
        long amountCop = (long) packagesCount * packagePriceCop;
        user.setPoints(user.getPoints() + pointsAdded);

        PointPurchase purchase = new PointPurchase();
        purchase.setUser(user);
        purchase.setPackagesCount(packagesCount);
        purchase.setPointsAdded(pointsAdded);
        purchase.setAmountCop(amountCop);

        userAccountRepository.save(user);
        pointPurchaseRepository.save(purchase);

        return new PointPurchaseOutcome(packagesCount, pointsAdded, amountCop, user.getPoints());
    }

    @Transactional(readOnly = true)
    public List<PointPurchase> recentPurchases(Long userId, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 50));
        return pointPurchaseRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, safeLimit));
    }

    public int getPackageSize() {
        return packageSize;
    }

    public long getPackagePriceCop() {
        return packagePriceCop;
    }
}
