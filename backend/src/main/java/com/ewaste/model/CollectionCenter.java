package com.ewaste.model;

import jakarta.persistence.*;

@Entity
@Table(name = "collection_centers")
public class CollectionCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id")
    private Integer centerId;

    @Column(name = "center_name", nullable = false)
    private String centerName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String contact;

    public CollectionCenter() {}

    public CollectionCenter(Integer centerId, String centerName, String location, String contact) {
        this.centerId = centerId;
        this.centerName = centerName;
        this.location = location;
        this.contact = contact;
    }

    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
