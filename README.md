# Spam Detector API

A Spring Boot application that detects spam phone numbers and allows users to report spam calls.
The API provides endpoints for user authentication, searching contacts, reporting spam, and calculating spam likelihood.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Building and Running the Application](#building-and-running-the-application)
- [API Endpoints](#api-endpoints)
- [Technologies Used](#technologies-used)
- [Contact Information](#contact-information)

## Prerequisites
- An IDE like IntelliJ IDEA
- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher (mysql  Ver 8.4.2 for macos14 on arm64 (MySQL Community Server - GPL))


## Building and Running the Application

My Recommendation is to use IntelliJ IDEA to build and run the application. However, you can also use the command line.

IntelliJ IDEA Steps:

1. File -> Open -> Select the project folder
2. Wait for IntelliJ IDEA to import the project
3. Right-click on the project folder -> Maven -> Reload Project
4. Right-click on the `spam-detector` folder -> Run 'SpamDetectorApplication'
5. The application should start and you should see the following output in the console:

   ```bash
   2021-10-03 12:00:00.000  INFO 12345 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
   2021-10-03 12:00:00.000  INFO 12345 --- [           main] c.e.s.SpamDetectorApplication            : Started SpamDetectorApplication in 3.14159 seconds (JVM running for 3.14159)
   ```

Command Line Steps:

1. **Build the Project with Maven**:

   ```bash
   mvn clean install
   ```
   
2. **Run the Application**:

   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

## API Endpoints

Refer to the word(docx) file that I send you for the API endpoints.

### Authentication

- **Register a New User**

POST /auth/register


Request Body:

```json
{
  "name": "Jane Doe",
  "phoneNumber": "5551234567",
  "email": "jane.doe@example.com",
  "password": "securePassword123"
}


Login

POST /auth/login
Request Body:

{
  "phoneNumber": "5551234567",
  "password": "securePassword123"
}


Search


Search by Name

GET /search/name?query=John


Search by Phone Number

GET /search/phone?phoneNumber=5551234567


Spam Reporting


Report Spam

POST /spam/report

Request Body:

{
  "reportedPhoneNumber": "5559876543"
}


Get Spam Likelihood

GET /spam/likelihood?phoneNumber=5559876543

```

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Security
- JWT (JSON Web Tokens) for authentication
- Maven for build automation
- MySQL mysql  Ver 8.4.2 for macos14 on arm64 (MySQL Community Server - GPL)
- Lombok for boilerplate code reduction
- Faker for data generation


## Contact Information

If you have any questions or need further information, feel free to contact me.

- **Name**: Ankit Sahu
- **Email**: sahu.ankit010.work@gmail.com
- **LinkedIn**: [Your LinkedIn Profile](https://www.linkedin.com/in/sahuankit010/)


