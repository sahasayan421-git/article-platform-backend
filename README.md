# 🚀 Article Publishing Platform - Backend

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen" />
  <img src="https://img.shields.io/badge/Spring%20Security-JWT-success" />
  <img src="https://img.shields.io/badge/Database-PostgreSQL-blue" />
  <img src="https://img.shields.io/badge/Flyway-Migrations-red" />
  <img src="https://img.shields.io/badge/Swagger-OpenAPI-green" />
  <img src="https://img.shields.io/badge/Build-Maven-C71A36" />
  <img src="https://img.shields.io/badge/Tests-JUnit5%20%7C%20Mockito-yellow" />
</p>

A scalable and production-ready RESTful backend for a modern **Article Publishing Platform** built using **Spring Boot**.

This backend powers features like secure authentication, article publishing, nested comments, mentions, likes, caching, database optimizations, Swagger/OpenAPI documentation, and comprehensive testing.

Designed with clean architecture principles and scalable backend engineering practices.

---

# ✨ Features

## 🔐 Authentication & Security

* JWT-based Authentication
* User Registration & Login
* BCrypt Password Encryption
* Spring Security Integration
* Custom JWT Authentication Filter

---

## 📰 Article Management

* Create Articles
* Update Articles
* Fetch Articles with Pagination & Sorting
* Fetch Single Article Details
* Author Information Mapping

---

## 💬 Advanced Comment System

* Nested / Threaded Comments
* Recursive Comment Tree Responses
* Add & Delete Comments
* Mention Parsing (`@username`)
* DTO-based Response Mapping

---

## ❤️ Like System

### Article Likes

* Like Articles
* Prevent Duplicate Likes
* Fetch Like Counts

### Comment Likes

* Like Comments
* User Like Tracking

---

## 👥 User Features

* User Search APIs
* Mention Suggestions
* Mention Response DTOs

---

## ⚡ Performance Optimizations

* Database Indexing
* Partitioned Tables
* Query Optimization
* Spring Cache Support
* Flyway Migration Versioning

---

## 📘 API Documentation

* Swagger/OpenAPI Integration
* Interactive API Testing
* API Endpoint Documentation

---

## 🛡️ Exception Handling

* Global Exception Handling
* Structured API Error Responses
* Custom ResourceNotFoundException

---

## 🧪 Comprehensive Testing

Implemented using:

* JUnit 5
* Mockito
* Spring Boot Test

Covered Layers:

* Controllers
* Services
* Repository
* Security
* Utilities
* Exception Handling

---

# 🏗️ Architecture

The project follows a layered architecture approach:

```text
Client
   ↓
Controller Layer
   ↓
Service Layer
   ↓
Repository Layer
   ↓
Database
```

Additional Supporting Layers:

* DTO Layer
* Mapper Layer
* Security Layer
* Utility Layer
* Exception Layer

---

# 🛠️ Tech Stack

| Category          | Technology                  |
| ----------------- | --------------------------- |
| Language          | Java 17                     |
| Framework         | Spring Boot                 |
| Security          | Spring Security + JWT       |
| ORM               | Spring Data JPA + Hibernate |
| Database          | PostgreSQL                  |
| Migration Tool    | Flyway                      |
| API Documentation | Swagger/OpenAPI             |
| Build Tool        | Maven                       |
| Testing           | JUnit 5 + Mockito           |
| Logging           | Logback                     |
| Caching           | Spring Cache                |

---

# 📂 Project Structure

```text
src/main/java/com/sayan/article_platform
│
├── config          # Application & datasource configuration
├── controller      # REST API controllers
├── dto             # Request & response DTOs
├── entity          # JPA entities
├── exception       # Global exception handling
├── mapper          # DTO mapping layer
├── repository      # Spring Data repositories
├── security        # JWT & Spring Security configuration
├── service         # Business logic layer
└── util            # Utility classes
```

---

# 🗄️ Database Migrations

Database schema management is handled using **Flyway**.

Migration files are located in:

```text
src/main/resources/db/migration
```

Implemented migrations include:

* Database Extensions
* Table Creation
* Index Creation
* Triggers & Functions
* Partitioned Tables
* Partition Indexes

---

# 🗄️ Local Database Setup (PostgreSQL + DBeaver)

This project uses **PostgreSQL** as the primary database.

The following steps explain how to install PostgreSQL, configure the database locally, and connect it using DBeaver.

---

# 1️⃣ Install PostgreSQL

Download PostgreSQL from the official website:

https://www.postgresql.org/download/

During installation:

* Select default options
* Remember the password set for the `postgres` user
* Default port:

```text
5432
```

After installation:

* PostgreSQL server starts automatically
* pgAdmin may also get installed

---

# 2️⃣ Verify PostgreSQL Installation

Open terminal / command prompt:

```bash
psql --version
```

Expected output:

```text
psql (PostgreSQL) 17.x
```

---

# 3️⃣ Install DBeaver

Download DBeaver Community Edition:

https://dbeaver.io/download/

DBeaver is used for visually managing PostgreSQL databases.

---

# 4️⃣ Create Database

Open SQL Shell (`psql`) or pgAdmin.

Login using:

* Username: `postgres`
* Password: your installation password

Create database:

```sql
CREATE DATABASE article_platform;
```

Verify database:

```sql
\l
```

You should now see:

```text
article_platform
```

---

# 5️⃣ Configure Database in DBeaver

## Open DBeaver

Navigate to:

```text
Database → New Database Connection
```

Select:

```text
PostgreSQL
```

Click:

```text
Next
```

---

## Configure Connection

Fill the following values:

| Field    | Value            |
| -------- | ---------------- |
| Host     | localhost        |
| Port     | 5432             |
| Database | article_platform |
| Username | postgres         |
| Password | your_password    |

Click:

```text
Test Connection
```

If prompted:

* Download PostgreSQL drivers
* Accept defaults

Click:

```text
Finish
```

Now PostgreSQL is connected successfully in DBeaver.

---

# 6️⃣ Configure Spring Boot Database Properties

Update:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/article_platform
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# 7️⃣ Run Flyway Migrations

This project uses Flyway for database migrations.

Migration files are located in:

```text
src/main/resources/db/migration
```

When the application starts:

* Flyway automatically creates tables
* Creates indexes
* Creates triggers/functions
* Creates partitioned tables

No manual SQL execution is required.

---

# 8️⃣ Run the Application

## Linux / Mac

```bash
./mvnw spring-boot:run
```

## Windows

```bash
mvnw.cmd spring-boot:run
```

Application starts at:

```text
http://localhost:8080
```

---

# 9️⃣ Access Swagger/OpenAPI Documentation

After starting the application:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI documentation:

```text
http://localhost:8080/v3/api-docs
```

---

# 🔐 Authentication Flow

```text
Register/Login
      ↓
Generate JWT Token
      ↓
Client sends JWT in Authorization Header
      ↓
JWT Filter validates request
      ↓
Access Protected APIs
```

Example:

```http
Authorization: Bearer your_jwt_token
```

---

# 📌 API Endpoints

# 🔑 Authentication APIs

| Method | Endpoint             | Description   |
| ------ | -------------------- | ------------- |
| POST   | `/api/auth/register` | Register User |
| POST   | `/api/auth/login`    | Login User    |

---

# 📰 Article APIs

| Method | Endpoint             | Description         |
| ------ | -------------------- | ------------------- |
| GET    | `/api/articles`      | Fetch All Articles  |
| GET    | `/api/articles/{id}` | Fetch Article By ID |
| POST   | `/api/articles`      | Create Article      |
| PUT    | `/api/articles/{id}` | Update Article      |
| DELETE | `/api/articles/{id}` | Delete Article      |

---

# 💬 Comment APIs

| Method | Endpoint                            | Description            |
| ------ | ----------------------------------- | ---------------------- |
| POST   | `/api/comments`                     | Create Comment         |
| GET    | `/api/comments/article/{articleId}` | Fetch Article Comments |
| DELETE | `/api/comments/{id}`                | Delete Comment         |

---

# ❤️ Like APIs

| Method | Endpoint                         | Description       |
| ------ | -------------------------------- | ----------------- |
| POST   | `/api/likes/article/{articleId}` | Like Article      |
| GET    | `/api/likes/article/{articleId}` | Get Article Likes |

---

# 👤 User APIs

| Method | Endpoint              | Description         |
| ------ | --------------------- | ------------------- |
| GET    | `/api/users/search`   | Search Users        |
| GET    | `/api/users/mentions` | Mention Suggestions |

---

# 🧪 Run Tests

```bash
mvn test
```

---

# 📊 Implemented Engineering Concepts

* Layered Architecture
* DTO Pattern
* JWT Authentication
* Spring Security
* Global Exception Handling
* Flyway Migration
* Database Partitioning
* Spring Cache
* Nested Comment Trees
* Mention Parsing
* Unit Testing
* Pagination & Sorting

---

# 🔥 Highlights

## ✅ Nested Comment Tree

Supports hierarchical threaded discussions.

## ✅ Mention Parsing

Automatically extracts and processes `@mentions`.

## ✅ Duplicate Like Prevention

Database-level protection against duplicate likes.

## ✅ Database Partitioning

Optimized handling for large-scale datasets.

## ✅ Clean Architecture

Well-separated layers for maintainability and scalability.

---

# 📈 Future Enhancements

* Refresh Token Support
* Docker Support
* Kubernetes Deployment
* Media Upload Support
* Notification System
* Elasticsearch Integration
* CI/CD Pipeline
* Rate Limiting
* Email Verification & Password Reset
* Real-time Notifications using WebSockets

---

# 🤝 Contributing

Contributions are welcome.

## Steps

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push changes
5. Create a Pull Request

---

# 👨‍💻 Author

## Sayan Saha

Backend Developer passionate about scalable Java & Spring Boot applications.

### GitHub

https://github.com/sahasayan421-git

---
