# Chapter 5 — JPA (Java Persistence API)

---

# 📖 Definition

**JPA (Java Persistence API)** is the **official Java specification** that defines **how Java applications should map objects to relational databases and perform persistence operations.**

> **Simple Definition:** JPA is a **set of rules (specification)** for ORM in Java. It is **not** an ORM framework and **cannot work by itself**.

---

# ❓ Why Do We Need JPA?

Let's imagine a world **before JPA**.

There were many ORM frameworks.

```text
Hibernate

TopLink

OpenJPA

EclipseLink

...
```

Every framework had its own API.

Its own annotations.

Its own methods.

If you switched frameworks,

you had to rewrite your code.

That was a huge problem.

---

Java solved this by introducing **JPA**.

JPA says:

> "Every ORM framework should follow these common rules."

Now developers can write code using the **JPA standard** instead of depending directly on one ORM framework.

---

# 🚨 Problems Before JPA

Suppose Company A uses Hibernate.

Company B uses EclipseLink.

Company C uses OpenJPA.

Without JPA

```text
Different APIs

↓

Different Code

↓

Different Learning

↓

Vendor Lock-In
```

Changing ORM frameworks would require major code changes.

---

JPA solved this.

```text
Java Application

↓

JPA Specification

↓

Any JPA Provider

↓

Database
```

Your application depends on **JPA**, not on a specific ORM implementation.

---

# 🎯 Important Core Points (AMB)

* JPA is a **Specification**, not a Framework.
* JPA defines rules.
* JPA does **not** contain an implementation.
* JPA cannot communicate with a database by itself.
* Hibernate is the implementation we use in our project.
* Spring Data JPA builds on top of JPA.

---

# 🧩 Mental Model

This is the most important diagram in this chapter.

```text
Developer

↓

Writes Code Using JPA

↓

Hibernate Implements JPA

↓

JDBC

↓

PostgreSQL
```

Notice carefully.

We write code using **JPA APIs**.

Hibernate performs the actual work.

---

# 📦 Specification vs Implementation

Think about driving.

Traffic Rules

```text
Drive on Left

Wear Seat Belt

Follow Speed Limit
```

These are **rules**.

Rules don't move your car.

Now think about the car.

```text
BMW

Audi

Mercedes

Toyota
```

These are **implementations**.

Exactly the same idea.

```text
JPA

↓

Specification (Rules)

━━━━━━━━━━━━━━━━━━━━━━

Hibernate

↓

Implementation (Engine)
```

JPA defines **what should happen**.

Hibernate decides **how it happens**.

---

# 🏗️ Our Project Example

Our project contains

```java
@Entity
public class Employee {
}
```

Who provides `@Entity`?

Answer:

**JPA Specification**

Hibernate understands that annotation because it implements JPA.

---

Repository

```java
public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {
}
```

Notice the name.

`JpaRepository`

Not

`HibernateRepository`

Spring Data JPA is built around the JPA standard.

---

# ⚙️ Internal Working

Suppose we execute

```java
employeeRepository.save(employee);
```

Internally

```text
Spring Data JPA

↓

JPA API

↓

Hibernate

↓

JDBC

↓

PostgreSQL
```

JPA itself never saves anything.

Hibernate performs the actual persistence.

---

# 📊 Responsibilities

## JPA

Responsible for:

* Defining annotations
* Defining interfaces
* Defining lifecycle rules
* Defining EntityManager
* Defining persistence operations

---

## Hibernate

Responsible for:

* Reading entities
* Generating SQL
* Managing Persistence Context
* Dirty Checking
* Caching
* Sending SQL through JDBC

---

# 🏛️ JPA Provides

Some common annotations:

```java
@Entity

@Table

@Id

@GeneratedValue

@Column

@OneToMany

@ManyToOne

@JoinColumn
```

Common interfaces:

```java
EntityManager
```

Common concepts:

```text
Persistence Context

Entity Lifecycle

Transactions

ORM Mapping
```

These are part of the JPA specification.

Hibernate implements them.

---

# 📋 What Happens in Our Project?

When our application starts

Hibernate scans

```text
@Entity

↓

Employee

Department
```

Because JPA defines `@Entity`.

Hibernate recognizes it and creates the corresponding database mappings.

---

When we call

```java
employeeRepository.findAll();
```

Flow

```text
Repository

↓

JPA API

↓

Hibernate

↓

SQL

↓

Database

↓

Employee Objects
```

Again,

Hibernate performs the work.

JPA defines the contract.

---

# 🧠 One Common Misconception

Many beginners say

> "JPA connects to PostgreSQL."

❌ Wrong.

JPA has no database engine.

It cannot execute SQL.

Hibernate executes SQL.

---

Another misconception

> "JPA is Hibernate."

❌ Wrong.

Hibernate implements JPA.

They are related,

but not the same thing.

---

# 📊 Relationship Between Technologies

```text
Java Application

↓

Spring Data JPA

↓

JPA Specification

↓

Hibernate

↓

JDBC

↓

PostgreSQL
```

Every layer has a different responsibility.

---

# 💡 Real-Life Analogy

Imagine you're building a house.

The architect creates the blueprint.

```text
Blueprint

↓

Construction Company

↓

House
```

The blueprint defines **what should be built**.

The construction company actually builds it.

Likewise

```text
JPA

↓

Blueprint

↓

Hibernate

↓

Builds Everything
```

JPA defines the rules.

Hibernate implements them.

---

# 💡 Best Practices

✅ Program against the JPA specification rather than Hibernate-specific APIs whenever possible.

✅ Learn JPA concepts first.

✅ Learn Hibernate internals second.

✅ This keeps your code portable and easier to maintain.

---

# 🎤 Interview Notes

### Q1. What is JPA?

JPA (Java Persistence API) is the Java specification for Object Relational Mapping and persistence.

---

### Q2. Is JPA a Framework?

No.

JPA is a specification.

---

### Q3. Can JPA work without Hibernate?

No.

JPA requires an implementation (called a JPA Provider), such as Hibernate or EclipseLink.

---

### Q4. Is Hibernate JPA?

No.

Hibernate is an implementation of the JPA specification.

---

### Q5. Why do we prefer coding against JPA instead of Hibernate APIs?

Because it reduces vendor lock-in and makes switching JPA providers easier if needed.

---

# 📝 Things to Remember

✔ JPA = Specification.

✔ Hibernate = Implementation.

✔ JPA defines rules.

✔ Hibernate follows those rules.

✔ JPA cannot execute SQL.

✔ Hibernate executes SQL.

✔ Spring Data JPA is built on top of JPA.

---

# 🧠 Final Mental Model

Never forget this diagram.

```text
Java Objects

        │

        ▼

Spring Data JPA
(Productivity Layer)

        │

        ▼

JPA
(Specification / Rules)

        │

        ▼

Hibernate
(Implementation)

        │

        ▼

JDBC
(Database Communication)

        │

        ▼

PostgreSQL
(Database)
```

If you remember this stack, you'll understand where every technology fits.

---

# 🔗 Chapter Connection

Our learning journey now looks like this:

```text
Database

↓

SQL

↓

JDBC

↓

ORM

↓

JPA
```

We now know that JPA is only a **set of rules**.

The next question naturally becomes:

> **Who actually implements these rules and performs the real work?**

The answer is **Hibernate**.

In the next chapter, we'll study Hibernate in depth:

* What Hibernate is.
* Why it became the most popular ORM framework.
* How it implements JPA.
* What happens internally when we call `save()`, `findById()`, and `findAll()`.
* Why understanding Hibernate is essential for mastering Spring Data JPA.
