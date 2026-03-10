package com.caballito.juegodcaballos.service.dto;

public record PointPurchaseOutcome(
        int packagesCount,
        long pointsAdded,
        long amountCop,
        long currentPoints
) {
}
