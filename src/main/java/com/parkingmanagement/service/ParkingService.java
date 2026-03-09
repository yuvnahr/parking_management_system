package com.parkingmanagement.service;

import com.parkingmanagement.entity.Parking;
import com.parkingmanagement.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Parking Management
 * Handles business logic and validation
 */
@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    /**
     * Get all active parking entries
     * @return list of all parking entries
     */
    public List<Parking> getAllParkingEntries() {
        return parkingRepository.findAll();
    }

    /**
     * Get parking entry by ID
     * @param id the parking entry ID
     * @return Optional containing the parking entry if found
     */
    public Optional<Parking> getParkingById(Long id) {
        return parkingRepository.findById(id);
    }

    /**
     * Get parking entry by slot number
     * @param slotNumber the parking slot number
     * @return Optional containing the parking entry if found
     */
    public Optional<Parking> getParkingBySlotNumber(Integer slotNumber) {
        return parkingRepository.findBySlotNumber(slotNumber);
    }

    /**
     * Get parking entry by vehicle number
     * @param vehicleNumber the vehicle number
     * @return Optional containing the parking entry if found
     */
    public Optional<Parking> getParkingByVehicleNumber(String vehicleNumber) {
        return parkingRepository.findByVehicleNumber(vehicleNumber);
    }

    /**
     * Save a new parking entry with validation
     * Validates that:
     * - Vehicle number is not already parked
     * - Slot number is not already occupied
     * - Entry time is auto-generated
     *
     * @param parking the parking entry to save
     * @return the saved parking entry
     * @throws IllegalArgumentException if validation fails
     */
    public Parking saveParkingEntry(Parking parking) {
        // Validate: Check if vehicle number already exists
        if (parkingRepository.existsByVehicleNumber(parking.getVehicleNumber())) {
            throw new IllegalArgumentException("Vehicle with number '" + parking.getVehicleNumber() 
                    + "' is already parked. Same vehicle cannot enter twice.");
        }

        // Validate: Check if slot number is already occupied
        if (parkingRepository.existsBySlotNumber(parking.getSlotNumber())) {
            throw new IllegalArgumentException("Parking slot '" + parking.getSlotNumber() 
                    + "' is already occupied. Same slot cannot be assigned twice.");
        }

        // Validate: Slot number should be positive
        if (parking.getSlotNumber() <= 0) {
            throw new IllegalArgumentException("Parking slot number must be a positive number.");
        }

        // Validate: Owner name should not be empty
        if (parking.getOwnerName() == null || parking.getOwnerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty.");
        }

        // Validate: Vehicle number should not be empty
        if (parking.getVehicleNumber() == null || parking.getVehicleNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle number cannot be empty.");
        }

        // Validate: Vehicle type should not be empty
        if (parking.getVehicleType() == null || parking.getVehicleType().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle type cannot be empty.");
        }

        // Auto-generate entry time if not provided
        if (parking.getEntryTime() == null) {
            parking.setEntryTime(LocalDateTime.now());
        }

        return parkingRepository.save(parking);
    }

    /**
     * Delete a parking entry by ID
     * @param id the parking entry ID
     */
    public void deleteParkingEntry(Long id) {
        parkingRepository.deleteById(id);
    }

    /**
     * Count total active parking entries
     * @return total number of parking entries
     */
    public long getTotalParkingCount() {
        return parkingRepository.count();
    }

}
