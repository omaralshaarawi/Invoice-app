# Invoice & Provider Management System

This project is a **Java Spring Boot**-based accounting management system for managing **users, providers, invoices, and invoice lines**.  
It includes **role-based authentication and authorization** and **REST APIs** for CRUD operations, and supports **MySQL** (production) and **H2** (development/testing) databases.  
The application is fully tested using **REST Assured**.

---

## **Features**

### 1. Authentication & Authorization
- Implemented with **Spring Security**.
- Roles:
  - **Admin** – Full access.
  - **Chef Accountant** – All actions except system configuration.
  - **Accountant** – Can only add invoices and invoice details.
- Secured endpoints using:
  - `@PreAuthorize`
  - `@Secured`
- Passwords stored securely with **BCrypt**.
- JWT-based authentication with a configurable **token secret**.

---

### 2. User Management
- Endpoints: `/users`
- Features:
  - Create new users with default **ACCOUNTANT** role.
  - Update user details and roles (ACCOUNTANT, CHEF_ACCOUNTANT, ADMIN).
  - Get user by ID.
  - Get all users (Admin only).
  - Delete users (with confirmation flag).

**Example – Create a user:**
```http
POST /users
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Example – Login:**
```http
POST /login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

---

### 3. Provider Management (CRUD)
- Endpoints: `/providers`
- Features:
  - Create, retrieve, update, and delete providers.
  - Fields: `name`, `phone`, `service`, `note`, `address`.
  - Pagination for listing all providers.
  - Admin role restriction for some operations.

**Example – Create a provider:**
```http
POST /providers
Content-Type: application/json

{
  "name": "Provider A",
  "phone": 123456789,
  "service": "IT Support",
  "note": "Preferred vendor",
  "address": "123 Street"
}
```

---

### 4. Invoice Management (CRUD)
- Endpoints: `/invoice`
- Features:
  - Create invoices with fields: `providerName`, `dateTime`, `address`, `paid`, `delivered_by`, and `invoiceLines`.
  - Retrieve invoices by ID, Provider Name, Date, or list all (paginated).
  - Update invoice details.
  - Delete invoices (requires confirmation).
  - Automatic calculations:
    - `Total` = sum of invoice lines
    - `Remaining` = `Total - Paid`

**Example – Create an invoice:**
```http
POST /invoice
Content-Type: application/json

{
  "providerName": "Provider A",
  "dateTime": "2025-08-11T10:30:00Z",
  "address": "123 Street",
  "paid": 200,
  "delivered_by": "CourierX",
  "invoiceLines": [
    {
      "productName": "Laptop",
      "quantity": 2,
      "price": 750
    },
    {
      "productName": "Mouse",
      "quantity": 5,
      "price": 20
    }
  ]
}
```

---

### 5. Invoice Line Management (CRUD)
- Endpoints: `/invoiceLine`
- Features:
  - Create invoice lines linked to an invoice.
  - Retrieve invoice lines by Invoice ID & Line ID.
  - Update invoice lines with recalculated totals & remaining balances.
  - Delete invoice lines (requires confirmation).
  - Fields: `productName`, `quantity`, `price`, `invoiceId`, `line`.

**Example – Create an invoice line:**
```http
POST /invoiceLine/{invoiceId}
Content-Type: application/json

{
  "productName": "Keyboard",
  "quantity": 3,
  "price": 45,
  "invoiceId": "{invoiceId}"
}
```

---

## **Tech Stack**
- **Java 17**
- **Spring Boot**
- **Spring Security** (JWT, BCrypt, role-based access)
- **Hibernate / JPA**
- **MySQL** (Production)
- **H2 Database** (Development & Testing)
- **Maven**
- **REST Assured** (API testing)

---

## **Installation & Setup**

### 1. Clone the repository
```bash
git clone https://github.com/your-username/invoice-provider-management.git
cd invoice-provider-management
```

### 2. Configure the Database

#### MySQL (Production)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/invoice_app
spring.datasource.username=your_mysql_user
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
```

#### H2 (Development/Testing)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Set your JWT secret:
```properties
tokenSecret=your-secret-key
```

---

### 3. Run the application
```bash
mvn spring-boot:run
```

---

## **API Endpoint Summary**

### Authentication
| Method | Endpoint       | Description          |
|--------|---------------|----------------------|
| POST   | `/login`      | Authenticate user    |
| POST   | `/logout`     | Logout user          |

### Users
| Method | Endpoint         | Description |
|--------|------------------|-------------|
| GET    | `/users/{id}`    | Get user by ID (Admin) |
| GET    | `/users`         | Get all users (Admin) |
| POST   | `/users`         | Create new user |
| PUT    | `/users/{id}`    | Update user |
| DELETE | `/users/{id}`    | Delete user |

### Providers
| Method | Endpoint         | Description |
|--------|------------------|-------------|
| GET    | `/providers/{id}`| Get provider by ID |
| GET    | `/providers`     | Get all providers |
| POST   | `/providers`     | Create provider |
| PUT    | `/providers/{id}`| Update provider |
| DELETE | `/providers/{id}`| Delete provider |

### Invoices
| Method | Endpoint                  | Description |
|--------|---------------------------|-------------|
| GET    | `/invoice/id/{id}`         | Get invoice by ID |
| GET    | `/invoice/provider-name/{name}` | Get invoices by provider name |
| GET    | `/invoice/date/{date}`     | Get invoices by date |
| GET    | `/invoice`                 | Get all invoices |
| POST   | `/invoice`                 | Create invoice |
| PUT    | `/invoice/{id}`            | Update invoice |
| DELETE | `/invoice/{id}`            | Delete invoice |

### Invoice Lines
| Method | Endpoint                     | Description |
|--------|------------------------------|-------------|
| GET    | `/invoiceLine/{id}/{line}`    | Get invoice line by IDs |
| POST   | `/invoiceLine/{id}`           | Create invoice line |
| PUT    | `/invoiceLine/{id}/{line}`    | Update invoice line |
| DELETE | `/invoiceLine/{id}/{line}`    | Delete invoice line |

---

## **Testing**
The application uses **REST Assured** for automated integration testing.  
Run tests:
```bash
mvn test
```

---

## **Security**
- JWT token-based authentication.
- Role-based endpoint restrictions.
- BCrypt password encryption.
- Method-level security (`@PreAuthorize`, `@Secured`).

