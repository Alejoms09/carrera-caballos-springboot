package com.caballito.juegodcaballos.persistence.entity;

import com.caballito.juegodcaballos.model.Suit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "bet_record")
public class BetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bet_user"))
    private UserAccount user;

    @Enumerated(EnumType.STRING)
    @Column(name = "chosen_suit", nullable = false, length = 20)
    private Suit chosenSuit;

    @Enumerated(EnumType.STRING)
    @Column(name = "winner_suit", nullable = false, length = 20)
    private Suit winnerSuit;

    @Column(name = "track_length", nullable = false)
    private int trackLength;

    @Column(name = "faltering_enabled", nullable = false)
    private boolean falteringEnabled;

    @Column(name = "bet_points", nullable = false)
    private long betPoints;

    @Column(name = "payout_points", nullable = false)
    private long payoutPoints;

    @Column(nullable = false)
    private boolean won;

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

    public Suit getChosenSuit() {
        return chosenSuit;
    }

    public void setChosenSuit(Suit chosenSuit) {
        this.chosenSuit = chosenSuit;
    }

    public Suit getWinnerSuit() {
        return winnerSuit;
    }

    public void setWinnerSuit(Suit winnerSuit) {
        this.winnerSuit = winnerSuit;
    }

    public int getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(int trackLength) {
        this.trackLength = trackLength;
    }

    public boolean isFalteringEnabled() {
        return falteringEnabled;
    }

    public void setFalteringEnabled(boolean falteringEnabled) {
        this.falteringEnabled = falteringEnabled;
    }

    public long getBetPoints() {
        return betPoints;
    }

    public void setBetPoints(long betPoints) {
        this.betPoints = betPoints;
    }

    public long getPayoutPoints() {
        return payoutPoints;
    }

    public void setPayoutPoints(long payoutPoints) {
        this.payoutPoints = payoutPoints;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
