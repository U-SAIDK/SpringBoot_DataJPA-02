# Chapter 14 — Fetch Types (LAZY vs EAGER)

---

# 📖 Definition

A **Fetch Type** tells Hibernate **when related entities should be loaded from the database**.

It answers one simple question:

> **"Should Hibernate load the related object immediately, or only when I actually need it?"**

JPA provides two fetch strategies:

* `FetchType.LAZY`
* `FetchType.EAGER`

---

# 🤔 Why Do We Need Fetch Types?

Imagine an Employee belongs to a Department.

When we execute:

```java
Employee employee = employeeRepository.findById(1L).get();
```

Should Hibernate also load:

* Department?
* Department's Employees?
* Department's Manager?
* Department's Projects?

Maybe.

Maybe not.

Loading everything every time wastes memory, CPU, and database queries.

Fetch types solve this problem.

---

# 🚨 Problem It Solves

Without fetch strategies:

```text
Load Employee

↓

Load Department

↓

Load Employees

↓

Load Projects

↓

Load Manager

↓

Load Everything
```

Even if your code only needs:

```java
employee.getFirstName();
```

Huge waste.

---

With fetch strategies:

```text
Load Employee

↓

Only load Department if needed
```

Much faster.

Much lighter.

---

# 🌍 Real-World Analogy

Imagine ordering food through a delivery app.

You order:

```text
Burger
```

Should the restaurant also send:

* Pizza
* Fries
* Dessert
* Drinks

Automatically?

No.

Only what you requested.

If later you ask for fries,

then fries are prepared.

That is exactly how **LAZY Loading** works.

---

# 🏗 Our Project

Our Employee entity contains:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;
```

This tells Hibernate:

> "Load the Employee now. Load the Department only if someone asks for it."

---

# 🗄 Database View

Database

```text
employees

+----+-------+---------------+
| id | name  | department_id |
+----+-------+---------------+
| 1  | USAID | 1             |
+----+-------+---------------+

━━━━━━━━━━━━━━━━━━━━━━

departments

+----+-------------+
| id | name        |
+----+-------------+
| 1  | Engineering |
+----+-------------+
```

Employee stores only

```text
department_id
```

Hibernate decides **when** to retrieve the actual Department row.

---

# ☕ Java Object View

Immediately after loading:

```text
Employee

↓

Department ?

(Not Loaded Yet)
```

The Employee object exists.

The Department object is still waiting.

---

# ⚙ Behind the Scenes — LAZY Loading

Suppose we execute:

```java
Employee employee =
employeeRepository.findById(1L).get();
```

Hibernate generates:

```sql
SELECT *
FROM employees
WHERE id = 1;
```

Notice:

No query for Department.

---

Later...

```java
employee.getDepartment();
```

Now Hibernate realizes:

> "The Department hasn't been loaded yet."

So it executes another query:

```sql
SELECT *
FROM departments
WHERE id = 1;
```

Only then is the Department object created.

This is called **Lazy Loading**.

---

# 🧠 Mental Model

```text
Employee Loaded

│

└── Department

        │

        ▼

Loads only when accessed
```

Think of it as **"Load on Demand."**

---

# 🔥 What is EAGER Loading?

Now imagine:

```java
@ManyToOne(fetch = FetchType.EAGER)
private Department department;
```

Hibernate immediately executes:

```sql
SELECT *
FROM employees
WHERE id = 1;

SELECT *
FROM departments
WHERE id = 1;
```

Even if you never call:

```java
employee.getDepartment();
```

The Department is already loaded.

---

# ⚖️ LAZY vs EAGER

| LAZY                                        | EAGER                                           |
| ------------------------------------------- | ----------------------------------------------- |
| Loads only when needed                      | Loads immediately                               |
| Better performance                          | Can load unnecessary data                       |
| Default for most collections (`@OneToMany`) | Default for `@ManyToOne` and `@OneToOne` in JPA |
| Saves memory                                | Uses more memory                                |

---

# 🏗 Why Did We Choose LAZY?

Our project intentionally overrides the default:

```java
@ManyToOne(fetch = FetchType.LAZY)
```

Why?

Because most REST APIs don't always need the Department.

Sometimes we only return:

```json
{
  "id": 1,
  "firstName": "USAID",
  "lastName": "Khan"
}
```

Loading the Department every time would be unnecessary.

LAZY keeps the application efficient.

---

# ⚠️ Important Interview Point

Many developers don't realize:

```java
@ManyToOne
```

without specifying anything is **EAGER** by default according to the JPA specification.

We explicitly changed it to:

```java
fetch = FetchType.LAZY
```

because it's generally a better choice for production systems.

---

# ❌ Common Beginner Mistakes

### Mistake 1

Using `EAGER` everywhere.

This often causes unnecessary database queries and poor performance.

---

### Mistake 2

Thinking `LAZY` means "never load."

Wrong.

It means:

> **Load later, when accessed.**

---

### Mistake 3

Confusing `LAZY` with caching.

LAZY controls **when** data is fetched.

Caching controls **whether** data needs to be fetched again.

These are different concepts.

---

# ✅ Best Practices

✅ Prefer `LAZY` unless you truly need immediate loading.

✅ Override JPA defaults when appropriate.

✅ Think about API requirements before choosing a fetch strategy.

✅ Be aware that lazy loading requires an active persistence context (we'll study this later).

---

# 🎤 Interview Questions

### Q1. What is FetchType?

It defines when related entities are loaded from the database.

---

### Q2. What is the difference between LAZY and EAGER?

* LAZY loads related data only when accessed.
* EAGER loads related data immediately.

---

### Q3. What is the default fetch type for `@ManyToOne`?

`FetchType.EAGER`.

---

### Q4. Why do many developers prefer LAZY?

Because it avoids loading unnecessary data and usually improves performance.

---

# 📝 AMB (Always Remember)

✔ Fetch Type controls **when** related data is loaded.

✔ LAZY = Load on demand.

✔ EAGER = Load immediately.

✔ `@ManyToOne` defaults to EAGER.

✔ Our project explicitly uses LAZY for better performance.

✔ Choosing the right fetch strategy is important for scalable applications.

---

# 🧠 Final Mental Model

```text
LAZY

Employee

↓

Department

(Not Loaded Yet)

↓

employee.getDepartment()

↓

Load Department


━━━━━━━━━━━━━━━━━━━━━━━━━━

EAGER

Employee

↓

Department

↓

Both loaded immediately
```

> **Golden Rule:**
> **LAZY loads data only when you ask for it. EAGER loads data whether you need it or not.**

---

# 🔗 Chapter Connection

LAZY loading gives us better performance, but it introduces another question:

> **How does Hibernate know when to automatically save changes to an entity?**

For example:

```java
employee.setSalary(90000);
```

Why don't we always need to call an explicit SQL `UPDATE`?

How does Hibernate detect that an object has changed?

That leads us into the next major concept:

# **Chapter 15 — Entity Lifecycle & Persistence Context**

We'll learn:

* Transient
* Persistent (Managed)
* Detached
* Removed
* Persistence Context
* Dirty Checking

These concepts explain the "magic" behind Hibernate's automatic database updates.
