# Air Ambulance Management System

The **Air Ambulance Management System** is a mission-critical full-stack platform designed to facilitate rapid medical evacuation, emergency air transport dispatch, pilot and fleet management, organ transport logistics, and hospital trauma center coordination.

---

## 🌟 Features & Functional Modules

### 🔒 1. Authentication & Security
- **JWT Bearer Token Authentication** with Refresh Token mechanism & Password Hashing via BCrypt.
- **Role-Based Access Control (RBAC)** supporting 4 distinct roles:
  - **ADMIN**: Complete system control, fleet management, hospital onboarding, pilot assignments.
  - **HOSPITAL**: Trauma bed management, organ transport logistics, emergency airlift requests.
  - **PILOT**: Duty status toggle, flight mission assignments, flight deck logs.
  - **PATIENT**: Emergency transport booking requests, status tracking, medical profile.

### 🏥 2. Hospital Module
- Onboard certified trauma centers & regional hospital helipads.
- Track total and available ICU bed capacity.
- Search and filter hospitals by name or city.

### 🚁 3. Air Ambulance Fleet Module
- Register and maintain medical helicopters (e.g. Eurocopter EC135, Bell 429) & fixed-wing jets.
- Track registration numbers, passenger capacity, current base location, fuel status, availability, and assigned pilots.

### 👨‍✈️ 4. Pilot Module
- Maintain pilot flight licenses, flight experience (years), phone contacts, and duty status (`AVAILABLE`, `ON_MISSION`, `OFF_DUTY`).
- Direct assignment of pilots to air ambulances.

### 🩺 5. Patient Module
- Detailed patient records including age, gender, blood group, emergency contacts, and medical condition notes.

### 🚨 6. Emergency Booking Dispatch Workflow
```
Patient / Dispatcher Requests Transport
                ↓
Auto-Assigns Nearest Available Air Ambulance & Pilot
                ↓
Notifies Assigned Pilot (Flight Deck)
                ↓
Notifies Destination Hospital (Trauma Center)
                ↓
Live Booking Status Updates: REQUESTED → ASSIGNED → IN_TRANSIT → COMPLETED
```

### 🫀 7. Organ Transport Logistics
- High-priority organ transport logistics (Heart, Liver, Kidneys) between source and recipient hospitals with estimated time of arrival (ETA) tracking.

### 🔔 8. Real-time Notifications Module
- Automated notification alerts generated upon booking creation, pilot dispatch, takeoff, landing, and organ transport alerts.

---

## 🛠 Tech Stack

### Backend
- **Java**: 21 / 23
- **Framework**: Spring Boot 3.3.x
- **Security**: Spring Security, JWT (io.jsonwebtoken)
- **Database / ORM**: MySQL 8.0, Spring Data JPA, Hibernate
- **Validation & Helpers**: Bean Validation (Hibernate Validator), Lombok
- **API Documentation**: OpenAPI 3.0 / Swagger UI (SpringDoc v2.5.0)
- **Containerization**: Docker & Docker Compose

### Frontend
- **Framework**: React 18 + Vite
- **UI Library**: Material UI (MUI v5)
- **Styling Theme**: Custom Dark Blue Medical Glassmorphism Theme
- **Routing**: React Router DOM v6
- **HTTP Client**: Axios with Interceptors for JWT & Auto-Refresh
- **State Management**: React Context API (`AuthContext`)

---

## 📁 Project Structure

```
Air-Ambulance-Management-System/
├── backend/
│   ├── src/main/java/com/airambulance/
│   │   ├── AirAmbulanceApplication.java
│   │   ├── config/ (SecurityConfig, JwtUtils, JwtAuthenticationFilter, DataInitializer, OpenApiConfig)
│   │   ├── controller/ (Auth, Hospital, AirAmbulance, Pilot, Patient, EmergencyBooking, OrganTransport, Notification, Dashboard)
│   │   ├── dto/ (Requests & Responses)
│   │   ├── entity/ (User, Role, Hospital, AirAmbulance, Pilot, Patient, EmergencyBooking, OrganTransport, Notification)
│   │   ├── enums/ (RoleName, AmbulanceStatus, FuelStatus, PilotStatus, BookingStatus, EmergencyLevel, OrganPriority, NotificationType)
│   │   ├── exception/ (GlobalExceptionHandler, ResourceNotFoundException, BadRequestException)
│   │   ├── repository/ (JpaRepositories)
│   │   └── service/ (Services & Implementation classes)
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── src/
│   │   ├── api/ (Axios Instance & Service layers)
│   │   ├── components/ (Navbar, Sidebar, Footer, ProtectedRoute, LoadingSpinner, StatCard, StatusChip)
│   │   ├── context/ (AuthContext)
│   │   ├── pages/ (Landing, Login, Register, Dashboards, Hospitals, AirAmbulances, Bookings, OrganTransports, Patients, Notifications, Profile)
│   │   ├── theme/ (Custom Dark Blue Medical Theme)
│   │   ├── App.jsx
│   │   ├── index.css
│   │   └── main.jsx
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## ⚡ Quick Start & Setup Instructions

### Pre-requisites
- JDK 21+ and Apache Maven 3.8+
- Node.js 18+ and npm
- MySQL Server 8.0+ running on `localhost:3306` (or Docker). The backend defaults to MySQL; configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` if your local settings differ.

---

### 🔑 Pre-seeded Default User Credentials

The application auto-seeds standard roles and test accounts on first launch:

| Role | Email | Password | Details |
| :--- | :--- | :--- | :--- |
| **System Admin** | `admin@airambulance.com` | `admin123` | Full Mission Control Access |
| **Hospital** | `cityhospital@med.org` | `hospital123` | City General Trauma Center |
| **Pilot** | `captain.john@skyrescue.com` | `pilot123` | Captain John Miller (AV-987654) |
| **Patient** | `patient.smith@gmail.com` | `patient123` | Robert Smith (Blood O+) |

---

### 🚀 Running Backend

1. Ensure MySQL database `air_ambulance_db` is created or allowed to auto-create with root credentials (`root` / `root`).
2. Navigate to `backend/`:
   ```bash
   cd backend
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Start Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
5. Backend service will be available at: `http://localhost:8080`
6. Swagger UI API Documentation: `http://localhost:8080/swagger-ui.html`

---

### 💻 Running Frontend

1. Navigate to `frontend/`:
   ```bash
   cd frontend
   ```
2. Install npm dependencies:
   ```bash
   npm install
   ```
3. Launch development server:
   ```bash
   npm run dev
   ```
4. Open browser at: `http://localhost:3000`

---

### 🐳 Running with Docker Compose

To launch the complete application stack (MySQL + Spring Boot + React Frontend):

```bash
docker-compose up --build
```
- Frontend UI: `http://localhost:3000`
- Backend API: `http://localhost:8080`
- MySQL Database: `localhost:3306`
