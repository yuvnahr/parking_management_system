# Smart Parking Slot Management System

A web-based Smart Parking Slot Management System built with Spring Boot, Hibernate/JPA, and a relational database (H2/MySQL). This application performs basic CRUD operations for managing parking slots efficiently.

## Features

✅ **Vehicle Entry Form** - Add new parking entries with vehicle details
✅ **Active Parking List** - View all currently parked vehicles in a table format
✅ **Duplicate Prevention** - Same vehicle number cannot enter twice
✅ **Slot Allocation** - Same parking slot cannot be assigned twice
✅ **Auto-Generated Fields** - ID and Entry Time are automatically generated
✅ **Data Persistence** - All data is stored in the database
✅ **MVC Architecture** - Proper separation of Model, View, and Controller
✅ **Responsive UI** - Works seamlessly on desktop and mobile devices

## Project Structure

```
parking_management_system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/parkingmanagement/
│   │   │       ├── ParkingManagementApplication.java    (Main entry point)
│   │   │       ├── entity/
│   │   │       │   └── Parking.java                     (JPA Entity)
│   │   │       ├── repository/
│   │   │       │   └── ParkingRepository.java           (Data access layer)
│   │   │       ├── service/
│   │   │       │   └── ParkingService.java              (Business logic)
│   │   │       └── controller/
│   │   │           └── ParkingController.java           (Request handling)
│   │   └── resources/
│   │       ├── application.properties                   (Configuration)
│   │       └── templates/
│   │           └── index.html                           (Thymeleaf template)
│   └── test/
│       └── java/
│           └── com/parkingmanagement/
│               └── ParkingManagementApplicationTests.java
├── pom.xml                                               (Maven configuration)
├── .gitignore
├── README.md
└── LICENSE
```

## Technology Stack

- **Backend Framework**: Spring Boot 3.2.0
- **ORM**: Hibernate/JPA
- **Database**: H2 (In-memory, embedded)
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Java Version**: 17+
- **Markup**: HTML5
- **Styling**: CSS3

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Internet connection (for Maven dependencies)

## Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yuvnahr/parking_management_system.git
cd parking_management_system
```

### 2. Build the Project
```bash
mvn clean install
```

This command will:
- Download all required dependencies
- Compile the Java code
- Package the application

### 3. Run the Application
```bash
mvn spring-boot:run
```

Or if you prefer using JAR:
```bash
java -jar target/parking-management-system-1.0.0.jar
```

### 4. Access the Application
- **Home Page**: http://localhost:8080/
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:parkingdb`
  - Username: `sa`
  - Password: (leave blank)

## API Endpoints

### Display Home Page
```
GET /
```
Response: HTML page with vehicle entry form and parking list

### Save Vehicle Entry
```
POST /save
```
**Form Parameters:**
- `vehicleNumber` (string, required) - Vehicle registration number
- `ownerName` (string, required) - Owner's name
- `vehicleType` (string, required) - Type: Car, Bike, or SUV
- `slotNumber` (integer, required) - Parking slot number

**Validations:**
- Vehicle number must be unique (no duplicates)
- Parking slot must be unique (no duplicates)
- All fields are required
- Slot number must be positive

**Response:** Redirect to home page with success/error message

### View Parking Details
```
GET /view/{vehicleNumber}
```
Retrieves parking details for a specific vehicle

### Delete Parking Entry (Vehicle Exit)
```
GET /delete/{id}
```
Removes a vehicle from the parking system

## Database Schema

### Parking Table
```sql
CREATE TABLE parking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_number VARCHAR(20) UNIQUE NOT NULL,
    owner_name VARCHAR(50) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    slot_number INT UNIQUE NOT NULL,
    entry_time TIMESTAMP NOT NULL
);
```

## Entity Model

### Parking Class
```java
@Entity
@Table(name = "parking")
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // Auto-generated ID
    
    @Column(unique = true, nullable = false)
    private String vehicleNumber;       // Vehicle registration number
    
    @Column(nullable = false)
    private String ownerName;           // Owner's full name
    
    @Column(nullable = false)
    private String vehicleType;         // Car, Bike, or SUV
    
    @Column(unique = true, nullable = false)
    private Integer slotNumber;         // Parking slot number
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime entryTime;    // Entry timestamp
}
```

## Service Layer Features

The `ParkingService` class provides:

1. **getAllParkingEntries()** - Retrieve all active parking records
2. **getParkingById(Long id)** - Find parking entry by ID
3. **getParkingBySlotNumber(Integer slotNumber)** - Find by slot number
4. **getParkingByVehicleNumber(String vehicleNumber)** - Find by vehicle number
5. **saveParkingEntry(Parking parking)** - Add new entry with validation
6. **deleteParkingEntry(Long id)** - Remove parking entry
7. **getTotalParkingCount()** - Count total active entries

## Validation Rules

1. **Duplicate Vehicle Prevention**
   - The system checks if a vehicle with the same registration number already exists
   - Error: "Vehicle with number 'DL01AB1234' is already parked. Same vehicle cannot enter twice."

2. **Duplicate Slot Prevention**
   - The system ensures no two vehicles occupy the same parking slot
   - Error: "Parking slot '101' is already occupied. Same slot cannot be assigned twice."

3. **Field Validation**
   - All fields are mandatory
   - Vehicle number and owner name must not be empty
   - Slot number must be positive (> 0)

## Testing the Application

### Test Case 1: Add a Vehicle
1. Go to http://localhost:8080/
2. Fill in the form:
   - Vehicle Number: `DL01AB1234`
   - Owner Name: `John Doe`
   - Vehicle Type: `Car`
   - Parking Slot: `101`
3. Click "Park Vehicle"
4. Success message appears, vehicle appears in the list

### Test Case 2: Duplicate Vehicle Prevention
1. Try to add the same vehicle number again
2. Error message: "Vehicle with number 'DL01AB1234' is already parked."

### Test Case 3: Duplicate Slot Prevention
1. Try to park another vehicle in slot `101`
2. Error message: "Parking slot '101' is already occupied."

### Test Case 4: Remove Vehicle
1. Click "Exit" button next to a vehicle in the list
2. Confirm the action
3. Vehicle is removed from the list

## Configuration

### Database Configuration (H2)
Edit `src/main/resources/application.properties`:

```properties
# H2 Configuration
spring.datasource.url=jdbc:h2:mem:parkingdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

### Switch to MySQL (Optional)
Uncomment in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/parking_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## Troubleshooting

### Issue: Port 8080 already in use
**Solution**: Change the port in `application.properties`:
```properties
server.port=8090
```

### Issue: H2 Console not accessible
**Solution**: Ensure `spring.h2.console.enabled=true` in `application.properties`

### Issue: Tables not created
**Solution**: Check that `spring.jpa.hibernate.ddl-auto=update` is set

### Issue: Maven dependencies not downloading
**Solution**: Run `mvn clean install -U` to force update

## Performance Considerations

- H2 Database is in-memory (data lost on restart for testing)
- For production, use MySQL or PostgreSQL
- Implement pagination for large datasets
- Add caching for frequently accessed data

## Future Enhancements

- 🔒 User authentication and authorization
- 📊 Dashboard with statistics and analytics
- 📅 Reservation system
- 💳 Payment integration
- 📱 Mobile app support
- 🔔 Notifications for available slots
- 📍 GPS integration for vehicle location

## Contributing

Contributions are welcome! Please feel free to submit a pull request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or suggestions, please open an issue in the GitHub repository.

---

**Happy Parking!** 🅿️

```
Developed for UE23CS352B: OBJECT ORIENTED ANALYSIS AND DESIGN
PES University, Department of Computer Science and Engineering
```
