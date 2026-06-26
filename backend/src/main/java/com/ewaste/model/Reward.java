package com.ewaste.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_id")
    private Integer rewardId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "waste_id", nullable = false)
    private WasteSubmission wasteSubmission;

    @Column(nullable = false)
    private Integer points = 100;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    public Reward() {}

    public Reward(Integer rewardId, User user, WasteSubmission wasteSubmission, Integer points, LocalDateTime date) {
        this.rewardId = rewardId;
        this.user = user;
        this.wasteSubmission = wasteSubmission;
        this.points = points;
        this.date = date;
    }

    public Integer getRewardId() {
        return rewardId;
    }

    public void setRewardId(Integer rewardId) {
        this.rewardId = rewardId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WasteSubmission getWasteSubmission() {
        return wasteSubmission;
    }

    public void setWasteSubmission(WasteSubmission wasteSubmission) {
        this.wasteSubmission = wasteSubmission;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
