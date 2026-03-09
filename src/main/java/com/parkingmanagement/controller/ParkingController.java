package com.parkingmanagement.controller;

import com.parkingmanagement.entity.Parking;
import com.parkingmanagement.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for handling parking management requests
 * Handles HTTP requests and interacts with the service layer
 */
@Controller
@RequestMapping("/")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    /**
     * Display the home page with vehicle entry form and active parking list
     * GET /
     *
     * @param model the Spring model for passing data to the view
     * @return the home view template
     */
    @GetMapping
    public String displayHomePage(Model model) {
        // Initialize a new parking object for the form
        model.addAttribute("parking", new Parking());

        // Get all active parking entries
        List<Parking> parkingList = parkingService.getAllParkingEntries();
        model.addAttribute("parkingList", parkingList);

        // Add total count of vehicles
        model.addAttribute("totalVehicles", parkingList.size());

        return "index";
    }

    /**
     * Save a new parking entry
     * POST /save
     *
     * @param parking the parking entry from form submission
     * @param redirectAttributes for flash attributes (error/success messages)
     * @return redirect to home page
     */
    @PostMapping("/save")
    public String saveParkingEntry(@ModelAttribute Parking parking, RedirectAttributes redirectAttributes) {
        try {
            // Validate and save the parking entry
            parkingService.saveParkingEntry(parking);

            // Add success message
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Vehicle with number '" + parking.getVehicleNumber() 
                    + "' has been successfully parked in slot " + parking.getSlotNumber() + "!");

            return "redirect:/";
        } catch (IllegalArgumentException e) {
            // Add error message if validation fails
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("parking", parking);

            return "redirect:/";
        } catch (Exception e) {
            // Handle unexpected errors
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "An unexpected error occurred: " + e.getMessage());

            return "redirect:/";
        }
    }

    /**
     * View parking details by vehicle number
     * GET /view/{vehicleNumber}
     *
     * @param vehicleNumber the vehicle number to search for
     * @param model the Spring model
     * @param redirectAttributes for flash attributes
     * @return the home view or error message
     */
    @GetMapping("/view/{vehicleNumber}")
    public String viewParkingByVehicle(@PathVariable String vehicleNumber, 
                                       Model model, RedirectAttributes redirectAttributes) {
        var parking = parkingService.getParkingByVehicleNumber(vehicleNumber);
        
        if (parking.isPresent()) {
            model.addAttribute("parkingDetails", parking.get());
            return "parking-details";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Parking entry not found for vehicle number: " + vehicleNumber);
            return "redirect:/";
        }
    }

    /**
     * Delete a parking entry when vehicle exits
     * GET /delete/{id}
     *
     * @param id the parking entry ID to delete
     * @param redirectAttributes for flash attributes
     * @return redirect to home page
     */
    @GetMapping("/delete/{id}")
    public String deleteParkingEntry(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var parking = parkingService.getParkingById(id);
            
            if (parking.isPresent()) {
                parkingService.deleteParkingEntry(id);
                redirectAttributes.addFlashAttribute("successMessage", 
                        "Vehicle '" + parking.get().getVehicleNumber() 
                        + "' has been removed from parking slot " + parking.get().getSlotNumber() + ".");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", 
                        "Parking entry not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Error removing vehicle: " + e.getMessage());
        }
        
        return "redirect:/";
    }

}
