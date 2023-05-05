

# Spring Boot Project with Maven, JaCoCo, Swagger OpenAPI, and PostgreSQL

This is a Spring Boot project that integrates several popular technologies for building web applications, including Maven for dependency management and building, JaCoCo for code coverage reporting, Swagger OpenAPI for API documentation and testing, and PostgreSQL for database management.

## Requirements

To build and run this project, you'll need the following tools:

- Java Development Kit (JDK) 17 or later
- Apache Maven 3.6 or later
- PostgreSQL 12 or later

## Installation and Setup

1. Clone the repository to your local machine using Git:

   ```
   git clone https://github.com/hotaru-ritsuki/course-api.git
   ```

2. Change directory to the project root:

   ```
   cd course-api
   ```

3. Build the project using Maven:

   ```
   mvn clean install
   ```

4. Create a PostgreSQL database and update the `application.properties` file in the `src/main/resources` directory with your database credentials:

   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_usernamee
   spring.datasource.password=your_password
   ```

5. Run the project:

   ```
   java -jar target/course-api-0.0.1.jar
   ```

6. Access the Swagger UI by opening a web browser and navigating to `http://localhost:8080/swagger-ui.html`.

## Usage

This project provides a simple RESTful API for Course Management Platform. The API allows you to perform the following operations:

- User registration and login 
- Authentication and authorization using JWT
- Role based access(STUDENT, INSTRUCTOR, ADMIN)
- Create courses and corresponding lessons
- Homework upload
- Submissions system
- Course statuses

The API documentation is available in the Swagger UI, which you can access by opening a web browser and navigating to `http://localhost:8080/swagger-ui.html`.

## Code Coverage Reporting

This project includes JaCoCo for code coverage reporting. To generate a code coverage report, run the following command after building the project:

```
mvn jacoco:report
```

The report will be generated in the `target/site/jacoco` directory.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more information.