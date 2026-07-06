# Chapter 10 — @ManyToOne Relationship

---

# 📖 Definition

A **Many-to-One** relationship means:

> **Many child records belong to one parent record.**

It is one of the **most common** relationships in enterprise applications.

---

# 🌍 Real-World Examples

```
Many Employees
        │
        ▼
One Department
```

```
Many Students
        │
        ▼
One College
```

```
Many Orders
        │
        ▼
One Customer
```

```
Many Books
        │
        ▼
One Author
```

The pattern is always:

```
Many Child Objects

↓

One Parent Object
```

---

# ❓ Why Do We Need It?

Imagine storing department information like this.

| Employee | Department |
| -------- | ---------- |
| John     | IT         |
| Alice    | IT         |
| Bob      | IT         |
| David    | HR         |

Notice the problem.

The department name is repeated again and again.

This creates:

❌ Duplicate Data

❌ Difficult Updates

❌ Wasted Storage

Instead, databases normalize data.

```
departments

+----+------+
| id | name |
+----+------+
| 1  | IT   |
| 2  | HR   |
+----+------+
```

Employees simply store the department ID.

```
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

One department.

Many employees.

Exactly a **Many-to-One** relationship.

---

# 🎯 Important Core Points (AMB)

* Many child objects.
* One parent object.
* Foreign Key exists in the child table.
* Child owns the relationship.
* `@JoinColumn` creates the foreign key.
* This is the most commonly used relationship in JPA.

---

# 🧩 Mental Model

```
Department

        ▲

        │

Employee

Employee

Employee

Employee
```

Many employees point to the same department.

---

# 🏗️ Our Project Example

Our Employee entity contains:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;
```

Let's understand every line.

---

# @ManyToOne

```java
@ManyToOne
```

Means

```
Many Employees

↓

One Department
```

Hibernate immediately understands the relationship.

---

# @JoinColumn

```java
@JoinColumn(name = "department_id")
```

This tells Hibernate:

> Create (or use) a column named `department_id`.

Database

```
employees

+----+------------+---------------+
| id | first_name | department_id |
+----+------------+---------------+
```

That column is the Foreign Key.

---

# What does Employee actually store?

Many beginners think Employee stores the department name.

Wrong.

Employee stores a Department object.

```java
Employee employee = new Employee();

Department department = ...

employee.setDepartment(department);
```

Java stores the object reference.

Hibernate stores only the ID.

Example

Java

```
Employee

↓

Department Object
```

Database

```
Employee Row

↓

department_id = 1
```

Hibernate performs the conversion automatically.

---

# 🔍 Behind the Scenes

Suppose we create an employee.

```java
Department department =
departmentRepository.findById(1L).get();

employee.setDepartment(department);

employeeRepository.save(employee);
```

Internally

```
Employee Object

↓

Read Department ID

↓

Generate SQL

↓

INSERT INTO employees

↓

department_id = 1

↓

Database
```

Notice something.

We never manually set

```
department_id
```

Hibernate extracted it automatically.

---

# SQL Generated

Hibernate generates something similar to

```sql
INSERT INTO employees
(first_name,last_name,email,salary,department_id)

VALUES

('USAID','Khan','abc@gmail.com',50000,1);
```

We never wrote this SQL.

Hibernate generated it.

---

# Foreign Key

Database

```
departments

+----+------+
| 1  | IT   |
+----+------+

employees

+----+--------+---------------+
| 1  | USAID  | 1             |
+----+--------+---------------+
```

The value

```
1
```

points to

```
departments.id
```

This is called a **Foreign Key**.

---

# Why We Needed DepartmentRepository

Remember our Service.

```java
Department department =
departmentRepository.findById(dto.getDepartmentId())
.orElseThrow(...);
```

Why didn't we simply write

```java
employee.setDepartmentId(1L);
```

Because

Employee doesn't have

```java
Long departmentId;
```

It has

```java
Department department;
```

Hibernate works with **Objects**.

Not foreign key values.

This is one of the biggest mindset shifts when learning JPA.

---

# Flow Inside Our Project

```
Client

↓

departmentId = 1

↓

Controller

↓

Service

↓

DepartmentRepository.findById()

↓

Department Entity

↓

Employee.setDepartment()

↓

EmployeeRepository.save()

↓

Hibernate

↓

department_id = 1

↓

Database
```

Exactly what our project does.

---

# Common Beginner Mistake

❌

```java
private Long departmentId;
```

inside Employee.

That's not object-oriented.

Correct

```java
private Department department;
```

Hibernate manages the foreign key.

---

# Best Practices

✅ Store object references.

✅ Let Hibernate manage foreign keys.

✅ Always load the parent entity before assigning it.

✅ Use `@JoinColumn` with meaningful names.

---

# 🎤 Interview Notes

### Q1. What is `@ManyToOne`?

It represents a relationship where many child entities belong to one parent entity.

---

### Q2. Where is the foreign key stored?

In the child table.

In our project:

```
employees.department_id
```

---

### Q3. What does `@JoinColumn` do?

It specifies the foreign key column used for the relationship.

---

### Q4. Why does Employee store a Department object instead of a departmentId?

Because JPA works with object relationships. Hibernate automatically converts object references into foreign key values.

---

# 📝 Things to Remember

✔ Many Employees → One Department.

✔ Foreign Key lives in the Employee table.

✔ Employee is the owning side of this relationship.

✔ `@JoinColumn` creates or maps the foreign key.

✔ Java stores a `Department` object.

✔ Database stores only `department_id`.

✔ Hibernate performs the conversion automatically.

---

# 🧠 Final Mental Model

```
Java World

Employee

│

▼

Department Object

━━━━━━━━━━━━━━━━━━━━

Database World

employees

│

▼

department_id

━━━━━━━━━━━━━━━━━━━━

Hibernate

↓

Converts Object ↔ Foreign Key
```

> **Golden Rule:**
> **In Java, think in Objects. In the database, think in IDs. Hibernate connects these two worlds.**

---

# 🔗 Chapter Connection

So far, we've looked at the relationship only from the **Employee** side.

```
Employee

↓

Department
```

But what if the Department also wants to know:

> "Which employees belong to me?"

That's where the opposite side of the relationship comes in.

In the next chapter we'll implement:

```java
@OneToMany(mappedBy = "department")
private List<Employee> employees;
```

You'll finally understand:

* Why we need `mappedBy`
* Why this relationship is bidirectional
* Why one side owns the foreign key
* How both entities stay synchronized
* How Hibernate avoids creating duplicate foreign keys
