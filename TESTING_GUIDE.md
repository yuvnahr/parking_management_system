# Testing Guide - Smart Parking Management System

This document provides comprehensive testing instructions for the Smart Parking Slot Management System.

## Quick Start Testing

### 1. Access the Application
- **URL**: http://localhost:8080/
- **Expected**: Home page with vehicle entry form and parking list table

### 2. Test Case 1: Add a Vehicle (Valid Entry)
#### Steps:
1. Navigate to http://localhost:8080/
2. Fill in the form:
   - **Vehicle Number**: `KA01AB1234`
   - **Owner Name**: `Rajesh Kumar`
   - **Vehicle Type**: `Car`
   - **Parking Slot**: `101`
3. Click "Park Vehicle" button

#### Expected Result:
- ✅ Success message: "Vehicle with number 'KA01AB1234' has been successfully parked in slot 101!"
- ✅ Vehicle appears in the "Active Parking List" table
- ✅ Total Vehicles count increases to 1

### 3. Test Case 2: Add Multiple Vehicles
#### Steps:
1. Add second vehicle:
   - Vehicle Number: `MH02CD5678`
   - Owner Name: `Priya Singh`
   - Vehicle Type: `Bike`
   - Parking Slot: `102`
2. Add third vehicle:
   - Vehicle Number: `DL03EF9012`
   - Owner Name: `Anil Verma`
   - Vehicle Type: `SUV`
   - Parking Slot: `103`

#### Expected Result:
- ✅ All three vehicles appear in the list
- ✅ Total Vehicles count shows 3
- ✅ Each vehicle has correct details and entry time

### 4. Test Case 3: Prevent Duplicate Vehicle Entry
#### Steps:
1. Try to add a vehicle with the same number as an existing one
   - Vehicle Number: `KA01AB1234` (same as vehicle 1)
   - Owner Name: `Another Person`
   - Vehicle Type: `Car`
   - Parking Slot: `201`
2. Click "Park Vehicle"

#### Expected Result:
- ❌ Error message: "Vehicle with number 'KA01AB1234' is already parked. Same vehicle cannot enter twice."
- ✅ Vehicle list remains unchanged
- ✅ No duplicate entry created in the database

### 5. Test Case 4: Prevent Duplicate Slot Assignment
#### Steps:
1. Try to assign an existing slot to another vehicle
   - Vehicle Number: `TS04GH3456`
   - Owner Name: `New Owner`
   - Vehicle Type: `Car`
   - Parking Slot: `101` (already occupied by KA01AB1234)
2. Click "Park Vehicle"

#### Expected Result:
- ❌ Error message: "Parking slot '101' is already occupied. Same slot cannot be assigned twice."
- ✅ Vehicle list remains unchanged
- ✅ No duplicate slot assignment created

### 6. Test Case 5: Validate Required Fields
#### Steps:
Try submitting the form with missing fields:

**Scenario A - Empty Vehicle Number:**
- Vehicle Number: (empty)
- Owner Name: `John Doe`
- Vehicle Type: `Car`
- Parking Slot: `201`
- Expected: Form validation error (HTML5 required)

**Scenario B - Empty Owner Name:**
- Vehicle Number: `KA05IJ7890`
- Owner Name: (empty)
- Vehicle Type: `Bike`
- Parking Slot: `201`
- Expected: Form validation error (HTML5 required)

**Scenario C - Invalid Slot Number:**
- Vehicle Number: `KA06KL1234`
- Owner Name: `Test User`
- Vehicle Type: `SUV`
- Parking Slot: `0` or `-5` (invalid)
- Expected: Error or validation warning

#### Expected Result:
- ✅ Form requires all fields to be filled
- ✅ Slot number must be positive
- ✅ Proper validation messages shown

### 7. Test Case 6: Remove Vehicle (Vehicle Exit)
#### Steps:
1. Locate a vehicle in the "Active Parking List" table
2. Click the "✕ Exit" button for that vehicle
3. Confirm the deletion when prompted

#### Expected Result:
- ✅ Success message: "Vehicle 'KA01AB1234' has been removed from parking slot 101."
- ✅ Vehicle is removed from the list
- ✅ Slot becomes available for reuse
- ✅ Total Vehicles count decreases

### 8. Test Case 7: Reuse Freed Slot
#### Steps:
1. Remove a vehicle (from Test Case 6)
2. Add a new vehicle to the freed slot:
   - Vehicle Number: `UP07MN5678`
   - Owner Name: `New Driver`
   - Vehicle Type: `Car`
   - Parking Slot: `101` (previously freed)
3. Click "Park Vehicle"

#### Expected Result:
- ✅ New vehicle successfully parked in slot 101
- ✅ Slot reuse works properly
- ✅ No conflicts with previously occupied slot

### 9. Test Case 8: Entry Time Auto-Generation
#### Steps:
1. Add a new vehicle
2. Check the "Entry Time" column in the table

#### Expected Result:
- ✅ Entry Time is automatically populated with current date and time
- ✅ Format: `YYYY-MM-DD HH:MM:SS`
- ✅ Time is accurate and displayed correctly

### 10. Test Case 9: View Vehicle Details (Optional Feature)
#### Steps:
1. To view details by vehicle number, use URL:
   - http://localhost:8080/view/KA01AB1234

#### Expected Result:
- ✅ Shows parking details for the specified vehicle
- ✅ Displays all vehicle information

### 11. Test Case 10: Clear Form Functionality
#### Steps:
1. Fill the parking form with some data
2. Click the "🔄 Clear Form" button

#### Expected Result:
- ✅ All form fields are cleared/reset
- ✅ Form returns to initial state
- ✅ Ready for new entry

## Database Verification

### Using H2 Console:
1. Navigate to: http://localhost:8080/h2-console
2. Connection URL: `jdbc:h2:mem:parkingdb`
3. Username: `sa`
4. Password: (leave blank)
5. Click "Connect"

### Verify Data:
```sql
-- View all parking entries
SELECT * FROM parking;

-- Check for unique vehicle numbers
SELECT vehicle_number, COUNT(*) FROM parking GROUP BY vehicle_number HAVING COUNT(*) > 1;

-- Check for unique slot numbers
SELECT slot_number, COUNT(*) FROM parking GROUP BY slot_number HAVING COUNT(*) > 1;

-- Count total vehicles
SELECT COUNT(*) as total_vehicles FROM parking;
```

## API Endpoints Testing (cURL)

### Test Add Vehicle:
```bash
curl -X POST http://localhost:8080/save \
  -d "vehicleNumber=KA01AB1234" \
  -d "ownerName=John+Doe" \
  -d "vehicleType=Car" \
  -d "slotNumber=101"
```

### Test View All Data:
```bash
curl http://localhost:8080/
```

### Test Delete Vehicle:
```bash
curl http://localhost:8080/delete/1
```

## Performance Testing

### Load Test:
1. Add 50+ vehicles to test system performance
2. Verify the list displays correctly
3. Check database query times in logs

### Stress Test:
1. Rapid form submissions
2. Multiple simultaneous requests
3. Verify data integrity

## Error Scenarios Testing

| Test Case | Input | Expected Behavior |
|-----------|-------|-------------------|
| Invalid Vehicle Number Format | `""` or only spaces | Error: Vehicle number cannot be empty |
| Invalid Owner Name | `""` or only spaces | Error: Owner name cannot be empty |
| Invalid Vehicle Type | Choose dropdown or empty | Form requires selection |
| Invalid Slot Number | `0`, `-5`, or non-numeric | Error or validation warning |
| Duplicate Vehicle | Same vehicle number twice | Error: Vehicle already parked |
| Duplicate Slot | Same slot twice | Error: Slot already occupied |
| Very Long Vehicle Number | 50+ characters | May be truncated or rejected |
| Special Characters in Name | `@#$%^&*()` | May be accepted or escaped |

## Browser Compatibility Testing

Test on the following browsers:
- ✅ Chrome/Chromium
- ✅ Firefox
- ✅ Safari
- ✅ Edge
- ✅ Mobile Browsers (Responsive Design)

## Responsive Design Testing

### Desktop View (1920x1080):
- ✅ Form displays in grid layout
- ✅ Table displays all columns clearly
- ✅ No horizontal scrolling

### Tablet View (768px):
- ✅ Form converts to 2-column layout
- ✅ Table remains readable
- ✅ Proper spacing maintained

### Mobile View (375px):
- ✅ Form converts to single column
- ✅ Table scrolls horizontally
- ✅ Touch-friendly buttons

## Data Persistence Testing

### After Application Restart:
1. Note the current vehicle list
2. Stop the application (Ctrl+C)
3. Restart the application
4. Check if data persists

**Note**: With H2 in-memory mode, data will be lost on restart. For persistence, switch to MySQL following the README instructions.

## Notes

- All tests should pass successfully
- No errors should appear in the console
- Database queries should execute without issues
- UI should be responsive and user-friendly
- All validation rules should be enforced

## Troubleshooting

If tests fail:
1. Check the browser console for JavaScript errors
2. Check the server logs for Spring Boot exceptions
3. Verify the database connection
4. Ensure all required fields are filled
5. Check the database for duplicate entries

---

**Last Updated**: 2026-03-09
