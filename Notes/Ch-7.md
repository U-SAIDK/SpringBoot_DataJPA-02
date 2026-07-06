# Chapter 7 — Spring Data JPA

---

# 📖 Definition

**Spring Data JPA** is a Spring framework that simplifies database access by building on top of **JPA** and its implementation (**Hibernate**).

Instead of writing DAO classes, `EntityManager` code, or repetitive CRUD logic, Spring Data JPA automatically provides these features through repository interfaces.

> **Simple Definition:** Spring Data JPA is a productivity layer built on top of JPA that eliminates boilerplate database code.

---

# ❓ Why Do We Need Spring Data JPA?

Let's compare.

## Using Hibernate Directly

Without Spring Data JPA, you typically work with `EntityManager`.

```java
@PersistenceContext
private EntityManager entityManager;

public Employee save(Employee employee) {
    entityManager.persist(employee);
    return employee;
}
```

For every entity, you'd write methods like:

* save()
* findById()
* findAll()
* update()
* delete()

Again and again.

---

Now compare it with our project.

```java
public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {
}
```

That's all.

Immediately we get dozens of ready-to-use methods.

---

# 🚨 Problems Spring Data JPA Solves

Without Spring Data JPA

```text
EmployeeDAO

↓

save()

findById()

findAll()

delete()

update()

Custom Queries

Pagination

Sorting

...
```

Every project repeated the same code.

---

With Spring Data JPA

```text
EmployeeRepository

↓

extends JpaRepository

↓

Done ✅
```

No CRUD implementation required.

---

# 🎯 Important Core Points (AMB)

* Spring Data JPA is a Spring Framework.
* It is built on top of JPA.
* JPA is implemented by Hibernate.
* It automatically creates repository implementations.
* It provides built-in CRUD methods.
* It supports query generation from method names.
* It supports pagination and sorting.
* It reduces boilerplate code.

---

# 🧩 Mental Model

This is the complete stack.

```text
Your Code

↓

Spring Data JPA
(Productivity Layer)

↓

JPA
(Specification)

↓

Hibernate
(Implementation)

↓

JDBC

↓

PostgreSQL
```

Every layer has one responsibility.

---

# 🔍 Behind the Scenes

We write

```java
employeeRepository.save(employee);
```

Internally

```text
EmployeeRepository

↓

JpaRepository

↓

SimpleJpaRepository

↓

EntityManager

↓

Hibernate

↓

JDBC

↓

PostgreSQL
```

Notice something interesting.

Our repository is only an interface.

There is no implementation.

So who executes `save()`?

Spring Data JPA automatically creates an implementation at runtime.

---

# 🏗️ Our Project Example

Our repository

```java
@Repository
public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {
}
```

We never implemented

```java
save()

findAll()

findById()

delete()

count()

existsById()
```

Yet all of them work.

Why?

Because Spring Boot automatically creates an implementation when the application starts.

---

# ⚙️ What Happens During Application Startup?

When Spring Boot starts

```text
@ComponentScan

↓

Find Repository Interfaces

↓

EmployeeRepository

DepartmentRepository

↓

Generate Implementations

↓

Register Beans

↓

Dependency Injection Ready
```

That's why this works.

```java
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl
        implements EmployeeService {

    private final EmployeeRepository employeeRepository;

}
```

Spring injects the generated implementation.

Not the interface itself.

---

# 📦 JpaRepository

This is the interface we extend.

```java
public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {
}
```

Let's understand both generic parameters.

```text
JpaRepository<
Employee,
Long
>
```

### Employee

The Entity Class.

Spring knows which table to work with.

```text
Employee

↓

employees table
```

---

### Long

Primary Key Type.

Our Employee entity

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

Since the ID is `Long`,

the repository also uses `Long`.

---

# 🏗️ Repository Hierarchy

Many beginners think JpaRepository is the first interface.

Actually it extends multiple interfaces.

```text
Repository
      │
      ▼
CrudRepository
      │
      ▼
PagingAndSortingRepository
      │
      ▼
JpaRepository
```

Each level adds more features.

---

# 📊 What Each Interface Provides

## Repository

Marker interface.

No methods.

Just tells Spring:

> "This is a repository."

---

## CrudRepository

Basic CRUD operations.

Examples

```java
save()

findById()

findAll()

delete()

deleteById()

existsById()

count()
```

---

## PagingAndSortingRepository

Adds

```java
findAll(Pageable pageable)

findAll(Sort sort)
```

Useful when displaying large datasets.

---

## JpaRepository

Adds JPA-specific features.

Examples

```java
flush()

saveAndFlush()

deleteAllInBatch()

getReferenceById()
```

We'll use these later.

---

# 📊 Built-in Methods We Already Used

In our project

```java
employeeRepository.save(employee);
```

↓

INSERT or UPDATE

---

```java
employeeRepository.findById(id);
```

↓

SELECT ... WHERE id=?

---

```java
employeeRepository.findAll();
```

↓

SELECT *

---

```java
employeeRepository.delete(employee);
```

↓

DELETE

---

All without writing SQL.

---

# 🏗️ How save() Decides INSERT vs UPDATE

Many beginners ask:

> How does the same method perform both INSERT and UPDATE?

Hibernate checks the entity's primary key.

```text
ID == null

↓

INSERT

━━━━━━━━━━━━━━━━━━

ID exists

↓

UPDATE
```

Example

New Employee

```java
Employee employee = new Employee();
```

ID

```text
null
```

↓

INSERT

---

Existing Employee

```java
employee.setId(1L);
```

↓

UPDATE

(We'll study Entity Lifecycle later for the complete explanation.)

---

# 🏗️ Our Project Architecture

```text
Client

↓

Controller

↓

Service

↓

EmployeeRepository

↓

Spring Data JPA

↓

Hibernate

↓

JDBC

↓

PostgreSQL
```

Notice

The Service never communicates with Hibernate directly.

Everything goes through the Repository.

---

# 🧠 One Amazing Feature

Suppose you add

```java
Optional<Employee> findByEmail(String email);
```

No implementation.

No SQL.

No JPQL.

Nothing.

Spring automatically understands

```text
find

↓

By

↓

Email
```

and generates the query.

We'll master derived query methods in later chapters.

---

# 💡 Why Spring Data JPA Became So Popular

Without Spring Data JPA

* DAO classes
* EntityManager code
* CRUD implementation
* Query implementation

Lots of repetitive code.

With Spring Data JPA

Most of that disappears.

Developers focus on business logic instead of infrastructure code.

---

# 💡 Best Practices

✅ Extend `JpaRepository` for most Spring Boot applications.

✅ Keep repositories focused on database operations only.

✅ Put business logic in the Service layer.

✅ Prefer derived query methods before writing custom queries.

---

# 🎤 Interview Notes

### Q1. What is Spring Data JPA?

Spring Data JPA is a Spring framework that simplifies database access by providing automatic repository implementations on top of JPA.

---

### Q2. Does Spring Data JPA replace Hibernate?

No.

Spring Data JPA uses Hibernate (or another JPA provider) internally.

---

### Q3. Why do we extend JpaRepository?

To inherit ready-made CRUD, pagination, sorting, and JPA-specific methods without writing implementations.

---

### Q4. Who implements EmployeeRepository?

Spring Data JPA creates the implementation automatically at application startup.

---

### Q5. Is JpaRepository a class?

No.

It is an interface.

Spring generates the implementation at runtime.

---

# 📝 Things to Remember

✔ Spring Data JPA sits on top of JPA.

✔ JPA sits on top of Hibernate.

✔ Hibernate uses JDBC.

✔ We only create repository interfaces.

✔ Spring automatically creates repository implementations.

✔ `JpaRepository<Entity, ID>` needs the Entity type and Primary Key type.

✔ `save()` performs INSERT or UPDATE depending on the entity state.

✔ Business logic belongs in the Service layer, not in the Repository.

---

# 🧠 Final Mental Model

Never forget this complete stack.

```text
Controller
        │
        ▼
Service
        │
        ▼
Repository Interface
(EmployeeRepository)
        │
        ▼
Spring Data JPA
(Auto Implementation)
        │
        ▼
JPA
(Specification)
        │
        ▼
Hibernate
(ORM Engine)
        │
        ▼
JDBC
(Database Communication)
        │
        ▼
PostgreSQL
```

---

# 🔗 Chapter Connection

We now understand the entire technology stack used in our project.

The next chapter will shift from theory to **our own application architecture**.

We'll analyze every package we created:

```text
controller/

service/

repository/

entity/

dto/

util/

exception/
```

You'll understand **why each layer exists**, **what its responsibility is**, and **how a request flows through all of them** before we move on to mastering advanced repository features like derived queries, pagination, sorting, JPQL, Specifications, and performance optimization.
