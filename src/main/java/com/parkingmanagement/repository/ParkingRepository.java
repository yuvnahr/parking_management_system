package com.parkingmanagement.repository;

import com.parkingmanagement.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Parking entity
 * Provides CRUD operations and custom queries
 */
@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {

    /**
     * Find parking entry by vehicle number
     * @param vehicleNumber the vehicle number
     * @return Optional containing the parking entry if found
     */
    Optional<Parking> findByVehicleNumber(String vehicleNumber);

    /**
     * Find parking entry by slot number
     * @param slotNumber the parking slot number
     * @return Optional containing the parking entry if found
     */
    Optional<Parking> findBySlotNumber(Integer slotNumber);

    /**
     * Check if a vehicle number already exists
     * @param vehicleNumber the vehicle number
     * @return true if vehicle exists, false otherwise
     */
    boolean existsByVehicleNumber(String vehicleNumber);

    /**
     * Check if a slot number is already occupied
     * @param slotNumber the parking slot number
     * @return true if slot is occupied, false otherwise
     */
    boolean existsBySlotNumber(Integer slotNumber);
}
