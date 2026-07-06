# Chapter 2 — SQL (Structured Query Language)

---

# 📖 Definition

**SQL (Structured Query Language)** is the standard language used to communicate with a Relational Database.

Using SQL, we can:

* Store data
* Retrieve data
* Update data
* Delete data
* Create tables
* Modify tables
* Manage relationships

> **Simple Definition:** SQL is the language that databases understand.

---

# ❓ Why Do We Need SQL?

Imagine you have a PostgreSQL database.

It does **not understand Java**.

For example, PostgreSQL cannot understand:

```java
Employee employee = new Employee(
    "USAID",
    "Khan",
    50000
);
```

It only understands SQL commands like:

```sql
INSERT INTO employees(first_name,last_name,salary)
VALUES ('USAID','Khan',50000);
```

So there must always be a way to convert Java objects into SQL.

That is exactly what Hibernate will do for us later.

---

# 🚨 Problem SQL Solves

Without SQL...

```text
Java Application

↓

???

↓

Database
```

The database would have no idea:

* What to save
* What to update
* What to delete
* What to search

SQL became the universal language understood by almost every relational database.

Examples:

* PostgreSQL
* MySQL
* Oracle
* SQL Server
* MariaDB

All of them understand SQL (with some database-specific features).

---

# 🎯 Important Core Points (AMB)

* SQL is the standard language for relational databases.
* Every CRUD operation is ultimately performed using SQL.
* Hibernate generates SQL automatically.
* Spring Data JPA hides SQL most of the time, but SQL still executes behind the scenes.
* Understanding basic SQL is essential for every backend developer.

---

# ⚙️ The Four Most Important SQL Operations (CRUD)

Everything we do in our Spring Boot project eventually becomes one of these four operations.

---

## 1. CREATE → INSERT

Adds a new record.

```sql
INSERT INTO employees
(first_name,last_name,email,salary)
VALUES
('USAID','Khan','usaid@gmail.com',50000);
```

Equivalent in our project:

```java
employeeRepository.save(employee);
```

Hibernate generates the `INSERT` statement automatically.

---

## 2. READ → SELECT

Retrieves data.

```sql
SELECT *
FROM employees;
```

Equivalent:

```java
employeeRepository.findAll();
```

or

```java
employeeRepository.findById(id);
```

---

## 3. UPDATE

Updates existing data.

```sql
UPDATE employees
SET salary=70000
WHERE id=1;
```

Equivalent:

```java
employeeRepository.save(employee);
```

Notice something interesting:

Spring Data JPA uses the same `save()` method for both **Insert** and **Update**.

We'll learn why later when we study the Entity Lifecycle.

---

## 4. DELETE

Deletes data.

```sql
DELETE
FROM employees
WHERE id=1;
```

Equivalent:

```java
employeeRepository.delete(employee);
```

or

```java
employeeRepository.deleteById(id);
```

---

# 📊 CRUD Mapping

```text
CREATE

↓

INSERT

↓

save()

━━━━━━━━━━━━━━━━━━━━━━

READ

↓

SELECT

↓

findById()

findAll()

━━━━━━━━━━━━━━━━━━━━━━

UPDATE

↓

UPDATE

↓

save()

━━━━━━━━━━━━━━━━━━━━━━

DELETE

↓

DELETE

↓

delete()

deleteById()
```

This is one of the most important mappings to remember.

---

# 🏗️ Our Project Example

Suppose the client sends:

```http
POST /api/employees
```

JSON

```json
{
  "firstName":"USAID",
  "lastName":"Khan",
  "salary":50000
}
```

Inside the Service:

```java
employeeRepository.save(employee);
```

Hibernate internally generates SQL similar to:

```sql
INSERT INTO employees
(first_name,last_name,salary)
VALUES
('USAID','Khan',50000);
```

Notice:

We never wrote SQL ourselves.

Hibernate did.

---

Now suppose:

```http
GET /api/employees
```

Our code:

```java
employeeRepository.findAll();
```

Hibernate generates:

```sql
SELECT *
FROM employees;
```

Again,

no SQL written by us.

---

# ⚙️ Internal Working

When we write:

```java
employeeRepository.findAll();
```

Spring Data JPA

↓

calls Hibernate

↓

Hibernate creates SQL

↓

SQL sent through JDBC

↓

PostgreSQL executes SQL

↓

Rows returned

↓

Hibernate converts rows into Java Objects

↓

Spring returns JSON

---

Flow

```text
findAll()

↓

Hibernate

↓

SELECT *

FROM employees

↓

PostgreSQL

↓

Rows

↓

Employee Objects

↓

JSON
```

---

# 📋 SQL in Our Project

Our application mainly works with two tables.

```text
employees
```

and

```text
departments
```

Typical SQL Hibernate generates:

Save Employee

```sql
INSERT INTO employees ...
```

---

Find Employee

```sql
SELECT *
FROM employees;
```

---

Update Employee

```sql
UPDATE employees
SET salary=...
WHERE id=?;
```

---

Delete Employee

```sql
DELETE
FROM employees
WHERE id=?;
```

---

# 🧠 Important Observation

Even though we never wrote SQL,

SQL **always** executes.

Spring Data JPA does **not eliminate SQL**.

It simply writes SQL for us.

Think of it like this:

```text
Java

↓

Hibernate

↓

SQL

↓

Database
```

No matter what,

the database always receives SQL.

---

# 💡 SQL vs Spring Data JPA

Without Spring Data JPA

```java
Write SQL

↓

Execute SQL

↓

Read ResultSet

↓

Convert Rows

↓

Create Objects
```

With Spring Data JPA

```java
employeeRepository.findAll();
```

Done.

Spring and Hibernate handle everything else.

---

# 📊 Our Project Flow

```text
POST Request

↓

Controller

↓

Service

↓

Repository

↓

Hibernate

↓

INSERT SQL

↓

PostgreSQL

━━━━━━━━━━━━━━━━━━━━━━

GET Request

↓

Controller

↓

Service

↓

Repository

↓

SELECT SQL

↓

PostgreSQL
```

---

# 💡 Best Practices

✅ Learn basic SQL even if using Spring Data JPA.

✅ Never assume JPA replaces SQL—it generates SQL.

✅ Enable SQL logging while learning to see what Hibernate produces.

✅ Understand CRUD SQL before learning advanced JPA features.

---

# 🎤 Interview Notes

### Q1. What is SQL?

SQL is the standard language used to communicate with relational databases.

---

### Q2. Does Hibernate remove SQL?

No.

Hibernate generates SQL automatically.

---

### Q3. Does Spring Data JPA eliminate SQL?

No.

It only hides SQL from the developer.

The database still executes SQL.

---

### Q4. Which SQL command is used for CRUD?

| CRUD   | SQL    |
| ------ | ------ |
| Create | INSERT |
| Read   | SELECT |
| Update | UPDATE |
| Delete | DELETE |

---

### Q5. Why should a Spring Boot developer learn SQL?

Because every JPA operation eventually becomes SQL, and understanding SQL helps debug performance issues, optimize queries, and interpret Hibernate logs.

---

# 📝 Things to Remember

✔ SQL is the language of relational databases.

✔ PostgreSQL understands SQL, not Java.

✔ Every JPA operation eventually becomes SQL.

✔ Hibernate automatically generates SQL.

✔ `save()` can generate `INSERT` or `UPDATE` depending on the entity state.

✔ `findAll()` generates `SELECT`.

✔ `delete()` generates `DELETE`.

---

# 🔗 Chapter Connection

We now know:

* Where data is stored → **Database**
* How databases communicate → **SQL**

The next question is:

> **How does a Java application send SQL commands to a database?**

The answer is **JDBC (Java Database Connectivity).**

JDBC is the bridge between Java applications and relational databases, and understanding it will help you appreciate why Hibernate and Spring Data JPA were created.
