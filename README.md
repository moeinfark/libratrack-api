# LibraTrack API
A RESTful library management API built with Java and Spring Boot, featuring JWT authentication, role-based access control, full CRUD operations, borrow record tracking, and advanced search across 4 domain entities.
## Tech Stack
Java · Spring Boot · Spring Security · Spring Data JPA / Hibernate · JWT · Lombok · MySQL · Maven · Swagger / OpenAPI 3.0
## Features
- JWT-based authentication with BCrypt password hashing
- Role-based access control (ADMIN / MEMBER)
- Book management with advanced multi-criteria and keyword search
- User management
- Borrow record tracking with availability checks and alternative copy suggestions
- Rate limiting
- Request/response logging
- Global exception handling with descriptive error responses
- Input validation on all endpoints
- API documentation via Swagger UI
## Getting Started
### Prerequisites
- Java 21+
- MySQL 8+
- Maven
### Setup
1. Clone the repository
2. Create a MySQL database:
   sql
   CREATE DATABASE librarydb;

3. Create a MySQL user and grant privileges:
   sql
   CREATE USER 'library'@'localhost' IDENTIFIED BY 'yourpassword';
   GRANT ALL PRIVILEGES ON librarydb.* TO 'library'@'localhost';

4. Add password and role columns to the user table:
   sql
   ALTER TABLE user ADD COLUMN password VARCHAR(255);
   ALTER TABLE user ADD COLUMN role VARCHAR(50);

5. Copy application.properties.example to application.properties and fill in your credentials
6. Run:
   bash
   ./mvnw spring-boot:run

7. Open Swagger UI: http://localhost:8080/swagger-ui.html
## Authentication
All endpoints except /api/auth/** and /swagger-ui/** require a valid JWT token.
### 1. Register
json
POST /api/auth/register
{
"firstName": "John",
"lastName": "Doe",
"userName": "johndoe",
"password": "yourpassword"
}

### 2. Login
json
POST /api/auth/login
{
"userName": "johndoe",
"password": "yourpassword"
}

Both return:
json
{
"token": "eyJhbGci...",
"userName": "johndoe",
"role": "MEMBER"
}

### 3. Use the token
Add the token to every request header:
Authorization: Bearer eyJhbGci...

In Postman: **Authorization tab → Bearer Token → paste token**
## Endpoints
### Auth /api/auth
| Method | Path | Description | Auth required |
|--------|------|-------------|---------------|
| POST | /api/auth/register | Register a new user | No |
| POST | /api/auth/login | Login and get token | No |
### Books /api/books
| Method | Path | Description | Auth required |
|--------|------|-------------|---------------|
| GET | /api/books | Get all books | Yes |
| GET | /api/books/{id} | Get book by ID | Yes |
| POST | /api/books | Add a new book | Yes |
| PUT | /api/books/{id} | Update a book | Yes |
| DELETE | /api/books/{id} | Delete a book | Yes |
| GET | /api/books/search?keyword= | Keyword search across all fields | Yes |
| GET | /api/books/advance-search?name=&author=&isbn= | Multi-criteria search | Yes |
### Users /api/users
| Method | Path | Description | Auth required |
|--------|------|-------------|---------------|
| GET | /api/users | Get all users | Yes |
| GET | /api/users/{id} | Get user by ID | Yes |
| POST | /api/users | Add a new user | Yes |
| PUT | /api/users/{id} | Update a user | Yes |
| DELETE | /api/users/{id} | Delete a user | Yes |
| GET | /api/users/{id}/borrow-records | Get borrow records for a user | Yes |
| GET | /api/users/borrow-records | Get all borrowed book IDs | Yes |
### Borrow Records /api/borrow-records
| Method | Path | Description | Auth required |
|--------|------|-------------|---------------|
| GET | /api/borrow-records | Get all borrow records | Yes |
| POST | /api/borrow-records | Borrow a book | Yes |
| DELETE | /api/borrow-records/{bookId} | Return a book | Yes |
| GET | /api/borrow-records/borrowed/book-ids | Get all currently borrowed book IDs | Yes |
| GET | /api/borrow-records/available/book-ids | Get all available book IDs | Yes |
## Example Requests
### Register
json
POST /api/auth/register
{
"firstName": "John",
"lastName": "Doe",
"userName": "johndoe",
"password": "yourpassword"
}

### Add a book
json
POST /api/books
{
"bookName": "Clean Code",
"bookAuthor": "Robert C. Martin",
"bookISBN": "978-0132350884"
}

### Add a user
json
POST /api/users
{
"firstName": "John",
"lastName": "Doe",
"userName": "johndoe"
}

### Borrow a book
json
POST /api/borrow-records
{
"userId": 1,
"bookId": 1000001
}

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
| user_name | VARCHAR (unique) |
| password | VARCHAR (BCrypt hashed) |
| role | VARCHAR (ADMIN / MEMBER) |
### BorrowRecord
| Column | Type |
|--------|------|
| id | BIGINT (PK) |
| user_id | BIGINT |
| book_id | BIGINT |
| borrow_date | DATE |
| return_date | DATE |

