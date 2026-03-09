# Database Configuration Guide

This guide explains how to configure different databases with the Smart Parking Management System.

## Current Setup: H2 Database (In-Memory)

The application is pre-configured with **H2 Database** which is:
- ✅ Easy to set up (no installation required)
- ✅ Perfect for testing and development
- ✅ In-memory (data is lost on restart)
- ✅ Accessible via H2 Console

### Current Configuration
File: `src/main/resources/application.properties`

```properties
# ==========================================
# H2 Database Configuration (Development)
# ==========================================
spring.datasource.url=jdbc:h2:mem:parkingdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console (for debugging)
spring.h2.console.enabled=true

# ==========================================
# JPA / Hibernate Configuration
# ==========================================
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Access H2 Console
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:parkingdb`
- Username: `sa`
- Password: (blank)

---

## Switch to MySQL Database

### Step 1: Install MySQL
```bash
# Ubuntu/Debian
sudo apt-get install mysql-server

# macOS (using Homebrew)
brew install mysql

# Or download from: https://www.mysql.com/downloads/
```

### Step 2: Create Database
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE parking_db;

# Create user (optional but recommended)
CREATE USER 'parking_user'@'localhost' IDENTIFIED BY 'parking_password';
GRANT ALL PRIVILEGES ON parking_db.* TO 'parking_user'@'localhost';
FLUSH PRIVILEGES;

# Exit MySQL
EXIT;
```

### Step 3: Update application.properties
Edit `src/main/resources/application.properties` and replace the H2 configuration with:

```properties
# ==========================================
# MySQL Database Configuration
# ==========================================
spring.datasource.url=jdbc:mysql://localhost:3306/parking_db
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=your_password

# Optional: Use parking_user if created above
# spring.datasource.username=parking_user
# spring.datasource.password=parking_password

# ==========================================
# JPA / Hibernate Configuration
# ==========================================
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# MySQL specific properties
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Step 4: Rebuild and Run
```bash
# Clean and rebuild
mvn clean install

# Run the application
mvn spring-boot:run
```

### Step 5: Verify Connection
- Application should start without database errors
- Tables will be auto-created in MySQL database
- Check MySQL for tables:
  ```bash
  mysql -u root -p
  USE parking_db;
  SHOW TABLES;
  SELECT * FROM parking;
  ```

---

## Switch to PostgreSQL Database

### Step 1: Install PostgreSQL
```bash
# Ubuntu/Debian
sudo apt-get install postgresql postgresql-contrib

# macOS (using Homebrew)
brew install postgresql

# Or download from: https://www.postgresql.org/download/
```

### Step 2: Create Database
```bash
# Login to PostgreSQL
sudo -u postgres psql

# Create database
CREATE DATABASE parking_db;

# Create user (optional)
CREATE USER parking_user WITH ENCRYPTED PASSWORD 'parking_password';
GRANT ALL PRIVILEGES ON DATABASE parking_db TO parking_user;

# Exit PostgreSQL
\q
```

### Step 3: Update application.properties
```properties
# ==========================================
# PostgreSQL Database Configuration
# ==========================================
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_db
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=your_password

# Optional: Use parking_user if created above
# spring.datasource.username=parking_user
# spring.datasource.password=parking_password

# ==========================================
# JPA / Hibernate Configuration
# ==========================================
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Step 4: Add PostgreSQL Dependency (pom.xml)
```xml
<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.1</version>
    <scope>runtime</scope>
</dependency>
```

### Step 5: Rebuild and Run
```bash
mvn clean install
mvn spring-boot:run
```

---

## Database Comparison

| Feature | H2 | MySQL | PostgreSQL |
|---------|----|----|-----------|
| Setup Difficulty | Very Easy | Easy | Easy |
| Installation Required | No | Yes | Yes |
| Data Persistence | No (In-Memory) | Yes | Yes |
| Performance | Good | Excellent | Excellent |
| Scalability | Limited | Good | Excellent |
| Use Case | Development/Testing | Production | Production |
| Admin Tool | H2 Console | MySQL Workbench | PgAdmin |

---

## Configuration Profiles

For managing different configurations, create separate property files:

### Create New Files:
1. `src/main/resources/application-dev.properties` (for H2)
2. `src/main/resources/application-prod.properties` (for MySQL)
3. `src/main/resources/application-test.properties` (for Testing)

### Example - application-prod.properties:
```properties
spring.datasource.url=jdbc:mysql://prod-server:3306/parking_db
spring.datasource.username=prod_user
spring.datasource.password=prod_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=validate
```

### Run with Profile:
```bash
# Development (H2)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Production (MySQL)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

---

## Common Connection Issues

### Issue: Cannot Connect to MySQL
**Solution**:
1. Verify MySQL is running: `mysql --version`
2. Check credentials in properties file
3. Ensure database exists: `SHOW DATABASES;`
4. Test connection: `mysql -u root -p`

### Issue: Cannot Connect to PostgreSQL
**Solution**:
1. Verify PostgreSQL is running: `psql --version`
2. Check PostgreSQL service: `sudo systemctl status postgresql`
3. Verify credentials and database existence

### Issue: "Access Denied" Error
**Solution**:
1. Verify username and password in properties
2. Check user permissions: `SHOW GRANTS FOR 'user'@'localhost';` (MySQL)
3. Create new user with proper permissions

### Issue: Port Already in Use
**Solution**:
```bash
# Find process using port 3306 (MySQL)
lsof -i :3306
# Kill the process
kill -9 <PID>
```

---

## Best Practices

1. **Development**: Use H2 for quick testing
2. **Testing**: Use H2 with separate test database
3. **Production**: Use MySQL or PostgreSQL with backups
4. **Credentials**: Never hardcode passwords in properties
5. **Backup**: Regular backups for production databases
6. **Monitoring**: Monitor database connections and performance

---

## Environment Variables (Advanced)

Instead of hardcoding credentials, use environment variables:

```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.url=${DB_URL}
```

### Set Environment Variables:
```bash
export DB_USERNAME=parking_user
export DB_PASSWORD=secure_password
export DB_URL=jdbc:mysql://localhost:3306/parking_db

# Run application
mvn spring-boot:run
```

---

## Conclusion

The application is flexible and supports multiple databases. Choose based on your use case:
- **H2**: Quick testing and development
- **MySQL**: Production environment
- **PostgreSQL**: Enterprise production environment

For more information, refer to official documentation:
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate Guide](https://hibernate.org/orm/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
