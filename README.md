# ✈️ Flight Repository System

A **RESTful backend application** built with **Spring Boot** and **Java** that enables users to search flights, manage bookings, and maintain wallet balances. This project demonstrates core backend development concepts including layered architecture, JPA/Hibernate ORM, transaction management, and comprehensive API testing.

---

## ✨ Features

### Current Features

- 🔍 **Flight Search**: Search flights by origin and destination
- 💳 **Wallet Management**: Users have digital wallets for payments
- 🎫 **Flight Booking**: Book flights with automatic wallet deduction
- 📊 **Seat Management**: Real-time seat availability tracking
- ⚡ **Transaction Safety**: All booking operations are atomic using `@Transactional`

### Key Highlights

- **Layered Architecture**: Clean separation of Controller, Service, and Repository layers
- **Spring Data JPA**: No manual SQL queries required for CRUD operations
- **MySQL Integration**: Production-grade relational database
- **RESTful Design**: Follows REST API best practices
- **Error Handling**: Meaningful error messages for all edge cases

---

## 🛠 Tech Stack

| Technology | Purpose |
|------------|---------|
| **Java 17** | Core programming language |
| **Spring Boot 3.x** | Framework for building REST APIs |
| **Spring Data JPA** | ORM and database operations |
| **Hibernate** | JPA implementation |
| **MySQL 8** | Relational database |
| **Maven** | Build tool and dependency management |
| **Postman** | API testing and documentation |

---

## 🏗 Architecture

This project follows **3-tier layered architecture**:

```
┌─────────────────────────────────────┐
│   Controller Layer (REST APIs)      │  ← Handles HTTP requests
├─────────────────────────────────────┤
│   Service Layer (Business Logic)    │  ← Core application logic
├─────────────────────────────────────┤
│   Repository Layer (Data Access)    │  ← Database operations
├─────────────────────────────────────┤
│   Database (MySQL)                  │  ← Persistent storage
└─────────────────────────────────────┘
```

### Design Principles Applied

- **Separation of Concerns**: Each layer has a single responsibility
- **Dependency Injection**: Loose coupling using Spring's `@Autowired`
- **Transaction Management**: Ensures data consistency with `@Transactional`
- **Repository Pattern**: Abstraction over database operations

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.x
- Maven 3.6+
- Postman (for API testing)

### Installation Steps

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/flight-repository-system.git
cd flight-repository-system
```

2. **Create MySQL Database**
```sql
CREATE DATABASE flightdb;
```

3. **Configure Database Connection**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/flightdb
spring.datasource.username=root
spring.datasource.password=your_password
```

4. **Build the Project**
```bash
mvn clean install
```

5. **Run the Application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

6. **Verify Installation**
```bash
curl http://localhost:8080/api/flights
```

---

## 📡 API Endpoints

### Base URL: `http://localhost:8080`

### Flight APIs

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/api/flights` | Get all flights | - |
| `GET` | `/api/flights/{id}` | Get flight by ID | - |
| `GET` | `/api/flights/search?origin={origin}&destination={destination}` | Search flights | - |
| `POST` | `/api/flights` | Add new flight | Flight JSON |
| `DELETE` | `/api/flights/{id}` | Delete flight | - |

### User APIs

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/api/users` | Get all users | - |
| `GET` | `/api/users/{id}` | Get user by ID | - |
| `POST` | `/api/users` | Create new user | User JSON |
| `PUT` | `/api/users/{id}/wallet?amount={amount}` | Top up wallet | - |
| `DELETE` | `/api/users/{id}` | Delete user | - |

### Booking APIs

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/api/bookings` | Get all bookings | - |
| `GET` | `/api/bookings/user/{userId}` | Get user's bookings | - |
| `POST` | `/api/bookings?userId={userId}&flightId={flightId}` | Book a flight | - |
| `PUT` | `/api/bookings/{id}/cancel` | Cancel booking | - |

### Example Requests

**Create User:**
```bash
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "name": "Rahul Sharma",
  "email": "rahul@gmail.com"
}
```

**Add Flight:**
```bash
POST http://localhost:8080/api/flights
Content-Type: application/json

{
  "flightNumber": "AI-202",
  "origin": "Delhi",
  "destination": "Mumbai",
  "departureTime": "2024-06-01T08:00:00",
  "arrivalTime": "2024-06-01T10:00:00",
  "price": 4500.00,
  "availableSeats": 120
}
```

**Book Flight:**
```bash
POST http://localhost:8080/api/bookings?userId=1&flightId=1
```

---

## 🗄 Database Schema

### Entity Relationship Diagram

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│    User     │         │   Booking    │         │   Flight    │
├─────────────┤         ├──────────────┤         ├─────────────┤
│ id (PK)     │◄───────┤ id (PK)      │────────►│ id (PK)     │
│ name        │   1:N   │ user_id (FK) │   N:1   │ flightNumber│
│ email       │         │ flight_id(FK)│         │ origin      │
│ walletBalance│        │ status       │         │ destination │
└─────────────┘         └──────────────┘         │ price       │
                                                  │ availableSeats
                                                  │ departureTime
                                                  │ arrivalTime │
                                                  └─────────────┘
```

### Table Structures

**users**
- `id` (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- `name` (VARCHAR)
- `email` (VARCHAR, UNIQUE)
- `wallet_balance` (DECIMAL)

**flights**
- `id` (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- `flight_number` (VARCHAR)
- `origin` (VARCHAR)
- `destination` (VARCHAR)
- `departure_time` (DATETIME)
- `arrival_time` (DATETIME)
- `price` (DECIMAL)
- `available_seats` (INT)

**bookings**
- `id` (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- `user_id` (BIGINT, FOREIGN KEY → users.id)
- `flight_id` (BIGINT, FOREIGN KEY → flights.id)
- `status` (VARCHAR) - 'CONFIRMED' or 'CANCELLED'

```

**Example Test Scenarios:**
- Get flight by valid ID → Returns flight
- Get flight by invalid ID → Throws exception
- Book flight with sufficient balance → Success
- Book flight with insufficient balance → Error
- Book flight with no seats available → Error

### Testing with Postman

Import the Postman collection from `/postman` folder for ready-to-use API tests.

---

## 🎯 Future Enhancements

Here are planned features to make this project production-ready:

### Phase 1: Security & Authentication
- [ ] **JWT Authentication**: Secure user login with JSON Web Tokens
- [ ] **Spring Security**: Role-based access control (Admin vs User)
- [ ] **Password Encryption**: Use BCrypt for secure password storage
- [ ] **API Rate Limiting**: Prevent abuse

### Phase 2: Enhanced Features
- [ ] **Email Notifications**: Send booking confirmation emails using Spring Mail
- [ ] **Booking History**: Track all past bookings
- [ ] **Flight Ratings & Reviews**: Users can rate flights after travel
- [ ] **Multi-currency Support**: Handle different currencies
- [ ] **Seat Selection**: Let users choose specific seats
- [ ] **Payment Gateway Integration**: Razorpay/Stripe for actual payments

### Phase 3: Performance & Scalability
- [ ] **Pagination**: For flight search results using Spring Data's `Pageable`
- [ ] **Caching**: Redis integration for frequently accessed data
- [ ] **Search Optimization**: Full-text search with Elasticsearch
- [ ] **Booking Queue**: Handle concurrent bookings with message queues

### Phase 4: API Documentation
- [ ] **Swagger/OpenAPI**: Auto-generate interactive API documentation
- [ ] **API Versioning**: Support multiple API versions

### Phase 5: Advanced Features
- [ ] **Flight Recommendations**: ML-based personalized suggestions
- [ ] **Dynamic Pricing**: Price changes based on demand
- [ ] **Loyalty Program**: Reward points for frequent flyers
- [ ] **Refund Management**: Handle cancellations and refunds
- [ ] **Admin Dashboard**: Manage flights, users, and bookings

### Phase 6: DevOps & Deployment
- [ ] **Docker**: Containerize the application
- [ ] **CI/CD Pipeline**: GitHub Actions for automated testing and deployment
- [ ] **Cloud Deployment**: Deploy on AWS/Azure/Heroku
- [ ] **Monitoring**: Application health monitoring with Actuator
- [ ] **Logging**: Centralized logging with ELK stack

---

### Successful Booking Response
```json
{
  "id": 1,
  "user": {
    "id": 1,
    "name": "Rahul Sharma",
    "walletBalance": 5500.0
  },
  "flight": {
    "id": 1,
    "flightNumber": "AI-202",
    "origin": "Delhi",
    "destination": "Mumbai",
    "price": 4500.0,
    "availableSeats": 119
  },
  "status": "CONFIRMED"
}
```

---

## 📚 What I Learned

Through this project, I gained hands-on experience with:

- **Spring Boot**: Building production-grade REST APIs
- **JPA/Hibernate**: ORM mapping and database abstraction
- **Transaction Management**: Ensuring data consistency with `@Transactional`
- **Dependency Injection**: Implementing loose coupling
- **Error Handling**: Meaningful exception handling
- **API Design**: RESTful principles and best practices
- **MySQL**: Relational database design and queries

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Rishav Gupta**

- Email: rishav.mh103@gmail.com

---

**⭐ If you found this project helpful, please give it a star!**

---

**Project Status:** ✅ Active Development | 🎓 Learning Project | 💼 Portfolio Ready

**Last Updated:** February 2024
