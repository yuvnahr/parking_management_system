# Quick Start Guide - Smart Parking Management System

## Prerequisites
- Java 17 or higher installed
- Maven 3.6+ installed
- Internet connection for Maven dependencies

## Installation Steps

### Step 1: Navigate to Project Directory
```bash
cd /workspaces/parking_management_system
```

### Step 2: Build the Project
```bash
mvn clean install
```

### Step 3: Run the Application
```bash
mvn spring-boot:run
```

Or alternatively:
```bash
java -jar target/parking-management-system-1.0.0.jar
```

### Step 4: Access the Application
Open your browser and navigate to:
```
http://localhost:8080/
```

## First Test - Add a Vehicle

1. **Open the Home Page**: http://localhost:8080/
2. **Fill the Form**:
   - Vehicle Number: `DL01AB1234`
   - Owner Name: `John Doe`
   - Vehicle Type: `Car`
   - Parking Slot: `101`
3. **Click "Park Vehicle"**
4. **View Result**: Vehicle appears in the table below

## Key Features to Try

### ✅ Add Multiple Vehicles
- Try adding 3-4 different vehicles
- Each should have unique vehicle number and slot
- Entry time automatically generated

### ✅ Test Validation Rules
- Try entering the same vehicle number twice
  - **Expected**: Error message preventing duplicate entry
- Try assigning the same slot to two vehicles
  - **Expected**: Error message preventing slot duplication

### ✅ Remove Vehicles
- Click the "✕ Exit" button next to a vehicle
- Confirm when prompted
- Vehicle is removed from the list

### ✅ Clear Form
- Click "🔄 Clear Form" to reset input fields

## Access Points

| Feature | URL |
|---------|-----|
| Home Page | http://localhost:8080/ |
| H2 Database Console | http://localhost:8080/h2-console |
| View Vehicle Details | http://localhost:8080/view/{vehicleNumber} |

## H2 Console Access

1. Open: http://localhost:8080/h2-console
2. Connection Settings:
   - **JDBC URL**: `jdbc:h2:mem:parkingdb`
   - **Username**: `sa`
   - **Password**: (leave blank)
3. Click "Connect"
4. Run SQL queries to verify data

### Useful SQL Queries:
```sql
-- View all parking entries
SELECT * FROM parking;

-- Count total vehicles
SELECT COUNT(*) as total FROM parking;

-- View vehicles by type
SELECT * FROM parking WHERE vehicle_type = 'Car';
```

## Stop the Application

Press `Ctrl+C` in the terminal where the application is running.

## Troubleshooting

### Q: Application won't start
**A**: 
- Check Java is installed: `java -version`
- Check Maven is installed: `mvn -version`
- Clear Maven cache: `mvn clean`

### Q: Port 8080 already in use
**A**: Change port in `src/main/resources/application.properties`:
```properties
server.port=8090
```

### Q: H2 Console not accessible
**A**: Ensure `spring.h2.console.enabled=true` in `application.properties`

### Q: Database transactions not visible
**A**: 
- Refresh browser page
- Check browser console for errors
- Check server logs for exceptions

## Next Steps

1. **Explore the Code**:
   - Model: `src/main/java/com/parkingmanagement/entity/Parking.java`
   - Controller: `src/main/java/com/parkingmanagement/controller/ParkingController.java`
   - Service: `src/main/java/com/parkingmanagement/service/ParkingService.java`
   - View: `src/main/resources/templates/index.html`

2. **Read Full Documentation**: See `README.md`

3. **Run Tests**: See `TESTING_GUIDE.md`

4. **Switch to MySQL**:
   - Install MySQL
   - Create database: `CREATE DATABASE parking_db;`
   - Update `application.properties` with MySQL connection details

## File Structure

```
parking_management_system/
├── src/
│   ├── main/
│   │   ├── java/com/parkingmanagement/
│   │   │   ├── ParkingManagementApplication.java
│   │   │   ├── controller/ParkingController.java
│   │   │   ├── service/ParkingService.java
│   │   │   ├── repository/ParkingRepository.java
│   │   │   └── entity/Parking.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/index.html
│   └── test/java/...
├── pom.xml
├── README.md
├── TESTING_GUIDE.md
└── QUICK_START.md (this file)
```

## Learning Resources

- **Spring Boot**: https://spring.io/projects/spring-boot
- **Hibernate/JPA**: https://hibernate.org/orm/
- **Thymeleaf**: https://www.thymeleaf.org/
- **Maven**: https://maven.apache.org/

---

**Enjoy using the Smart Parking Management System!** 🅿️
