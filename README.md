# CafeConnect (A Cafe Management System Backend)
## Overview
This project is the backend implementation of a Cafe Management System, providing a robust and scalable API to manage various aspects of a cafe. The system covers user management, product management, category management, and bill handling. The backend is built using Java and Spring Boot, with PostgreSQL as the database. Postman was used extensively for API testing and validation.

## Technologies Used
- **Java**: Programming language for the backend logic.
- **Spring Boot**: Framework used for building the backend RESTful APIs.
- **PostgreSQL**: Relational database management system used for storing data.
- **Postman**: API testing tool used to test and validate the APIs.

## Project Structure

```
CafeConnect/
│
├── src/main/java/com.cafeconnect/
│   ├── rest/             # REST controllers for handling HTTP requests
│   ├── service/          # Business logic services
│   ├── dao/              # dao for database interactions
│   ├── model/            # Entity classes representing database tables
│   └── utils/            # Custom helper classes
│
├── src/main/resources/
│   ├── application.properties  # Configuration for database, security, etc.
│  
├── pom.xml              # Maven dependencies and build configuration
└── README.md            # Project documentation
```

## API Documentation
### Dashboard Management
- GET /dashboard/details: Getting all details to show on dashboard page.

### User Management
- POST /user/signup: Register a new user.
- POST /user/login: Authenticate and log in a user.
- GET /user/get: Get user details.
- POST /user/update: update details of user.
- GET /user/checkToken: Verify or refresh JWT token.
- POST /user/changePassword: Change the password.
- POST /user/forgetPassword: Initiate the forget password process.

### Product Management
- POST /product/add: Add a new product.
- GET /product/get: Retrieve all products.
- GET /product/getById/{id}: Retrieve product by ID.
- GET /product/getByCategory/{id}: Retrieve products by category ID.
- POST /product/update: Update a product.
- POST /product/delete/{id}: Delete a product.
- POST /product/updateStatus: Update the product's status.

### Category Management
- POST /category/add: Add a new category.
- GET /category/get: Retrieve all categories.
- PUT /category/update: Update a category.

### Bill Management
- GET /bill/getBills: Retrieve all bills.
- POST /bills/generateReport: Generating report of a bill.
- GET /bills/getPdf: Generate a PDF for a specific bill.
- DELETE /bills/delete/{id}: Delete a bill.


## Database Configuration
The application uses PostgreSQL as the database. Update the application.properties file with your database credentials.

## properties
```
spring.datasource.url=jdbc:postgresql://localhost:5432/cafedb?stringtype=unspecified
spring.datasource.username=username
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```
## Testing the APIs
- Use Postman to test the APIs. Import the Postman collection provided in the postman/ directory for a comprehensive set of API tests.
- Ensure the server is running before testing.
- Each endpoint returns responses in JSON format.
## Getting Started
### Prerequisites
- Java 22+
- Maven 3.9+
- PostgreSQL
## Installation
1. Clone the repository:
```
git clone https://github.com/RohitashGarhwal/CafeConnect.git
```

2. Navigate to the project directory:
```
cd CafeConnect
```

3. Configure your database in src/main/resources/application.properties.


4. Build the project:
```
mvn clean install
```

5. Run the application:
```
mvn spring-boot:run
```

6. Access the API at http://localhost:8081.

## Contributing
Contributions are welcome! Please create a pull request with a detailed description of the changes.

## License
This project is licensed under the MIT License - see the LICENSE file for details.