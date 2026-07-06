# Chapter 11 — @OneToMany Relationship

---

# 📖 Definition

A **One-to-Many** relationship means:

> **One parent entity is associated with multiple child entities.**

It is the opposite side of a `@ManyToOne` relationship.

In our project:

```text
One Department

↓

Many Employees
```

One department can contain many employees.

---

# 🤔 Why Do We Need It?

Suppose someone asks:

> "Show me all employees working in the Engineering department."

Without `@OneToMany`, the Department object only knows about itself.

```java
Department
```

It has no idea which employees belong to it.

By adding `@OneToMany`, Hibernate automatically connects the parent with all of its children.

---

# 🚨 Problem It Solves

Without `@OneToMany`

```text
Department

↓

No Employee List
```

To get employees, you'd manually write queries every time.

With `@OneToMany`

```text
Department

↓

employees

↓

Employee

Employee

Employee
```

The relationship becomes part of your object model.

---

# 🌍 Real-World Analogy

Imagine a company.

```text
Engineering Department

↓

John

Alice

Bob

David
```

The department knows who works inside it.

Likewise,

```text
Department

↓

List<Employee>
```

---

# 🏗 Our Project Example

Let's update our `Department` entity.

```java
@OneToMany(
        mappedBy = "department",
        fetch = FetchType.LAZY
)
private List<Employee> employees = new ArrayList<>();
```

Now the Department object can access all associated employees.

---

# 🗄 Database View

Notice something very important.

Even after adding `@OneToMany`...

The database **does not change**.

Still only one foreign key exists.

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

Notice:

There is **NO**

```text
employee_ids
```

column inside the Department table.

This is a huge interview point.

---

# ☕ Java Object View

Database stores

```text
department_id
```

Java stores

```text
Department

↓

employees

↓

Employee

Employee

Employee
```

Hibernate converts between the two worlds.

---

# ⚙ Behind the Scenes

Suppose we write:

```java
Department department =
departmentRepository.findById(1L).get();

department.getEmployees();
```

Internally

```text
Department Object

↓

employees requested

↓

Hibernate notices LAZY collection

↓

Generate SQL

↓

SELECT *

FROM employees

WHERE department_id = 1

↓

Create Employee Objects

↓

Return List<Employee>
```

The list isn't stored inside the database.

Hibernate creates it dynamically.

---

# 💻 SQL Generated

Hibernate generates something similar to:

```sql
SELECT *

FROM employees

WHERE department_id = 1;
```

Again,

we never wrote this SQL.

---

# 🧠 Mental Model

```text
Department

│

├── Employee

├── Employee

├── Employee

└── Employee
```

The parent object contains references to many child objects.

---

# 🔥 What is mappedBy?

Our code:

```java
@OneToMany(mappedBy = "department")
private List<Employee> employees;
```

The word

```text
department
```

is **NOT**

the database column.

It is the **field name inside Employee.java**.

Employee

```java
private Department department;
```

Hibernate reads

```text
mappedBy = "department"
```

and understands

> "The Employee entity already owns this relationship."

Therefore,

Hibernate does **NOT** create another foreign key.

---

# 🏗 Relationship Diagram

```text
Department

        1

        │

        │

        ▼

Employee

Employee

Employee

Employee
```

The relationship is exactly the same as before.

We're simply viewing it from the opposite side.

---

# 🔄 Bidirectional Relationship

Currently,

Employee knows Department.

Now,

Department also knows Employees.

```text
Employee

↓

Department

━━━━━━━━━━━━━━━━━━

Department

↓

Employees
```

This is called a **Bidirectional Relationship**.

---

# ❌ Common Beginner Mistakes

### Mistake 1

Thinking `@OneToMany` creates another foreign key.

Wrong.

It doesn't.

The existing foreign key is reused.

---

### Mistake 2

Using

```java
mappedBy = "department_id"
```

Wrong.

It must reference the **Java field name**, not the database column.

Correct

```java
mappedBy = "department"
```

---

### Mistake 3

Forgetting to initialize the collection.

Always prefer

```java
private List<Employee> employees =
new ArrayList<>();
```

instead of leaving it `null`.

---

# ✅ Best Practices

✅ Use `mappedBy` on the parent side.

✅ Initialize collections.

✅ Prefer `List<>` for ordered relationships.

✅ Keep the child (`@ManyToOne`) as the owning side.

---

# 🎤 Interview Questions

### Q1. Does `@OneToMany` create a foreign key?

No.

The foreign key already exists in the child table.

---

### Q2. What does `mappedBy` refer to?

The field name inside the child entity.

Not the database column.

---

### Q3. Which side owns the relationship?

The `@ManyToOne` side.

---

### Q4. Can Department exist without employees?

Yes.

An empty list is perfectly valid.

---

# 📝 AMB (Always Remember)

✔ One Department → Many Employees.

✔ Foreign Key stays inside Employee.

✔ `mappedBy` references the Java field.

✔ `@OneToMany` is usually the inverse side.

✔ Hibernate builds the employee list dynamically.

✔ Parent stores objects.

✔ Database stores IDs.

---

# 🔗 Connection to Next Chapter

We now have both sides of the relationship:

```text
Employee

↓

Department

━━━━━━━━━━━━━━━━━━

Department

↓

Employees
```

This creates something called a **Bidirectional Relationship**.

While powerful, it also introduces new challenges:

* Infinite JSON recursion
* Keeping both sides synchronized
* Owning Side vs Inverse Side
* Serialization issues
* Helper methods (`addEmployee()`, `removeEmployee()`)

In the next chapter, we'll master **Bidirectional Relationships**, one of the most practical and interview-heavy topics in Spring Data JPA.
