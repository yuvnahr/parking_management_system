# Developer Guide - Smart Parking Management System

## 👨‍💻 For Developers: Understanding the Codebase

This guide explains the project structure, code organization, and how to extend the system.

---

## 📁 Project Structure Explained

### Entry Point
**File**: `src/main/java/com/parkingmanagement/ParkingManagementApplication.java`
```java
@SpringBootApplication
public class ParkingManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParkingManagementApplication.class, args);
    }
}
```
- Application starts here
- Spring Boot auto-configures beans
- Embedded Tomcat server starts

---

## 🏛️ MVC Layers

### 1️⃣ Model Layer (Data)
**Location**: `src/main/java/com/parkingmanagement/entity/Parking.java`

**Purpose**: Represents the parking record in the database

**Key Components**:
- `@Entity`: Marks as JPA entity
- `@Table`: Maps to database table "parking"
- `@UniqueConstraint`: Enforces unique vehicle number and slot
- `@GeneratedValue`: Auto-generates ID
- `@PrePersist`: Auto-generates entry time before insert

**Important Methods**:
```java
// Getters and Setters for all fields
public String getVehicleNumber() { ... }
public void setVehicleNumber(String vehicleNumber) { ... }

// Auto-generate entry time
@PrePersist
protected void onCreate() {
    if (entryTime == null) {
        entryTime = LocalDateTime.now();
    }
}
```

### 2️⃣ View Layer (UI)
**Location**: `src/main/resources/templates/index.html`

**Purpose**: HTML/CSS template for user interface

**Key Sections**:
- Header with application title
- Statistics section (vehicle count)
- Vehicle entry form
- Active parking list table
- Alert messages (success/error)

**Technologies**:
- HTML5 for structure
- CSS3 for styling (responsive design)
- Thymeleaf for dynamic content

**Thymeleaf Features Used**:
```html
<!-- Variable binding -->
<input th:field="*{vehicleNumber}">

<!-- Iteration -->
<tr th:each="parking : ${parkingList}">

<!-- Conditional rendering -->
<div th:if="${successMessage}" class="alert-success">

<!-- Format date/time -->
<span th:text="${#temporals.format(parking.entryTime, 'yyyy-MM-dd HH:mm:ss')}">
```

### 3️⃣ Controller Layer (Request Handler)
**Location**: `src/main/java/com/parkingmanagement/controller/ParkingController.java`

**Purpose**: Handles HTTP requests and responses

**Request Handlers**:
```java
@GetMapping("/")  // Display home page
public String displayHomePage(Model model)

@PostMapping("/save")  // Add new vehicle
public String saveParkingEntry(@ModelAttribute Parking parking, ...)

@GetMapping("/view/{vehicleNumber}")  // View vehicle details
public String viewParkingByVehicle(@PathVariable String vehicleNumber, ...)

@GetMapping("/delete/{id}")  // Remove vehicle
public String deleteParkingEntry(@PathVariable Long id, ...)
```

**Responsibilities**:
- Receive HTTP requests
- Extract parameters/form data
- Call service layer
- Pass data to view
- Handle redirects and error messages

---

## 🔧 Service Layer (Business Logic)
**Location**: `src/main/java/com/parkingmanagement/service/ParkingService.java`

**Purpose**: Contains all business logic and validation

**Key Methods**:
```java
// Retrieve operations
public List<Parking> getAllParkingEntries()
public Optional<Parking> getParkingById(Long id)
public Optional<Parking> getParkingBySlotNumber(Integer slotNumber)

// Create operation (with validation)
public Parking saveParkingEntry(Parking parking)
// Validates:
// - No duplicate vehicle numbers
// - No duplicate slot numbers
// - All fields required
// - Auto-generates entry time

// Delete operation
public void deleteParkingEntry(Long id)

// Count operation
public long getTotalParkingCount()
```

**Validation Logic**:
```java
// Check duplicate vehicle
if (parkingRepository.existsByVehicleNumber(parking.getVehicleNumber())) {
    throw new IllegalArgumentException("Vehicle already parked");
}

// Check duplicate slot
if (parkingRepository.existsBySlotNumber(parking.getSlotNumber())) {
    throw new IllegalArgumentException("Slot already occupied");
}

// Validate slot number
if (parking.getSlotNumber() <= 0) {
    throw new IllegalArgumentException("Slot must be positive");
}
```

---

## 💾 Repository Layer (Data Access)
**Location**: `src/main/java/com/parkingmanagement/repository/ParkingRepository.java`

**Purpose**: Provides database operations (extends JpaRepository)

**Methods Available**:
```java
// From JpaRepository
save(Parking)          // INSERT/UPDATE
delete(Parking)        // DELETE
findById(Long)         // SELECT by ID
findAll()              // SELECT all
count()                // COUNT

// Custom methods
findByVehicleNumber(String)      // Find specific vehicle
findBySlotNumber(Integer)        // Find by slot
existsByVehicleNumber(String)    // Check if vehicle exists
existsBySlotNumber(Integer)      // Check if slot exists
```

**How it Works**:
- Extends `JpaRepository<Parking, Long>`
- Spring Data generates implementations
- SQL queries generated automatically
- Uses Hibernate for ORM

---

## 🗄️ Database Layer (Configuration)
**Location**: `src/main/resources/application.properties`

**Current Configuration** (H2):
```properties
spring.datasource.url=jdbc:h2:mem:parkingdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

**What This Means**:
- H2 in-memory database
- Auto-create/update tables (ddl-auto=update)
- Show SQL in logs (spring.jpa.show-sql=true)
- Format SQL output (hibernateformat_sql=true)

---

## 🔄 Request-Response Flow

```
1. User submits form
   ↓
2. Browser sends POST /save request
   ↓
3. ParkingController.saveParkingEntry() receives request
   ↓
4. Controller calls ParkingService.saveParkingEntry()
   ↓
5. Service validates business rules
   ↓
6. Service calls ParkingRepository.save()
   ↓
7. Repository executes SQL INSERT via Hibernate
   ↓
8. Database stores record
   ↓
9. Service returns saved Parking object
   ↓
10. Controller redirects to home page with message
   ↓
11. Browser loads GET / to display updated page
   ↓
12. Controller calls ParkingService.getAllParkingEntries()
   ↓
13. Service calls ParkingRepository.findAll()
   ↓
14. Repository executes SQL SELECT
   ↓
15. Data returned to Thymeleaf template
   ↓
16. HTML rendered with vehicle list
   ↓
17. Browser displays page to user
```

---

## 🛠️ How to Extend the System

### Add a New Field to Parking
1. Add field to `Parking.java`:
```java
@Column(nullable = false)
private String contactNumber;
```

2. Add getter/setter:
```java
public String getContactNumber() { return contactNumber; }
public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
```

3. Add to HTML form in `index.html`:
```html
<input type="tel" name="contactNumber" required>
```

4. Database automatically updates on startup

### Add a New Endpoint
1. Add method to `ParkingController.java`:
```java
@GetMapping("/report")
public String generateReport(Model model) {
    long totalVehicles = parkingService.getTotalParkingCount();
    model.addAttribute("total", totalVehicles);
    return "report";
}
```

2. Create new template `src/main/resources/templates/report.html`

3. Access at `/report` URL

### Add New Query
1. Add method to `ParkingRepository.java`:
```java
List<Parking> findByVehicleType(String vehicleType);
```

2. Spring Data auto-implements based on method name

3. Use in service:
```java
public List<Parking> getParkingByType(String vehicleType) {
    return parkingRepository.findByVehicleType(vehicleType);
}
```

### Add Service Method
1. Add to `ParkingService.java`:
```java
public List<Parking> searchByOwnerName(String name) {
    return parkingRepository.findAll().stream()
        .filter(p -> p.getOwnerName().contains(name))
        .collect(Collectors.toList());
}
```

2. Call from controller:
```java
@GetMapping("/search")
public String search(@RequestParam String name, Model model) {
    model.addAttribute("results", parkingService.searchByOwnerName(name));
    return "search-results";
}
```

---

## 🧪 Development Best Practices

### 1. Code Organization
```
✅ Controllers: HTTP request handling only
✅ Services: Business logic and validation
✅ Repositories: Database operations only
❌ Don't mix concerns
```

### 2. Naming Conventions
```java
// Classes
public class ParkingController { }  // UpperCamelCase

// Methods
public void saveParkingEntry() { }  // lowerCamelCase with verb

// Variables
private String vehicleNumber;       // lowerCamelCase

// Constants
private static final int MAX_SLOTS = 1000;  // UPPER_SNAKE_CASE
```

### 3. Exception Handling
```java
try {
    parkingService.saveParkingEntry(parking);
} catch (IllegalArgumentException e) {
    redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
}
```

### 4. Logging (Optional)
```java
private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);
logger.info("Vehicle added: " + parking.getVehicleNumber());
logger.error("Database error: ", e);
```

### 5. Comments
```java
/**
 * Save a new parking entry with validation
 * @param parking the parking entry to save
 * @return the saved parking entity
 * @throws IllegalArgumentException if validation fails
 */
public Parking saveParkingEntry(Parking parking) { ... }
```

---

## 🧪 Writing Tests

### Unit Test Example
```java
@SpringBootTest
class ParkingServiceTest {
    @Autowired
    private ParkingService parkingService;
    
    @Test
    void testDuplicateVehicleNotAllowed() {
        Parking p1 = new Parking("KA01AB1234", "John", "Car", 101);
        parkingService.saveParkingEntry(p1);
        
        Parking p2 = new Parking("KA01AB1234", "Jane", "Bike", 102);
        assertThrows(IllegalArgumentException.class, 
            () -> parkingService.saveParkingEntry(p2));
    }
}
```

### Integration Test Example
```java
@SpringBootTest
@AutoConfigureMockMvc
class ParkingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testAddVehicleEndpoint() throws Exception {
        mockMvc.perform(post("/save")
            .param("vehicleNumber", "KA01AB1234")
            .param("ownerName", "John")
            .param("vehicleType", "Car")
            .param("slotNumber", "101"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
    }
}
```

---

## 🐛 Debugging Tips

### Enable SQL Logging
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Check H2 Console
- URL: http://localhost:8080/h2-console
- Run SQL queries directly
- Verify data is being saved

### Use Browser DevTools
- F12 to open developer tools
- Check Network tab for HTTP requests
- Check Console for JavaScript errors

### Check Application Logs
- Look for Spring Boot startup messages
- Search for ERROR or WARN entries
- Stack traces show exact error locations

---

## 📦 Deployment Considerations

### Production Checklist
- [ ] Switch to MySQL/PostgreSQL
- [ ] Add authentication
- [ ] Enable HTTPS
- [ ] Set up logging
- [ ] Configure error pages
- [ ] Add performance monitoring
- [ ] Backup database strategy
- [ ] Set up automated testing
- [ ] Document API endpoints
- [ ] Security scanning

### Build for Production
```bash
# Create optimized JAR
mvn clean package -DskipTests

# Run with production profile
java -jar parking-management-system-1.0.0.jar --spring.profiles.active=prod
```

---

## 🔗 Useful Resources

### Spring Framework
- https://spring.io/projects/spring-boot
- https://spring.io/projects/spring-data-jpa

### JPA/Hibernate
- https://hibernate.org/orm/
- https://jakarta.ee/specifications/persistence/

### Thymeleaf
- https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html

### Maven
- https://maven.apache.org/
- https://mvnrepository.com/

---

## 📝 Git Workflow

```bash
# Check status
git status

# Add changes
git add .

# Commit changes
git commit -m "feat: Add new parking slot filter"

# Push changes
git push origin main

# Create feature branch
git checkout -b feature/vehicle-search
```

**Commit Message Format**:
```
feat: Add new feature
fix: Fix bug
docs: Update documentation
style: Format code
refactor: Restructure code
test: Add tests
```

---

## 🚀 Performance Optimization Tips

1. **Database Indexing**
   - Add indexes on frequently searched columns
   - Example: `CREATE INDEX idx_vehicle_number ON parking(vehicle_number);`

2. **Pagination**
   - Load data in chunks instead of all at once
   - Use `PagingAndSortingRepository`

3. **Caching**
   - Cache frequently accessed data
   - Use Spring `@Cacheable`

4. **Connection Pooling**
   - Already configured via HikariCP
   - Adjust pool size in properties if needed

5. **Query Optimization**
   - Use custom `@Query` for complex queries
   - Avoid N+1 queries

---

## 📋 Common Development Tasks

### Task: Add email notification on vehicle entry
1. Add `spring-boot-starter-mail` dependency
2. Create `NotificationService.java`
3. Call service from `ParkingController`
4. Configure email properties

### Task: Add pagination to parking list
1. Change repository to `PagingAndSortingRepository`
2. Add `Pageable` parameter to controller
3. Update template with page navigation

### Task: Add search functionality
1. Add search parameter to controller
2. Add filter logic to service
3. Update HTML form with search field
4. Display filtered results

---

## ✅ Code Checklist Before Commit

- [ ] Code follows naming conventions
- [ ] Comments explain complex logic
- [ ] No hardcoded values
- [ ] Error handling implemented
- [ ] Tests written (if applicable)
- [ ] No unused imports
- [ ] No debug print statements
- [ ] Follows MVC architecture
- [ ] Database changes documented
- [ ] NEW: Run `mvn clean install` successfully

---

## 🤝 Contributing Guidelines

1. Fork the project
2. Create feature branch: `git checkout -b feature/your-feature`
3. Make changes and test
4. Commit with clear messages
5. Push to branch
6. Create pull request

---

## 📞 Getting Help

- Check existing documentation
- Review similar code patterns
- Search Stack Overflow
- Read Spring Boot official docs
- Ask in development team

---

This guide should help you understand and extend the Parking Management System. Happy coding! 🚀

