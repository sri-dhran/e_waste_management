package com.ewaste.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "transportation_logs")
public class TransportationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "waste_id", nullable = false)
    private WasteSubmission wasteSubmission;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String location;

    public TransportationLog() {}

    public TransportationLog(Integer logId, WasteSubmission wasteSubmission, String status, LocalDate date, LocalTime time, String location) {
        this.logId = logId;
        this.wasteSubmission = wasteSubmission;
        this.status = status;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public WasteSubmission getWasteSubmission() {
        return wasteSubmission;
    }

    public void setWasteSubmission(WasteSubmission wasteSubmission) {
        this.wasteSubmission = wasteSubmission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
