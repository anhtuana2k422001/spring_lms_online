# Spring LMS Online - Hệ thống Quản lý Học tập Trực tuyến

## 📋 Tổng quan

Spring LMS Online là một hệ thống quản lý học tập trực tuyến (Learning Management System) được xây dựng bằng Spring Boot với kiến trúc multi-module. Hệ thống cung cấp các tính năng quản lý khóa học, người dùng, và các hoạt động học tập trực tuyến.

## 🏗️ Kiến trúc Hệ thống

### Kiến trúc Multi-Module

Dự án được tổ chức theo mô hình **Clean Architecture** với 4 module chính:


### Chi tiết các Module:

#### 🎯 **API Module** (Presentation Layer)
- **Chức năng**: Xử lý HTTP requests, REST APIs
- **Thành phần chính**:
  - Controllers: `AuthController`, `UserController`, `CourseController`
  - DTOs: `LoginDto`, `UserRegistrationDto`
  - Security Configuration: `SecurityConfig`
  - Main Application: `LmsApplication`

#### 🧠 **Core Module** (Domain Layer)
- **Chức năng**: Business logic, Domain entities
- **Thành phần chính**:
  - Entities: `User`, `Course`
  - Service Interfaces: `UserService`, `CourseService`
  - Repository Interfaces: `UserRepository`, `CourseRepository`

#### 🔧 **Infra Module** (Infrastructure Layer)
- **Chức năng**: Implementation của services, database configurations
- **Thành phần chính**:
  - Service Implementations: `UserServiceImpl`, `CourseServiceImpl`
  - Database configurations
  - External integrations

#### 🔗 **Common Module** (Shared Layer)
- **Chức năng**: Shared utilities và common components
- **Thành phần chính**:
  - `ApiResponse`: Standardized API response format
  - Common utilities và exceptions

## 🛠️ Công nghệ sử dụng

### Backend Stack:
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (Authentication & Authorization)
- **Spring Data JPA** (Database ORM)
- **PostgreSQL** (Primary Database)
- **Redis** (Caching)
- **Apache Kafka** (Message Broker)
- **JWT** (JSON Web Tokens)
- **Lombok** (Code Generation)
- **Gradle** (Build Tool)

### DevOps & Infrastructure:
- **Docker & Docker Compose**
- **Swagger/OpenAPI** (API Documentation)

## 🚀 Hướng dẫn chạy dự án

### Yêu cầu hệ thống:
- Java 17+
- Docker & Docker Compose
- Gradle 8.0+

### Bước 1: Clone dự án
```bash
git clone <repository-url>
cd spring-lms-online

# Khởi động tất cả services (app, database, redis, kafka)
docker-compose up -d

# Xem logs
docker-compose logs -f app

# Dừng services
docker-compose down

# Khởi động database và dependencies
docker-compose up -d postgres redis kafka

# Build project
./gradlew build

# Chạy application
./gradlew :api:bootRun

### Bước 4: Truy cập ứng dụng
- API Base URL : http://localhost:8080
- Swagger UI : http://localhost:8080/swagger-ui.html
- Database : localhost:5433 (PostgreSQL)
- Redis : localhost:6379
- Kafka : localhost:9092

POST /api/auth/register    # Đăng ký tài khoản
POST /api/auth/login       # Đăng nhập

User Management APIs:
GET  /api/users/{id}       # Lấy thông tin user
PUT  /api/users/{id}       # Cập nhật thông tin user
DELETE /api/users/{id}     # Xóa user

Course Management APIs:
GET  /api/courses          # Danh sách khóa học
POST /api/courses          # Tạo khóa học mới
GET  /api/courses/{id}     # Chi tiết khóa học
PUT  /api/courses/{id}     # Cập nhật khóa học
DELETE /api/courses/{id}   # Xóa khóa học

## Database Schema
### Các bảng chính:
- users : Quản lý thông tin người dùng
- courses : Quản lý thông tin khóa học
- enrollments : Quản lý đăng ký khóa học
- lessons : Quản lý bài học
- assignments : Quản lý bài tập

🔧 Configuration
Environment Variables:
DB_USERNAME=lms_user
DB_PASSWORD=lms_password
REDIS_PASSWORD=
JWT_SECRET=myVerySecretJWTKey

### Application Properties:
- Database: PostgreSQL (Port: 5433)
- Redis: Port 6379
- Kafka: Port 9092
- Application: Port 8080

## 🧪 Testing
# Chạy tất cả tests
./gradlew test

# Chạy tests cho module cụ thể
./gradlew :api:test
./gradlew :core:test

## 📝 Development Guidelines
### Code Structure:
1. Controllers chỉ xử lý HTTP requests/responses
2. Services chứa business logic
3. Repositories chỉ xử lý data access
4. DTOs cho data transfer giữa layers

### Best Practices:
- Sử dụng @Valid cho validation
- Implement proper error handling
- Follow RESTful API conventions
- Use standardized ApiResponse format

## 🔒 Security
- JWT Authentication : Stateless authentication
- Password Encryption : BCrypt
- CORS Configuration : Configured for frontend integration
- Input Validation : Bean Validation (JSR-303)

## 📚 Documentation
- API Documentation : Swagger UI tại http://localhost:8080/swagger-ui.html
- Code Documentation : JavaDoc comments

## 🤝 Contributing
1. Fork the repository
2. Create feature branch ( git checkout -b feature/amazing-feature )
3. Commit changes ( git commit -m 'Add amazing feature' )
4. Push to branch ( git push origin feature/amazing-feature )
5. Open Pull Request

## 📄 License
This project is licensed under the MIT License.
Phiên bản : 1.0.0 Tác giả : LMS Development Team Cập nhật lần cuối : 2025

File README.md này bao gồm tất cả thông tin quan trọng về dự án:

✅ **Tổng quan dự án** và mục đích  
✅ **Kiến trúc chi tiết** với 4 module (api, core, infra, common)  
✅ **Stack công nghệ** đầy đủ (Spring Boot, PostgreSQL, Redis, Kafka...)  
✅ **Hướng dẫn setup** từng bước chi tiết  
✅ **API endpoints** và cách sử dụng  
✅ **Database schema** và cấu hình  
✅ **Testing guidelines** và best practices  
✅ **Security features** và documentation  
