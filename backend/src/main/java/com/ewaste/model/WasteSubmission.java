package com.ewaste.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waste_submissions")
public class WasteSubmission {

    @Id
    @Column(name = "waste_id", length = 50)
    private String wasteId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false)
    private String condition;

    @Column
    private String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "center_id", nullable = false)
    private CollectionCenter center;

    @Column(nullable = false)
    private String status = "Pending";

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "qr_path")
    private String qrPath;

    public WasteSubmission() {}

    public WasteSubmission(String wasteId, User user, String deviceName, Integer quantity, String condition, String photo, CollectionCenter center, String status, LocalDateTime date, String qrPath) {
        this.wasteId = wasteId;
        this.user = user;
        this.deviceName = deviceName;
        this.quantity = quantity;
        this.condition = condition;
        this.photo = photo;
        this.center = center;
        this.status = status;
        this.date = date;
        this.qrPath = qrPath;
    }

    public String getWasteId() {
        return wasteId;
    }

    public void setWasteId(String wasteId) {
        this.wasteId = wasteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public CollectionCenter getCenter() {
        return center;
    }

    public void setCenter(CollectionCenter center) {
        this.center = center;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getQrPath() {
        return qrPath;
    }

    public void setQrPath(String qrPath) {
        this.qrPath = qrPath;
    }
}
