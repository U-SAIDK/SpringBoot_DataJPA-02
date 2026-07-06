# Chapter 3 — JDBC (Java Database Connectivity)

---

# 📖 Definition

**JDBC (Java Database Connectivity)** is the standard Java API that allows Java applications to communicate with relational databases.

It acts as a **bridge** between Java code and the database.

> **Simple Definition:** JDBC is the bridge that allows Java applications to send SQL commands to databases.

---

# ❓ Why Do We Need JDBC?

Imagine we have this Java code.

```java
Employee employee = new Employee(
        "USAID",
        "Khan",
        50000
);
```

Can PostgreSQL understand Java Objects?

❌ No.

It only understands SQL.

So there must be something that converts Java requests into database communication.

That "something" is **JDBC**.

---

# The Problem Before JDBC

Without JDBC

```text
Java Application

        │

        ▼

???????????

        │

        ▼

PostgreSQL
```

Java had no standard way to communicate with databases.

Every database vendor had its own method.

Very difficult.

Very inconsistent.

---

JDBC solved this by providing one common API.

```text
Java Application

        │

        ▼

JDBC API

        │

        ▼

Database Driver

        │

        ▼

PostgreSQL
```

Now every Java application talks to JDBC.

JDBC handles the rest.

---

# 🎯 Important Core Points (AMB)

* JDBC is a Java API.
* JDBC sends SQL commands to databases.
* JDBC requires a database driver.
* Hibernate internally uses JDBC.
* Spring Data JPA also uses JDBC internally.
* We rarely write JDBC code manually in modern Spring Boot applications.

---

# ⚙️ What is a JDBC Driver?

JDBC itself does not know how to communicate with PostgreSQL.

It needs help.

That helper is called a **Database Driver**.

Think of the driver as a translator.

```text
Java

↓

JDBC

↓

PostgreSQL Driver

↓

PostgreSQL
```

For MySQL,

we use the MySQL Driver.

For PostgreSQL,

we use the PostgreSQL Driver.

---

# 🏗️ Our Project Example

Look at our dependencies.

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

Why did we add this?

Because JDBC needs a PostgreSQL driver.

Without it,

our application cannot communicate with PostgreSQL.

---

# ⚙️ How JDBC Works Internally

Suppose we want to save an employee.

The actual flow looks like this.

```text
Spring Boot

↓

Hibernate

↓

JDBC

↓

PostgreSQL Driver

↓

PostgreSQL

↓

Employee Saved
```

Notice something.

Even though we use Spring Data JPA,

JDBC is still involved.

It never disappears.

---

# 📊 Manual JDBC Example

Before Hibernate,

developers had to write code like this.

```java
Connection connection =
DriverManager.getConnection(url, username, password);

PreparedStatement statement =
connection.prepareStatement(
"INSERT INTO employees(first_name,last_name,salary) VALUES(?,?,?)"
);

statement.setString(1, "USAID");
statement.setString(2, "Khan");
statement.setDouble(3, 50000);

statement.executeUpdate();

connection.close();
```

---

# 😨 Problems with Manual JDBC

Look at that code.

For one simple INSERT we had to:

* Create a connection.
* Write SQL manually.
* Create a PreparedStatement.
* Set every parameter.
* Execute SQL.
* Handle exceptions.
* Close resources.

Now imagine writing this for:

* Save
* Update
* Delete
* Find
* Pagination
* Sorting
* Relationships

Thousands of lines of repetitive code.

---

# 📊 Visual Comparison

Without Hibernate

```text
Write SQL

↓

Open Connection

↓

Create Statement

↓

Set Parameters

↓

Execute SQL

↓

Read ResultSet

↓

Create Objects

↓

Close Connection
```

A lot of work.

---

With Spring Data JPA

```java
employeeRepository.save(employee);
```

Done.

---

# 🧠 What Happens Inside save()?

When we write

```java
employeeRepository.save(employee);
```

Spring Data JPA does **not** directly save the object.

Internally,

something similar happens.

```text
save()

↓

Hibernate

↓

Generate SQL

↓

JDBC

↓

Driver

↓

PostgreSQL

↓

Success
```

Hibernate generates SQL.

JDBC sends SQL.

Database executes SQL.

---

# 📋 JDBC Components

A typical JDBC program uses:

## Driver

Responsible for talking to the database.

---

## Connection

Represents the connection between Java and the database.

---

## Statement / PreparedStatement

Used to execute SQL.

PreparedStatement is preferred because it is:

* Faster
* Safer
* Prevents SQL Injection

---

## ResultSet

Stores rows returned from a SELECT query.

Hibernate later converts these rows into Java Objects automatically.

---

# 🏗️ Our Project Flow

When we execute

```java
employeeRepository.findAll();
```

Internally

```text
EmployeeRepository

↓

JpaRepository

↓

Hibernate

↓

Generate SQL

↓

JDBC

↓

PreparedStatement

↓

PostgreSQL

↓

ResultSet

↓

Hibernate

↓

Employee Objects

↓

JSON Response
```

Notice that JDBC is hidden from us,

but it is still doing the communication.

---

# 💡 Why Hibernate Was Created

Developers became tired of writing JDBC code.

Every project required:

* Connection handling
* SQL writing
* ResultSet mapping
* Resource cleanup

Hibernate solved these problems.

Instead of writing:

```java
PreparedStatement...
```

we simply write:

```java
employeeRepository.save(employee);
```

Much cleaner.

Much more productive.

---

# 📊 JDBC in the Big Picture

```text
Java

↓

JDBC

↓

Database
```

After Hibernate

```text
Java

↓

Hibernate

↓

JDBC

↓

Database
```

After Spring Data JPA

```text
Java

↓

Spring Data JPA

↓

Hibernate

↓

JDBC

↓

Database
```

Notice:

JDBC never goes away.

Every layer builds on top of it.

---

# 💡 Best Practices

✅ Understand what JDBC does, even if you never write it manually.

✅ Always use the correct database driver.

✅ Let Hibernate manage JDBC in Spring Boot applications.

✅ Never manually open database connections inside Spring Boot services.

Spring manages this for us.

---

# 🎤 Interview Notes

### Q1. What is JDBC?

JDBC is the standard Java API used to communicate with relational databases.

---

### Q2. Is JDBC a Framework?

No.

It is a Java API (part of the Java ecosystem).

---

### Q3. Does Hibernate replace JDBC?

No.

Hibernate is built on top of JDBC.

Hibernate still uses JDBC internally.

---

### Q4. Why don't we write JDBC code in Spring Boot?

Because Hibernate and Spring Data JPA automate connection handling, SQL generation, object mapping, and resource management.

---

### Q5. Why did we add the PostgreSQL dependency?

To provide the PostgreSQL JDBC Driver, which enables JDBC to communicate with a PostgreSQL database.

---

# 📝 Things to Remember

✔ JDBC is the bridge between Java and relational databases.

✔ JDBC sends SQL to the database.

✔ JDBC requires a database driver.

✔ PostgreSQL Driver is a JDBC Driver.

✔ Hibernate internally uses JDBC.

✔ Spring Data JPA also uses JDBC.

✔ Modern Spring Boot applications rarely require manual JDBC code.

---

# 🔗 Chapter Connection

So far, our journey looks like this:

```text
Database

↓

SQL

↓

JDBC
```

At this point, Java can finally communicate with the database.

But a new problem appears...

Developers have to write hundreds of lines of repetitive JDBC code for every project.

This leads us to the next chapter:

# **Chapter 4 — ORM (Object Relational Mapping)**

In the next chapter, we'll answer one of the biggest questions in backend development:

> **"Why did Hibernate exist in the first place if JDBC already worked?"**

Once you understand ORM, the existence of JPA, Hibernate, and Spring Data JPA will feel completely natural.
