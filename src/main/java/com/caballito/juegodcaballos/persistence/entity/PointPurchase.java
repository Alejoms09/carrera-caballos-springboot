package com.caballito.juegodcaballos.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_purchase")
public class PointPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_purchase_user"))
    private UserAccount user;

    @Column(name = "packages_count", nullable = false)
    private int packagesCount;

    @Column(name = "points_added", nullable = false)
    private long pointsAdded;

    @Column(name = "amount_cop", nullable = false)
    private long amountCop;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public int getPackagesCount() {
        return packagesCount;
    }

    public void setPackagesCount(int packagesCount) {
        this.packagesCount = packagesCount;
    }

    public long getPointsAdded() {
        return pointsAdded;
    }

    public void setPointsAdded(long pointsAdded) {
        this.pointsAdded = pointsAdded;
    }

    public long getAmountCop() {
        return amountCop;
    }

    public void setAmountCop(long amountCop) {
        this.amountCop = amountCop;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
