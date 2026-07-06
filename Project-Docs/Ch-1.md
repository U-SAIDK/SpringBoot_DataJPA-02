# Chapter 9 — Entity Layer (The Heart of Hibernate)

---

# 📖 Definition

An **Entity** is a **plain Java class (POJO)** that represents a table inside the database.

Each object of an Entity represents **one row** in that table.

Hibernate manages Entity objects automatically.

> **Simple Definition:** An Entity is a Java class that Hibernate maps to a database table.

---

# ❓ Why Do We Need Entities?

Suppose our database has this table:

## employees

| id | first_name | last_name | salary |
| -- | ---------- | --------- | ------ |
| 1  | USAID      | Khan      | 50000  |

Java cannot work directly with database rows.

Java works with **Objects**.

So Hibernate converts

```text
Database Row

↓

Employee Object
```

and

```text
Employee Object

↓

Database Row
```

This conversion is only possible because of **Entities**.

---

# 🚨 Problems Without Entities

Without entities:

* We'd manually map every database row.
* We'd manually create Java objects.
* We'd manually write SQL for every operation.
* We'd manually maintain relationships.

This is exactly what Hibernate saves us from.

---

# 🎯 Important Core Points (AMB)

* Entity = Java Class.
* Table = Database Table.
* Object = One Row.
* Field = One Column.
* Managed by Hibernate.
* Must have a Primary Key (`@Id`).
* Usually requires a no-argument constructor.
* Should represent business data, not API requests.

---

# 🧩 Mental Model

```text
Employee.java

        │

        ▼

Hibernate

        │

        ▼

employees Table

━━━━━━━━━━━━━━━━━━━━━━

Employee Object

        ↔

Database Row
```

Think of an Entity as the **bridge between Java Objects and database tables**.

---

# 🔍 Behind the Scenes

Suppose we write:

```java
Employee employee = new Employee();
employee.setFirstName("USAID");

employeeRepository.save(employee);
```

Internally:

```text
Employee Object

↓

Hibernate checks @Entity

↓

Reads metadata

↓

Generates INSERT SQL

↓

JDBC

↓

PostgreSQL

↓

Employee Row Created
```

The Entity is the starting point for everything Hibernate does.

---

# 🏗️ Our Employee Entity

```java
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    private LocalDateTime createdAt;
}
```

This class tells Hibernate everything it needs to know about the `employees` table.

---

# 📋 How Hibernate Reads This Entity

When the application starts, Hibernate scans the class.

```text
@Entity

↓

"This is a database table."

━━━━━━━━━━━━━━━━━━━━━━

@Table

↓

"The table name is employees."

━━━━━━━━━━━━━━━━━━━━━━

@Id

↓

"This is the Primary Key."

━━━━━━━━━━━━━━━━━━━━━━

@GeneratedValue

↓

"Database generates IDs."

━━━━━━━━━━━━━━━━━━━━━━

@ManyToOne

↓

"This Employee belongs to one Department."

━━━━━━━━━━━━━━━━━━━━━━

@JoinColumn

↓

"Use department_id as the Foreign Key."
```

Hibernate stores this metadata internally and uses it throughout the application's lifetime.

---

# 🏗️ Field Mapping

| Java Field | Database Column                        |
| ---------- | -------------------------------------- |
| id         | id                                     |
| firstName  | first_name *(default naming strategy)* |
| lastName   | last_name                              |
| email      | email                                  |
| salary     | salary                                 |
| department | department_id                          |
| createdAt  | created_at                             |

Hibernate handles this mapping automatically.

---

# 🏗️ Our Department Entity

We also created:

```java
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

}
```

Each `Department` object corresponds to one row in the `departments` table.

---

# 📊 Entity Relationships

Our project contains:

```text
Department

        1

        │

        │

        ▼

Many Employees
```

Which is represented in Java as:

```java
@ManyToOne
private Department department;
```

We'll study relationship annotations in detail in the next chapter.

---

# 🏗️ Entity Lifecycle in Our Project

When we create a new employee:

```java
Employee employee =
EmployeeMapper.toEntity(dto);
```

Current state:

```text
Java Object

(Not yet in Database)
```

After:

```java
employeeRepository.save(employee);
```

State becomes:

```text
Database Row

+

Managed Entity
```

We'll explore Entity Lifecycle (Transient, Persistent, Detached, Removed) in a dedicated chapter.

---

# 🧠 Entity vs DTO

Many beginners confuse these.

| Entity                      | DTO                            |
| --------------------------- | ------------------------------ |
| Represents a database table | Represents API data            |
| Managed by Hibernate        | Not managed by Hibernate       |
| Stored in the database      | Sent over HTTP                 |
| Contains persistence data   | Contains request/response data |

Example:

```text
Client

↓

EmployeeRequestDTO

↓

Mapper

↓

Employee Entity

↓

Database
```

Entities stay inside the persistence layer.

DTOs cross the API boundary.

---

# 💡 Best Practices

✅ One Entity should represent one database table.

✅ Every Entity should have a Primary Key.

✅ Keep Entities focused on persistence.

✅ Use DTOs instead of exposing Entities in API responses.

✅ Use meaningful relationships instead of duplicate fields.

---

# 🎤 Interview Notes

### Q1. What is an Entity?

An Entity is a Java class mapped to a database table using JPA annotations.

---

### Q2. Can an Entity exist without `@Entity`?

No.

Without `@Entity`, Hibernate does not recognize the class as a persistent entity.

---

### Q3. Why does every Entity need `@Id`?

Because Hibernate needs a unique identifier to manage each entity instance.

---

### Q4. Can we return Entities directly from Controllers?

Technically yes.

But in production, it's discouraged.

Use DTOs to keep the API contract separate from the persistence model.

---

### Q5. Who manages Entity objects?

Hibernate manages the lifecycle of entities once they become persistent.

---

# 📝 Things to Remember

✔ Entity = Java representation of a database table.

✔ One object = One database row.

✔ Hibernate manages Entities.

✔ Every Entity needs a Primary Key.

✔ `@Entity` marks the class.

✔ `@Table` specifies the table.

✔ Relationships are defined inside Entities.

✔ DTOs and Entities have different responsibilities.

---

# 🧠 Final Mental Model

```text
Client

        │

        ▼

EmployeeRequestDTO

        │

        ▼

EmployeeMapper

        │

        ▼

Employee Entity

        │

        ▼

Hibernate

        │

        ▼

employees Table
```

> **Golden Rule:**
> **Entities represent how data is stored. DTOs represent how data is exchanged. Never confuse the two.**

---

# 🔗 Chapter Connection

Now that we understand what an Entity is, we can answer a more interesting question:

> **How do two Entities relate to each other?**

In our project:

```text
Department

        │

        ▼

Employees
```

How does Hibernate know this?

How is the foreign key created?

What is `@ManyToOne`?

What does `mappedBy` actually do?

Why do we need `@JoinColumn`?

These questions lead us to the next chapter:

# **Chapter 10 — Entity Relationships**

We'll master:

* `@OneToOne`
* `@OneToMany`
* `@ManyToOne`
* `@ManyToMany`
* `mappedBy`
* `@JoinColumn`
* Owning Side vs Inverse Side
* Foreign Keys
* Relationship Best Practices

These are among the most frequently asked topics in Spring Boot and Hibernate interviews.
