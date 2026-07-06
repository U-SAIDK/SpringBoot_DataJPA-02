# Chapter 12 — Bidirectional Relationships

---

# 📖 Definition

A **Bidirectional Relationship** means:

> **Both entities know about each other and can navigate to each other.**

Unlike a unidirectional relationship, where only one entity has a reference, a bidirectional relationship allows navigation from **both directions**.

In our project:

```text
Employee  ─────────► Department

Department ───────► List<Employee>
```

Both entities are connected.

---

# 🤔 Why Do We Need Bidirectional Relationships?

Imagine you're building an HR system.

You want to answer two questions:

### Question 1

> Which department does John work in?

Easy.

```java
employee.getDepartment();
```

---

### Question 2

> Which employees work in the Engineering department?

Without a bidirectional relationship:

Impossible directly.

With it:

```java
department.getEmployees();
```

Now both questions are easy to answer.

---

# 🚨 Problem It Solves

Without Bidirectional Relationship

```text
Employee

↓

Department

━━━━━━━━━━━━━━━━━━━━━━

Department

❌ Doesn't know employees
```

With Bidirectional Relationship

```text
Employee

↓

Department

↑

Employees
```

Navigation works both ways.

---

# 🌍 Real-World Analogy

Think about a school.

A student knows which classroom they belong to.

```text
Student

↓

Classroom
```

At the same time,

the classroom knows all its students.

```text
Classroom

↓

Students
```

Both sides know about each other.

Exactly how a bidirectional relationship works.

---

# 🏗 Our Project

## Employee.java

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;
```

Employee knows its Department.

---

## Department.java

```java
@OneToMany(
        mappedBy = "department",
        fetch = FetchType.LAZY
)
private List<Employee> employees =
        new ArrayList<>();
```

Department knows all Employees.

Together they form one bidirectional relationship.

---

# 🗄 Database View

Even though Java has references in both directions...

The database still looks like this:

```text
departments

+----+-------------+
| id | name        |
+----+-------------+
| 1  | Engineering |
| 2  | HR          |
+----+-------------+

employees

+----+--------+---------------+
| id | name   | department_id |
+----+--------+---------------+
| 1  | John   | 1             |
| 2  | Alice  | 1             |
| 3  | Bob    | 1             |
| 4  | David  | 2             |
+----+--------+---------------+
```

Notice something important:

There is still **only one foreign key**.

```text
department_id
```

Bidirectional relationships exist in Java.

The database structure does **not** become bidirectional.

---

# ☕ Java Object View

Inside memory:

```text
Department

│

├── Employee

│      │

│      ▼

│   Department

│

├── Employee

│      │

│      ▼

│   Department

│

└── Employee

       │

       ▼

    Department
```

Both objects reference each other.

This is where beginners usually get confused.

---

# ⚙ Behind the Scenes

Suppose we execute

```java
Department department =
departmentRepository.findById(1L).get();
```

Hibernate creates

```text
Department Object
```

When we later access

```java
department.getEmployees();
```

Hibernate executes

```sql
SELECT *
FROM employees
WHERE department_id = 1;
```

Then each Employee object is created.

Each Employee already contains

```java
private Department department;
```

pointing back to the same Department object.

Everything becomes interconnected inside memory.

---

# 🧠 Mental Model

Think of it like a graph.

```text
        Department

        /   |   \

       /    |    \

Employee Employee Employee

       \    |    /

        \   |   /

       Department
```

Objects point to each other.

---

# 🔥 The Infinite Loop Problem

Now imagine Spring Boot wants to convert the objects into JSON.

It starts with

```text
Department
```

Department contains

```text
employees
```

Employee contains

```text
department
```

Department again contains

```text
employees
```

Employee again contains

```text
department
```

This never ends.

```text
Department

↓

Employees

↓

Department

↓

Employees

↓

Department

↓

Employees

↓

...
```

This is called **Infinite Recursion**.

---

# 🌐 Example

Suppose we return

```java
return department;
```

Spring tries to produce JSON.

```json
{
  "department": {
    "employees": [
      {
        "department": {
          "employees": [
```

It continues forever until the application throws an error such as a `StackOverflowError`.

---

# 🚨 Why Didn't We Face This Problem?

Because our project already follows a better design.

Instead of returning Entities,

we return

```text
DepartmentResponseDTO

EmployeeResponseDTO
```

The DTO breaks the recursive object graph.

This is one of the biggest advantages of using DTOs.

---

# ❌ Common Beginner Mistakes

### Mistake 1

Returning Entity objects directly from Controllers.

Instead:

```text
Entity

↓

Mapper

↓

DTO

↓

Client
```

---

### Mistake 2

Thinking both sides own the relationship.

They don't.

Only one side owns it.

We'll learn that in the next chapter.

---

### Mistake 3

Believing two references create two foreign keys.

Wrong.

Still only one foreign key exists.

---

# ✅ Best Practices

✅ Use DTOs for API responses.

✅ Understand that bidirectional relationships exist in Java memory.

✅ Keep only one owning side.

✅ Avoid exposing Entities directly.

---

# 🎤 Interview Questions

### Q1. What is a Bidirectional Relationship?

A relationship where both entities maintain references to each other.

---

### Q2. Does a bidirectional relationship create two foreign keys?

No.

Only one foreign key exists.

---

### Q3. Why can bidirectional relationships cause infinite recursion?

Because each object references the other, causing endless serialization.

---

### Q4. How do we avoid infinite recursion?

The best approach is to use DTOs for API responses.

Alternative annotations like `@JsonManagedReference` and `@JsonBackReference` also exist, but DTOs are generally preferred in production applications.

---

# 📝 AMB (Always Remember)

✔ Both entities know each other.

✔ Navigation works in both directions.

✔ Database still contains one foreign key.

✔ Java objects reference each other.

✔ Returning Entities directly may cause infinite recursion.

✔ DTOs solve this problem cleanly.

---

# 🧠 Final Mental Model

```text
                Java Memory

        Employee  ◄──────► Department

                ▲            │

                │            ▼

         Many Employees ◄────┘


━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

              Database

departments

        ▲

        │

department_id

        │

employees
```

> **Golden Rule:**
> **Bidirectional relationships exist in Java Objects, not in the database schema. The database still stores only one foreign key.**

---

# 🔗 Chapter Connection

We've learned that both entities know about each other.

But an important question remains:

> **If both entities have references, who actually controls the relationship?**

Consider this code:

```java
employee.setDepartment(department);
```

versus

```java
department.getEmployees().add(employee);
```

Which one updates the database?

Which one does Hibernate trust?

Which side writes the foreign key?

These questions introduce one of the most important Hibernate concepts:

# **Chapter 13 — Owning Side vs Inverse Side**

We'll understand:

* What the Owning Side is.
* What the Inverse Side is.
* Why `@ManyToOne` is usually the owner.
* Why `mappedBy` marks the inverse side.
* Which side Hibernate uses to generate SQL.
* How to keep both sides synchronized correctly.
