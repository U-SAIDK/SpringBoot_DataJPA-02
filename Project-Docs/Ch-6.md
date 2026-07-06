# Chapter 13 — Owning Side vs Inverse Side (Who Controls the Relationship?)

---

# 📖 Definition

In every **bidirectional JPA relationship**, only **one entity is responsible for managing the relationship in the database**.

This entity is called the **Owning Side**.

The other entity is called the **Inverse Side** (or Mapped Side).

> **Simple Definition:** The Owning Side is the side that writes and updates the foreign key in the database. The Inverse Side only reflects the relationship in Java.

---

# 🤔 Why Do We Need an Owning Side?

Imagine both entities could update the foreign key.

Example:

```java
employee.setDepartment(itDepartment);

department.getEmployees().add(employee);
```

Now imagine they don't agree.

```text
Employee says:
Department = IT

Department says:
Employee belongs to HR
```

Which one should Hibernate trust?

Without rules, Hibernate would not know which value to write into the database.

So JPA defines one simple rule:

> **Only one side controls the relationship.**

---

# 🚨 Problem It Solves

Without an owning side:

```text
Employee  ─────► IT Department

Department ───► Employee belongs to HR
```

Conflicting information.

Hibernate cannot decide which foreign key to save.

With an owning side:

```text
Employee (Owner)

↓

Writes Foreign Key

━━━━━━━━━━━━━━

Department

↓

Read-only view of relationship
```

Everything stays consistent.

---

# 🌍 Real-World Analogy

Imagine a passport office.

You change your address.

You update it only in the **government database**.

Your bank, employer, and insurance company read that information.

```text
Government Database

↓

Official Address

━━━━━━━━━━━━━━

Bank

Employer

Insurance
```

Only one system is allowed to write.

Everyone else reads.

Hibernate works exactly the same way.

---

# 🏗 Our Project

## Employee.java

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;
```

Notice something important.

Employee contains

```java
@JoinColumn
```

This is the owning side.

---

## Department.java

```java
@OneToMany(mappedBy = "department")
private List<Employee> employees;
```

Notice:

No `@JoinColumn`.

Instead it contains

```java
mappedBy
```

This is the inverse side.

---

# 🗄 Database View

The database contains

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
+----+--------+---------------+
```

Notice carefully.

The foreign key exists in

```text
employees.department_id
```

not inside the departments table.

That is why Employee owns the relationship.

---

# ☕ Java Object View

```text
Employee

↓

Department

━━━━━━━━━━━━━━━━━━━━

Department

↓

List<Employee>
```

Both references exist.

But only one writes to the database.

---

# ⚙ Behind the Scenes

Suppose we execute

```java
employee.setDepartment(engineering);

employeeRepository.save(employee);
```

Hibernate sees

```text
Owning Side Modified

↓

department_id changed

↓

Generate UPDATE SQL
```

SQL

```sql
UPDATE employees
SET department_id = 1
WHERE id = 10;
```

Everything works.

---

Now consider this.

```java
department.getEmployees().add(employee);

departmentRepository.save(department);
```

Nothing changes.

Why?

Because Hibernate checks

```text
Inverse Side

↓

Ignore Foreign Key Update
```

The inverse side cannot write the relationship.

---

# 🧠 Mental Model

```text
Employee

│

│   Controls

▼

department_id

━━━━━━━━━━━━━━━━━━

Department

│

│

▼

Only Mirrors Relationship
```

Remember:

Owner writes.

Inverse reads.

---

# 🔥 How Hibernate Thinks

Hibernate follows a simple rule.

```text
Does this side have @JoinColumn?

        │

      YES

        │

        ▼

Owns Relationship

━━━━━━━━━━━━━━━━━━━━

Does this side have mappedBy?

        │

      YES

        │

        ▼

Inverse Side
```

---

# 🏗 Example from Our Project

Correct

```java
employee.setDepartment(it);

employeeRepository.save(employee);
```

Result

```sql
UPDATE employees
SET department_id = 1;
```

---

Incorrect

```java
department.getEmployees().add(employee);

departmentRepository.save(department);
```

Result

```text
No Foreign Key Update
```

Because the owning side was never changed.

---

# 🔄 Keeping Both Sides in Sync

Although Hibernate only trusts the owning side, **your Java objects should stay consistent**.

A common helper method is:

```java
public void addEmployee(Employee employee) {
    employees.add(employee);
    employee.setDepartment(this);
}
```

Now both objects agree:

```text
Department

↓

Employees List Updated

━━━━━━━━━━━━━━

Employee

↓

Department Updated
```

Hibernate writes the foreign key because the owning side (`employee.setDepartment(...)`) changed, while the collection also stays correct in memory.

---

# ❌ Common Beginner Mistakes

### Mistake 1

Thinking both sides update the database.

❌ Wrong.

Only the owning side does.

---

### Mistake 2

Adding an employee only to the collection.

```java
department.getEmployees().add(employee);
```

Without:

```java
employee.setDepartment(department);
```

The database relationship will not be updated correctly.

---

### Mistake 3

Putting `@JoinColumn` on both sides.

This can lead to incorrect mappings or duplicate relationship definitions.

---

# ✅ Best Practices

✅ Put `@JoinColumn` on the owning side.

✅ Use `mappedBy` on the inverse side.

✅ Update the owning side whenever changing a relationship.

✅ Keep both sides synchronized with helper methods for consistency.

---

# 🎤 Interview Questions

### Q1. What is the owning side?

The entity that contains `@JoinColumn` and controls the foreign key.

---

### Q2. What is the inverse side?

The entity marked with `mappedBy`. It reflects the relationship but does not manage the foreign key.

---

### Q3. Which side does Hibernate use to generate SQL?

Only the owning side.

---

### Q4. Does `mappedBy` create a foreign key?

No.

It tells Hibernate that another entity already owns the relationship.

---

### Q5. Why should we synchronize both sides in Java?

To keep the in-memory object graph consistent and avoid confusing behavior during the application's execution.

---

# 📝 AMB (Always Remember)

✔ Every bidirectional relationship has one owner.

✔ `@JoinColumn` = Owning Side.

✔ `mappedBy` = Inverse Side.

✔ Foreign key lives on the owning side.

✔ Hibernate writes SQL only from the owning side.

✔ Synchronize both sides for a consistent object graph.

---

# 🧠 Final Mental Model

```text
                 Java Objects

Department  ◄──────────────► Employee

     ▲                           │

     │                           │

     │                           ▼

Employees List          @JoinColumn

     ▲                           │

     └──────────── Mirrors ───────┘


━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

                 Database

employees

+----+---------------+
| id | department_id |
+----+---------------+
| 1  |       1       |
+----+---------------+

Only Employee updates this column.
```

> **Golden Rule:**
> **The owning side controls the database. The inverse side only helps you navigate the object graph.**

---

# 🔗 Chapter Connection

We now know **who controls the relationship**.

The next question is:

> **What exactly does `mappedBy` do internally?**

We've used it:

```java
@OneToMany(mappedBy = "department")
private List<Employee> employees;
```

But how does Hibernate interpret this string?

Why does it prevent duplicate foreign keys?

What happens if you remove it?

In the next chapter we'll do a **deep dive into `mappedBy`**, including common mistakes, generated database schemas, and why it's one of the most important attributes in bidirectional relationships.
