# Chapter 17 — First-Level Cache (Hibernate's Built-in Cache)

---

# 📖 Definition

The **First-Level Cache** is Hibernate's **built-in cache** that stores **managed entities inside the Persistence Context**.

Every entity loaded during a session/transaction is automatically placed into this cache.

> **Simple Definition:**
> The First-Level Cache is Hibernate's internal memory where managed entities are stored so that repeated database queries can often be avoided.

---

# 🤔 Why Do We Need It?

Imagine this code:

```java
Employee employee1 =
        employeeRepository.findById(1L).orElseThrow();

Employee employee2 =
        employeeRepository.findById(1L).orElseThrow();
```

Should Hibernate execute:

```sql
SELECT * FROM employees WHERE id = 1;
```

twice?

That would be wasteful.

Instead, Hibernate remembers that it already loaded the entity.

---

# 🚨 Problem It Solves

Without Cache

```text
findById(1)

↓

SELECT

↓

findById(1)

↓

SELECT Again

↓

Unnecessary Database Work
```

With First-Level Cache

```text
findById(1)

↓

SELECT

↓

Store in Cache

↓

findById(1)

↓

Return Cached Object

↓

No SQL
```

Less database traffic.

Better performance.

---

# 🌍 Real-World Analogy

Imagine borrowing a book from your desk.

First time:

```text
Need Book

↓

Walk to Shelf

↓

Pick Book
```

Second time:

```text
Need Book

↓

Already on Desk

↓

Use It
```

You don't walk back to the shelf.

Hibernate behaves exactly like this.

The desk is the **First-Level Cache**.

---

# 🏗 Our Project Example

Suppose our service executes:

```java
Employee employee1 =
        employeeRepository.findById(1L).orElseThrow();

Employee employee2 =
        employeeRepository.findById(1L).orElseThrow();
```

Inside the same transaction.

Hibernate performs:

First call

```sql
SELECT *
FROM employees
WHERE id = 1;
```

Second call

```text
Return Cached Entity
```

No second SQL query.

---

# ⚙ Behind the Scenes

## Step 1

Repository

```java
findById(1L)
```

Hibernate checks

```text
Persistence Context

↓

Employee Exists?

↓

No
```

Result

```sql
SELECT *
FROM employees
WHERE id = 1;
```

Entity loaded.

Stored inside cache.

---

## Step 2

Repository

```java
findById(1L)
```

Hibernate checks

```text
Persistence Context

↓

Employee Exists?

↓

YES

↓

Return Cached Entity
```

No SQL generated.

---

# 🧠 Mental Model

```text
Database

↓

Employee Loaded

↓

Persistence Context

↓

First-Level Cache

↓

Future Requests

↓

Memory
```

Database is contacted only once.

---

# 🔥 Identity Guarantee

One of the coolest features.

```java
Employee employee1 =
employeeRepository.findById(1L).orElseThrow();

Employee employee2 =
employeeRepository.findById(1L).orElseThrow();

System.out.println(employee1 == employee2);
```

Output

```text
true
```

Why?

Because Hibernate returned the **same Java object**.

Not another copy.

---

# 🏗 Visual Representation

```text
Persistence Context

+---------------------------+

Employee(id=1)

Employee(id=2)

Department(id=1)

+---------------------------+

        ▲

        │

findById(1)

        │

Same Object Returned
```

---

# 💻 SQL Comparison

Without Cache

```sql
SELECT * FROM employees WHERE id = 1;

SELECT * FROM employees WHERE id = 1;
```

Two database calls.

---

With First-Level Cache

```sql
SELECT * FROM employees WHERE id = 1;
```

Only one query.

Second lookup comes from memory.

---

# 🔄 Relationship with Persistence Context

Many beginners think these are different.

They are closely related.

```text
Persistence Context

↓

Contains

↓

Managed Entities

↓

This storage is called

↓

First-Level Cache
```

Think of the First-Level Cache as one of the key responsibilities of the Persistence Context.

---

# 🚨 Scope of the First-Level Cache

Very important.

The First-Level Cache exists **only within one Persistence Context**.

Usually:

```text
One Transaction

↓

One Persistence Context

↓

One First-Level Cache
```

After the transaction ends:

```text
Persistence Context Closed

↓

Cache Cleared
```

Next transaction starts with a fresh cache.

---

# ❌ Common Beginner Mistakes

### Mistake 1

Thinking the cache is shared across users.

Wrong.

Each Persistence Context has its own cache.

---

### Mistake 2

Confusing the First-Level Cache with the Second-Level Cache.

The First-Level Cache is:

* Built into Hibernate
* Always enabled
* Scoped to a Persistence Context

A Second-Level Cache is optional and shared across Persistence Contexts.

---

### Mistake 3

Expecting cached entities after the transaction ends.

Once the Persistence Context is closed, the First-Level Cache is gone.

---

# ✅ Best Practices

✅ Trust Hibernate's built-in cache.

✅ Don't manually reload the same entity unnecessarily within the same transaction.

✅ Understand that cache lifetime matches the Persistence Context lifetime.

---

# 🎤 Interview Questions

### Q1. What is the First-Level Cache?

A built-in Hibernate cache that stores managed entities inside the Persistence Context.

---

### Q2. Is the First-Level Cache enabled by default?

Yes.

It is always enabled.

---

### Q3. Can it be shared between users or transactions?

No.

It belongs to a single Persistence Context.

---

### Q4. Why does `employee1 == employee2` return `true`?

Because Hibernate returns the same managed entity instance from the First-Level Cache.

---

### Q5. Does the First-Level Cache survive after a transaction ends?

No.

It is cleared when the Persistence Context is closed.

---

# 📝 AMB (Always Remember)

✔ First-Level Cache is built into Hibernate.

✔ It lives inside the Persistence Context.

✔ It stores managed entities.

✔ Repeated lookups can avoid additional SQL.

✔ Identity is preserved (`==` can be true).

✔ Cache ends with the Persistence Context.

---

# 🧠 Final Mental Model

```text
findById()

        │

        ▼

Persistence Context

        │

Entity Already Loaded?

   ┌───────────────┐
   │               │
  NO              YES
   │               │
   ▼               ▼
SELECT SQL    Return Cached Object
   │
   ▼
Store in First-Level Cache
```

> **Golden Rule:**
> **Hibernate never queries the database twice for the same managed entity within the same Persistence Context unless it has a reason to refresh it.**

---

# 🔗 Chapter Connection

So far we've learned:

* How entities are managed
* How changes are detected
* How Hibernate caches entities

Now we're ready to understand one of the biggest performance problems in ORM applications:

## **N+1 Query Problem**

Imagine:

```java
List<Employee> employees =
        employeeRepository.findAll();
```

Then:

```java
for (Employee employee : employees) {
    System.out.println(
        employee.getDepartment().getName()
    );
}
```

Looks harmless...

But Hibernate may execute:

```text
1 Query

+

100 More Queries

=

101 SQL Statements
```

This is called the **N+1 Query Problem**.

It is one of the most common Hibernate performance issues and a very popular interview topic.

In the next chapter we'll learn:

* What the N+1 Query Problem is
* Why it happens
* How to recognize it
* How to solve it using proper JPA techniques
* Best practices for production applications
