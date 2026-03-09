package com.parkingmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Parking Entity representing a parking slot entry
 */
@Entity
@Table(name = "parking",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "vehicleNumber", name = "unique_vehicle_number"),
                @UniqueConstraint(columnNames = "slotNumber", name = "unique_slot_number")
        })
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String vehicleType;

    @Column(nullable = false, unique = true)
    private Integer slotNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime entryTime;

    /**
     * Default Constructor
     */
    public Parking() {
    }

    /**
     * Constructor with all parameters
     */
    public Parking(String vehicleNumber, String ownerName, String vehicleType, Integer slotNumber) {
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;
        this.slotNumber = slotNumber;
    }

    /**
     * Constructor with all parameters including entryTime
     */
    public Parking(String vehicleNumber, String ownerName, String vehicleType, Integer slotNumber, LocalDateTime entryTime) {
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;
        this.slotNumber = slotNumber;
        this.entryTime = entryTime;
    }

    // ========== Getters and Setters ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    /**
     * Auto-generates entry time before entity is persisted
     */
    @PrePersist
    protected void onCreate() {
        if (entryTime == null) {
            entryTime = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "Parking{" +
                "id=" + id +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", slotNumber=" + slotNumber +
                ", entryTime=" + entryTime +
                '}';
    }
}
