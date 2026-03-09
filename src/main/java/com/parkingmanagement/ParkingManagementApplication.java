package com.parkingmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Parking Management System application
 */
@SpringBootApplication
public class ParkingManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingManagementApplication.class, args);
        System.out.println("====================================");
        System.out.println("Parking Management System Started!");
        System.out.println("Navigate to: http://localhost:8080/");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("====================================");
    }

}
