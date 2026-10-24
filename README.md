# Air Ambulance Management System

The **Air Ambulance Management System** is a full-stack application developed using Java, Spring Boot, and MySQL to manage emergency requests, patients, hospitals, and available air ambulances. The system includes functionality for managing emergency bookings and assigning suitable aircraft based on operational requirements and availability. Database operations support the storage and retrieval of application records.

## Features

### 1. Emergency Booking Management
- Create and manage emergency transport bookings.
- Retrieve booking information and booking records.
- Support air ambulance assignment for emergency requests.
- Update booking status and support booking cancellation.

### 2. Hospital Management
- Maintain hospital information for emergency coordination.
- Support hospital-related booking operations.

### 3. Patient Management
- Maintain patient records associated with emergency transport.
- Support patient-related booking operations.

### 4. Air Ambulance Management
- Maintain air ambulance records.
- Support the assignment of an air ambulance to an emergency booking.
- Manage aircraft information and availability as supported by the application.

### 5. Authentication and Authorization
- Spring Security-based authentication.
- JWT authentication filter and token-based request security.
- BCrypt password encoding.
- Role-based authorization support.

### 6. REST API Documentation
- API documentation using SpringDoc OpenAPI and Swagger UI.

## Tech Stack

### Backend
- **Language:** Java 21
- **Framework:** Spring Boot 3.3.2
- **Security:** Spring Security and JWT
- **Persistence:** Spring Data JPA and Hibernate
- **Database:** H2 for the configured local default; MySQL connector available
- **API Documentation:** SpringDoc OpenAPI / Swagger UI
- **Build Tool:** Maven
- **Utilities:** Lombok and Bean Validation

### Frontend
- **Framework:** React 18
- **Build Tool:** Vite
- **UI Library:** Material UI
- **Routing:** React Router DOM
- **HTTP Client:** Axios

## Project Structure

```text
Air-Ambulance-Management-System/
├── backend/
│   ├── src/main/java/com/airambulance/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── exception/
│   │   ├── repository/
│   │   └── service/
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── context/
│   │   ├── pages/
│   │   └── theme/
│   ├── package.json
│   ├── vite.config.js
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```

## Prerequisites

- JDK 21
- Apache Maven
- Node.js and npm
- MySQL Server, if using MySQL instead of the configured H2 database

## Getting Started

### 1. Run the Backend

From the project root, navigate to the backend directory:

```bash
cd backend
```

Build the application:

```bash
mvn clean install
```

Start the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend is configured to run on:

`http://localhost:8080`

Swagger UI is configured at:

`http://localhost:8080/swagger-ui.html`

### 2. Database Configuration

The current `application.properties` configuration uses an in-memory H2 database for local development.

To use MySQL, configure the MySQL datasource URL, username, password, and driver in `application.properties`, and ensure the MySQL server is running.

Do not publish real database passwords or production secrets in the repository.

### 3. Run the Frontend

Open a separate terminal and navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

Vite will display the local frontend URL in the terminal. Use that URL to open the application.

## Project Objective

The objective of this project is to simplify emergency air ambulance management through centralized record handling, emergency booking management, and aircraft assignment based on operational requirements and availability. The project uses Java, Spring Boot, and database operations to support its core functionality.
