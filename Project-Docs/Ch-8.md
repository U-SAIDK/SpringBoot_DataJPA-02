# Chapter 15 — Entity Lifecycle & Persistence Context

---

# 📖 Definition

Every JPA Entity goes through different **states** during its lifetime.

Hibernate tracks these states inside a special container called the **Persistence Context**.

> **Simple Definition:**
> The Entity Lifecycle describes an entity's journey from creation to deletion. The Persistence Context is Hibernate's memory that tracks those entities.

---

# 🤔 Why Do We Need It?

Imagine Hibernate didn't keep track of objects.

```java
Employee employee = employeeRepository.findById(1L).get();

employee.setSalary(90000);
```

How would Hibernate know:

* Which object changed?
* Which column changed?
* When to execute UPDATE?

It wouldn't.

The Persistence Context solves this problem.

---

# 🚨 Problem It Solves

Without Persistence Context

```text
Database

↓

Java Object

↓

Object Changes

↓

❌ Hibernate doesn't know
```

With Persistence Context

```text
Database

↓

Persistence Context

↓

Managed Entity

↓

Detect Changes

↓

Generate SQL Automatically
```

Hibernate always knows what's happening.

---

# 🌍 Real-World Analogy

Think of a teacher taking attendance.

```text
Classroom

↓

Teacher's Attendance Register

↓

Student arrives

↓

Teacher marks Present

↓

Student leaves

↓

Teacher marks Absent
```

The teacher always knows each student's status.

Hibernate works the same way.

The Persistence Context is Hibernate's attendance register.

---

# 🧠 The Four Entity States

Every entity exists in one of these four states.

```text
Transient

↓

Persistent (Managed)

↓

Detached

↓

Removed
```

Let's understand each one.

---

# 1️⃣ Transient State

Definition:

A Java object that **exists only in memory**.

Hibernate doesn't know about it.

Database doesn't know about it.

Example

```java
Employee employee = new Employee();

employee.setFirstName("USAID");
```

Current state

```text
Employee Object

↓

Java Memory

↓

❌ Not managed

❌ Not in Database
```

No SQL has been executed.

---

# 2️⃣ Persistent (Managed) State ⭐⭐⭐⭐⭐

This is the most important state.

Definition

An entity that is currently managed by Hibernate inside the Persistence Context.

Example

```java
Employee employee =
employeeRepository.findById(1L).get();
```

Hibernate executes

```sql
SELECT *
FROM employees
WHERE id = 1;
```

Now

```text
Database

↓

Employee Object

↓

Persistence Context

↓

Managed
```

Hibernate starts monitoring the object.

---

# Why is Managed State Special?

Suppose we write

```java
employee.setSalary(90000);
```

Notice:

No SQL yet.

Hibernate simply remembers

```text
Original Salary

↓

50000

━━━━━━━━━━━━━━

Current Salary

↓

90000
```

Later...

When the transaction finishes...

Hibernate compares both values.

It notices

```text
Salary Changed
```

Then automatically executes

```sql
UPDATE employees
SET salary = 90000
WHERE id = 1;
```

This automatic change detection is called **Dirty Checking**.

We'll study it in detail in the next chapter.

---

# 3️⃣ Detached State

Definition

An entity that was once managed but is **no longer attached** to the Persistence Context.

Example

```text
Database

↓

Managed Entity

↓

Persistence Context Closed

↓

Detached Entity
```

Now you can still modify it.

```java
employee.setSalary(100000);
```

But Hibernate no longer tracks those changes.

No UPDATE query will be generated automatically.

---

# 4️⃣ Removed State

Definition

A managed entity that has been marked for deletion.

Example

```java
employeeRepository.delete(employee);
```

Internally

```text
Managed Entity

↓

Marked for Removal

↓

DELETE SQL generated

↓

Removed from Database
```

Hibernate executes

```sql
DELETE
FROM employees
WHERE id = 1;
```

---

# 📊 Entity Lifecycle Diagram

```text
           new Employee()

                 │

                 ▼

         Transient Entity

                 │

                 │ save()

                 ▼

       Persistent (Managed)

        │              │

 update fields      delete()

        │              │

        ▼              ▼

 Dirty Checking     Removed

        │

Persistence Context Closed

        │

        ▼

     Detached
```

This diagram is worth memorizing.

---

# 🏗 Our Project Example

When creating a new employee:

```java
Employee employee =
EmployeeMapper.toEntity(dto);
```

State

```text
Transient
```

---

Then

```java
employeeRepository.save(employee);
```

State becomes

```text
Persistent
```

---

Later

```java
employee.setSalary(75000);
```

Still

```text
Persistent
```

Hibernate notices the change.

---

Finally

```java
employeeRepository.delete(employee);
```

State becomes

```text
Removed
```

---

# ⚙ Behind the Scenes

Imagine this code.

```java
Employee employee =
employeeRepository.findById(1L).get();

employee.setSalary(90000);
```

Internally

```text
SELECT Query

↓

Employee Loaded

↓

Stored Inside Persistence Context

↓

Object Modified

↓

Hibernate Detects Difference

↓

UPDATE Query Generated

↓

Transaction Commit
```

Notice

You never wrote SQL.

Hibernate did everything.

---

# 🧠 What is the Persistence Context?

The Persistence Context is a memory area managed by Hibernate.

It stores

```text
Managed Entities

Identity Map

Snapshots

Pending Changes
```

Whenever Hibernate loads an entity,

it stores it inside the Persistence Context.

---

# 📦 Visual Representation

```text
Hibernate

│

▼

Persistence Context

+------------------------+

Employee(id=1)

Department(id=1)

Employee(id=2)

+------------------------+

↓

Database
```

Hibernate always checks this container first.

---

# ❌ Common Beginner Mistakes

### Mistake 1

Thinking every object is managed.

Wrong.

Only entities inside the Persistence Context are managed.

---

### Mistake 2

Changing a Detached Entity.

Hibernate ignores those changes unless it is reattached.

---

### Mistake 3

Believing `save()` is required after every setter.

Not always.

If the entity is managed within a transaction, Hibernate can detect changes automatically.

---

# ✅ Best Practices

✅ Understand which state your entity is in.

✅ Work with managed entities inside transactions.

✅ Let Hibernate handle updates through Dirty Checking when appropriate.

✅ Avoid unnecessary calls to `save()` on already managed entities.

---

# 🎤 Interview Questions

### Q1. What are the four entity lifecycle states?

* Transient
* Persistent (Managed)
* Detached
* Removed

---

### Q2. What is the Persistence Context?

A first-level cache managed by Hibernate that tracks managed entities and their changes.

---

### Q3. Which entity state does Hibernate monitor?

Only the Persistent (Managed) state.

---

### Q4. What happens when a managed entity changes?

Hibernate detects the change and generates an UPDATE statement during transaction commit.

---

### Q5. Does every Java object become a managed entity?

No.

Only entities loaded or persisted within an active Persistence Context are managed.

---

# 📝 AMB (Always Remember)

✔ Every entity has a lifecycle.

✔ Persistence Context tracks managed entities.

✔ Managed entities are automatically monitored.

✔ Detached entities are not monitored.

✔ Removed entities are scheduled for deletion.

✔ Hibernate automatically generates SQL for managed entities.

---

# 🧠 Final Mental Model

```text
             new Employee()

                   │

                   ▼

              Transient

                   │ save()

                   ▼

       Persistence Context

      +-------------------+

      | Managed Employee  |

      +-------------------+

           │

     Change salary

           │

           ▼

    Dirty Checking

           │

           ▼

     UPDATE SQL

           │

Transaction Commit
```

> **Golden Rule:**
> **Hibernate doesn't watch every Java object. It only watches managed entities inside the Persistence Context. That's why automatic updates work.**

---

# 🔗 Chapter Connection

Now we know that Hibernate tracks managed entities.

But a new question appears:

> **How does Hibernate know exactly which field changed?**

Suppose:

```java
employee.setFirstName("USAID");
employee.setSalary(90000);
```

How does Hibernate detect these modifications without you calling an UPDATE method?

The answer is one of Hibernate's most powerful features:

# **Chapter 16 — Dirty Checking**

We'll learn:

* What Dirty Checking is.
* How Hibernate compares objects.
* Snapshots and change detection.
* When UPDATE SQL is generated.
* Why unnecessary `save()` calls are often not required.
* Performance implications and interview best practices.
