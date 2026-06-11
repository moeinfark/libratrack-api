# LibraTrack API

A RESTful library management API built with Java and Spring Boot, featuring full CRUD operations, borrow record tracking, and advanced search across 4 domain entities.

## Tech Stack

Java · Spring Boot · Spring Data JPA / Hibernate · MySQL · Maven · Swagger / OpenAPI 3.0

## Features

- Book management with advanced multi-criteria and keyword search
- User management
- Borrow record tracking with availability checks and alternative copy suggestions
- Rate limiting
- Request/response logging
- Global exception handling with descriptive error responses
- API documentation via Swagger UI

## Getting Started

### Prerequisites
- Java 17+
- MySQL 8+
- Maven

### Setup

1. Clone the repository
2. Create a MySQL database: `CREATE DATABASE librarydb;`
3. Create a MySQL user and grant privileges:
```sql
CREATE USER 'library'@'localhost' IDENTIFIED BY 'yourpassword';
GRANT ALL PRIVILEGES ON librarydb.* TO 'library'@'localhost';
```
4. Copy `application.properties.example` to `application.properties` and fill in your credentials
5. Run:
```bash
./mvnw spring-boot:run
```
6. Open Swagger UI: http://localhost:8080/swagger-ui.html

## Endpoints

### Books `/api/books`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| POST | `/api/books` | Add a new book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |
| GET | `/api/books/search?keyword=` | Keyword search across all fields |
| GET | `/api/books/advance-search?name=&author=&isbn=` | Multi-criteria search |

### Users `/api/users`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Add a new user |
| PUT | `/api/users/{id}` | Update a user |
| DELETE | `/api/users/{id}` | Delete a user |
| GET | `/api/users/{id}/borrow-records` | Get borrow records for a user |
| GET | `/api/users/borrow-records` | Get all borrowed book IDs |

### Borrow Records `/api/borrow-records`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/borrow-records` | Get all borrow records |
| POST | `/api/borrow-records` | Borrow a book |
| DELETE | `/api/borrow-records/{bookId}` | Return a book |
| GET | `/api/borrow-records/borrowed/book-ids` | Get all currently borrowed book IDs |
| GET | `/api/borrow-records/available/book-ids` | Get all available book IDs |

## Example Requests

### Add a book
```json
POST /api/books
{
  "bookName": "Clean Code",
  "bookAuthor": "Robert C. Martin",
  "bookISBN": "978-0132350884",
  "isAvailable": true
}
```

### Add a user
```json
POST /api/users
{
  "firstName": "John",
  "lastName": "Doe",
  "userName": "johndoe"
}
```

### Borrow a book
```json
POST /api/borrow-records
{
  "userId": 1,
  "bookId": 1000001
}
```

## Database Schema

### Book
| Column | Type |
|--------|------|
| book_id | BIGINT (PK, starts at 1000001) |
| book_name | VARCHAR |
| book_author | VARCHAR |
| book_isbn | VARCHAR |
| is_available | BOOLEAN |

### User
| Column | Type |
|--------|------|
| id | BIGINT (PK) |
| first_name | VARCHAR |
| last_name | VARCHAR |
| user_name | VARCHAR |

### BorrowRecord
| Column | Type |
|--------|------|
| id | BIGINT (PK) |
| user_id | BIGINT |
| book_id | BIGINT |
| borrow_date | DATE |
| return_date | DATE |