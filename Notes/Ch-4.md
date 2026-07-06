# Chapter 4 — ORM (Object Relational Mapping)

---

# 📖 Definition

**ORM (Object Relational Mapping)** is a programming technique that automatically maps **Java Objects** to **Database Tables** and **Database Rows** back to **Java Objects**.

Instead of writing SQL and manually converting database rows into Java objects, an ORM framework does this work automatically.

> **Simple Definition:** ORM is a bridge that converts Java Objects ↔ Database Tables automatically.

---

# ❓ Why Do We Need ORM?

Let's think about our project.

We have an Employee class.

```java
Employee employee = Employee.builder()
        .firstName("USAID")
        .lastName("Khan")
        .salary(50000)
        .build();
```

But PostgreSQL doesn't understand Java Objects.

It understands tables.

```
employees

-------------------------------------

id

first_name

last_name

salary

department_id

created_at
```

So somebody has to convert

```text
Java Object

↓

Database Row
```

and later

```text
Database Row

↓

Java Object
```

Doing this manually becomes repetitive.

ORM automates this entire process.

---

# 🚨 Problems Before ORM

Suppose you want to save an employee.

Using JDBC you must:

* Open Connection
* Write INSERT SQL
* Create PreparedStatement
* Set Parameters
* Execute SQL
* Handle Exceptions
* Close Connection

Now imagine doing this for

* Save
* Update
* Delete
* Search
* Pagination
* Relationships

Every project.

Thousands of lines.

Very repetitive.

---

# ORM Solves This

Instead of writing

```java
PreparedStatement statement =
connection.prepareStatement(...);
```

we simply write

```java
employeeRepository.save(employee);
```

ORM takes care of everything.

---

# 🎯 Important Core Points (AMB)

* ORM maps Java Objects to Database Tables.
* ORM maps Database Rows to Java Objects.
* Developers work with Objects instead of SQL.
* ORM reduces boilerplate code.
* Hibernate is the ORM framework used in our project.
* JPA defines how ORM should work.

---

# ⚙️ What Does "Mapping" Mean?

Mapping simply means

```
Employee Object

↓

employees Table
```

Example

Java Object

```java
Employee

id = 1

firstName = "USAID"

salary = 50000
```

Database Row

```
employees

----------------------------------

id = 1

first_name = USAID

salary = 50000
```

ORM automatically connects both worlds.

---

# 🏗️ Mapping in Our Project

Our Employee Entity

```java
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    private Long id;

    private String firstName;

    private Double salary;

}
```

Hibernate understands

```
Employee Class

↓

employees Table
```

Then

```
firstName

↓

first_name column

salary

↓

salary column
```

No manual conversion needed.

---

# 📊 ORM Visualization

Without ORM

```
Java Object

↓

Write SQL

↓

Execute SQL

↓

Read ResultSet

↓

Create Java Object

↓

Return Object
```

---

With ORM

```
Java Object

↓

Hibernate

↓

Database
```

Everything in the middle is automatic.

---

# ⚙️ Internal Working

Suppose we execute

```java
employeeRepository.save(employee);
```

Internally

```
Employee Object

↓

Hibernate

↓

Reads @Entity

↓

Reads @Table

↓

Reads @Column

↓

Generates INSERT SQL

↓

JDBC

↓

PostgreSQL

↓

Row Created
```

Notice

We never wrote SQL.

We never created a PreparedStatement.

Hibernate did it.

---

# Reading Data

Suppose

```java
employeeRepository.findById(1L);
```

Internally

```
PostgreSQL

↓

SELECT *

↓

ResultSet

↓

Hibernate

↓

Creates Employee Object

↓

Returns Employee
```

Again,

no manual mapping.

---

# 🏗️ Our Project Example

Suppose our database contains

```
employees

---------------------------------

1

USAID

Khan

50000
```

When Hibernate executes

```java
employeeRepository.findById(1L);
```

It automatically creates

```java
Employee employee =
Employee.builder()
        .id(1L)
        .firstName("USAID")
        .lastName("Khan")
        .salary(50000)
        .build();
```

We never write this ourselves.

Hibernate builds the object.

---

# 📋 Object ↔ Table Mapping

```
Java

Employee

↓

employees Table

━━━━━━━━━━━━━━━━━━━━━━

id

↓

id

━━━━━━━━━━━━━━━━━━━━━━

firstName

↓

first_name

━━━━━━━━━━━━━━━━━━━━━━

salary

↓

salary

━━━━━━━━━━━━━━━━━━━━━━

department

↓

department_id
```

This mapping is the heart of ORM.

---

# 🧠 Why ORM Became Popular

Without ORM

Every project required

* SQL
* JDBC
* ResultSet Mapping
* Object Creation
* Manual Updates

Huge amount of repetitive work.

With ORM

Developers simply work with Java Objects.

The ORM framework handles database communication.

---

# 📊 Our Complete Flow

```
Employee Object

↓

EmployeeRepository

↓

Hibernate ORM

↓

SQL

↓

JDBC

↓

PostgreSQL

↓

Rows

↓

Hibernate ORM

↓

Employee Object
```

Notice

ORM sits between

Java

and

Database.

---

# 💡 Real-Life Analogy

Imagine an interpreter.

```
English Speaker

↓

Interpreter

↓

Japanese Speaker
```

Neither person understands the other's language.

The interpreter translates.

ORM works exactly like that.

```
Java Objects

↓

ORM

↓

Database Tables
```

Java speaks Objects.

Database speaks Tables.

ORM translates both ways.

---

# 💡 Best Practices

✅ Think in terms of Java Objects, not database rows.

✅ Let Hibernate handle object mapping.

✅ Design clean entity classes.

✅ Avoid writing SQL when a JPA operation is sufficient.

---

# 🎤 Interview Notes

### Q1. What is ORM?

ORM (Object Relational Mapping) is a technique that automatically maps Java Objects to Database Tables and vice versa.

---

### Q2. What problem does ORM solve?

It removes repetitive JDBC code and automates object-to-table mapping.

---

### Q3. Is ORM a framework?

No.

ORM is a concept or technique.

Hibernate is an ORM framework.

---

### Q4. Which ORM framework are we using?

Hibernate.

---

### Q5. Does ORM eliminate SQL?

No.

ORM generates SQL automatically.

The database still executes SQL.

---

# 📝 Things to Remember

✔ ORM = Object Relational Mapping.

✔ ORM maps Java Objects to Database Tables.

✔ ORM maps Database Rows back to Java Objects.

✔ Hibernate is an ORM Framework.

✔ ORM sits between Java and the Database.

✔ ORM greatly reduces boilerplate JDBC code.

✔ Spring Data JPA builds on top of Hibernate.

---

# 🔗 Chapter Connection

Our journey so far:

```
Database

↓

SQL

↓

JDBC

↓

ORM
```

Now we know **how object mapping works**.

The next question is:

> **Who defines the standard rules for ORM in Java?**

Different ORM frameworks could behave differently, creating inconsistency.

To solve this, Java introduced a common specification.

That specification is called **JPA (Java Persistence API)**.

In the next chapter, we'll learn:

* What JPA actually is.
* Why JPA is **not** Hibernate.
* Why Hibernate implements JPA.
* Why Spring Data JPA is built on top of both.
