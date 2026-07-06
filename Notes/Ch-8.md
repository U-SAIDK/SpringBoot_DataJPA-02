# Chapter 8 — SpringBoot-JPA Project Architecture

---

# 📖 Definition

Our **SpringBoot-JPA** project follows a **Layered Architecture**.

Instead of placing all the code inside one class, we divide responsibilities into separate layers.

Each layer has **one specific responsibility**.

This makes the application:

* Easier to understand
* Easier to maintain
* Easier to test
* Easier to scale

> **Simple Definition:** Layered Architecture means every layer has one job and communicates only with the layer directly below it.

---

# ❓ Why Do We Need Layered Architecture?

Imagine writing everything inside one controller.

```java
@RestController
public class EmployeeController {

    // Validation

    // Business Logic

    // Database Queries

    // Exception Handling

    // JSON Mapping

}
```

This works for small demos.

But imagine the application grows to:

* 50 APIs
* 200 Services
* 100 Entities
* 300 Repository Methods

It becomes impossible to maintain.

---

Layered Architecture solves this.

```text
Controller

↓

Service

↓

Repository

↓

Database
```

Every layer has exactly one responsibility.

---

# 🚨 Problems Without Layered Architecture

Without layers:

❌ Duplicate Code

❌ Hard to Debug

❌ Hard to Test

❌ Hard to Reuse

❌ Huge Classes

❌ Tight Coupling

---

# 🎯 Important Core Points (AMB)

* One layer = One responsibility.
* Controllers never access the database directly.
* Services contain business logic.
* Repositories only perform database operations.
* DTOs are used for API communication.
* Entities represent database tables.
* Mappers convert DTOs ↔ Entities.

---

# 🧩 Mental Model

```text
HTTP Request

        │

        ▼

Controller

        │

        ▼

Service

        │

        ▼

Repository

        │

        ▼

Hibernate

        │

        ▼

PostgreSQL

━━━━━━━━━━━━━━━━━━━━━━

Response

        ▲

        │

DTO

        ▲

        │

Mapper

        ▲

        │

Entity
```

This is our complete application.

---

# 📂 Our Project Structure

```text
springbootjpa

│

├── controller

├── dto

├── entity

├── repository

├── service

├── util

├── exception

└── SpringBootJpaApplication
```

Every package exists for a reason.

---

# 🏗️ Layer Responsibilities

## 1. Entity Layer

Represents database tables.

Example

```java
Employee

Department
```

Annotations

```java
@Entity

@Table

@Id
```

Entity objects are managed by Hibernate.

---

## 2. DTO Layer

Used for communication with clients.

Example

```java
EmployeeRequestDTO

EmployeeResponseDTO

DepartmentRequestDTO

DepartmentResponseDTO
```

Clients never interact with entities directly.

---

## 3. Mapper Layer

Converts

```text
DTO

↓

Entity

━━━━━━━━━━━━━━

Entity

↓

DTO
```

Our project contains

```text
EmployeeMapper

DepartmentMapper
```

---

## 4. Repository Layer

Responsible only for database operations.

Example

```java
EmployeeRepository

DepartmentRepository
```

Extends

```java
JpaRepository
```

No business logic belongs here.

---

## 5. Service Layer

The brain of the application.

Responsibilities

* Business Logic
* Validation
* Calling Repository
* Calling Mapper
* Throwing Exceptions

Examples

```java
EmployeeServiceImpl

DepartmentServiceImpl
```

---

## 6. Controller Layer

Receives HTTP requests.

Returns HTTP responses.

Examples

```java
GET /employees

POST /employees

PUT /employees/{id}

DELETE /employees/{id}
```

The controller should remain thin.

---

## 7. Exception Layer

Handles errors.

Instead of returning stack traces,

we return clean JSON responses.

Example

```text
Employee not found with ID: 5
```

---

# 🔍 Behind the Scenes

Suppose the client creates an employee.

```http
POST /api/employees
```

JSON

↓

Controller

↓

EmployeeRequestDTO

↓

Service

↓

EmployeeMapper

↓

Employee Entity

↓

Repository

↓

Spring Data JPA

↓

Hibernate

↓

JDBC

↓

PostgreSQL

↓

Employee Saved

↓

EmployeeResponseDTO

↓

JSON Response

Everything flows through layers.

---

# 📊 Complete Request Lifecycle

```text
Client

↓

HTTP Request

↓

Controller

↓

Request DTO

↓

Service

↓

Mapper

↓

Entity

↓

Repository

↓

Spring Data JPA

↓

Hibernate

↓

JDBC

↓

PostgreSQL

━━━━━━━━━━━━━━━━━━━━━━

Database Row

↓

Hibernate

↓

Entity

↓

Mapper

↓

Response DTO

↓

Controller

↓

JSON

↓

Client
```

This is probably the most important diagram in the entire project.

---

# 🏗️ Example from Our Project

When we call

```http
POST /api/employees
```

Our application executes approximately this flow.

```text
EmployeeController

↓

createEmployee()

↓

EmployeeServiceImpl

↓

EmployeeMapper.toEntity()

↓

EmployeeRepository.save()

↓

Spring Data JPA

↓

Hibernate

↓

PostgreSQL

↓

Employee Saved

↓

EmployeeMapper.toDTO()

↓

JSON Response
```

Exactly what we've implemented.

---

# 📋 Package Summary

| Package    | Responsibility    |
| ---------- | ----------------- |
| entity     | Database Tables   |
| dto        | API Communication |
| util       | Mapper Classes    |
| repository | Database Access   |
| service    | Business Logic    |
| controller | REST Endpoints    |
| exception  | Error Handling    |

Remember this table.

It's a common interview question.

---

# 💡 Best Practices

✅ Keep Controllers thin.

✅ Put business logic inside Services.

✅ Never expose Entities directly.

✅ Use DTOs for requests and responses.

✅ Keep Repositories focused only on persistence.

✅ Use Mappers for conversion.

---

# 🎤 Interview Notes

### Q1. Why do we use Layered Architecture?

To separate responsibilities, improve maintainability, scalability, testing, and readability.

---

### Q2. Where should business logic be written?

Inside the Service layer.

---

### Q3. Should Controllers access Repositories directly?

No.

Controllers should call Services.

---

### Q4. Why do we use DTOs?

To avoid exposing Entity classes directly and to control the API contract.

---

### Q5. Why do we use Mapper classes?

To separate conversion logic from business logic and keep Services clean.

---

# 📝 Things to Remember

✔ One layer = One responsibility.

✔ Controller → HTTP.

✔ Service → Business Logic.

✔ Repository → Database.

✔ Entity → Database Table.

✔ DTO → API Object.

✔ Mapper → Conversion.

✔ Exception → Error Response.

---

# 🧠 Final Mental Model

```text
                 CLIENT

                    │

                    ▼

            EmployeeController

                    │

                    ▼

          EmployeeServiceImpl

                    │

                    ▼

            EmployeeMapper

                    │

                    ▼

                Employee

                    │

                    ▼

          EmployeeRepository

                    │

                    ▼

           Spring Data JPA

                    │

                    ▼

              Hibernate

                    │

                    ▼

                 JDBC

                    │

                    ▼

              PostgreSQL
```

> **Golden Rule:** Every layer has one responsibility. When each layer focuses on its own job, the application stays clean, maintainable, and easy to extend.

---

# 🔗 Chapter Connection

We now understand the **big picture** of our application.

Starting from the next chapter, we'll zoom in and master each layer individually.

Our roadmap is:

```text
Chapter 9  → Entity Layer
Chapter 10 → Relationships
Chapter 11 → DTO Layer
Chapter 12 → Mapper Layer
Chapter 13 → Repository Layer
Chapter 14 → Service Layer
Chapter 15 → Controller Layer
Chapter 16 → Exception Handling
```

By the end of these chapters, you'll not only know **how our project works**—you'll also understand **why it was designed this way**, enabling you to build clean Spring Boot applications from scratch with confidence.
