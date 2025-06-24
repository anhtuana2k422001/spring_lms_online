# Spring LMS Online - API Curl Commands

## 🔐 Authentication APIs

### Đăng ký tài khoản
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "0123456789"
  }'

### Đăng nhập
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'

## 👤 User Management APIs

### Lấy thông tin user theo ID
curl -X GET http://localhost:8080/api/users/{userId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Cập nhật thông tin user
curl -X PUT http://localhost:8080/api/users/{userId} \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "firstName": "Updated Name",
    "lastName": "Updated Last",
    "phoneNumber": "0987654321"
  }'

### Xóa user
curl -X DELETE http://localhost:8080/api/users/{userId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

## 📚 Course Management APIs

### Tạo khóa học mới
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Java Programming",
    "description": "Learn Java from basics",
    "instructorId": "instructor-uuid",
    "price": 99.99
  }'

### Lấy thông tin khóa học theo ID
curl -X GET http://localhost:8080/api/courses/{courseId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy danh sách tất cả khóa học (có phân trang)
curl -X GET "http://localhost:8080/api/courses?page=0&size=10&sort=title,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Tìm kiếm khóa học
curl -X GET "http://localhost:8080/api/courses/search?keyword=java&page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy khóa học theo instructor
curl -X GET http://localhost:8080/api/courses/instructor/{instructorId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Cập nhật khóa học
curl -X PUT http://localhost:8080/api/courses/{courseId} \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Advanced Java Programming",
    "description": "Updated description",
    "price": 149.99
  }'

### Xóa khóa học
curl -X DELETE http://localhost:8080/api/courses/{courseId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

## 📝 Enrollment APIs

### Đăng ký học khóa học
curl -X POST "http://localhost:8080/api/enrollments/enroll?studentId={studentId}&courseId={courseId}" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy thông tin enrollment theo ID
curl -X GET http://localhost:8080/api/enrollments/{enrollmentId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy danh sách enrollment của student
curl -X GET "http://localhost:8080/api/enrollments/student/{studentId}?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy danh sách enrollment của khóa học
curl -X GET "http://localhost:8080/api/enrollments/course/{courseId}?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Kiểm tra student đã đăng ký khóa học chưa
curl -X GET "http://localhost:8080/api/enrollments/check?studentId={studentId}&courseId={courseId}" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Cập nhật tiến độ học
curl -X PUT "http://localhost:8080/api/enrollments/{enrollmentId}/progress?progress=75.5" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Cập nhật trạng thái enrollment
curl -X PUT "http://localhost:8080/api/enrollments/{enrollmentId}/status?status=COMPLETED" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Hủy đăng ký khóa học
curl -X DELETE "http://localhost:8080/api/enrollments/unenroll?studentId={studentId}&courseId={courseId}" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Đếm số lượng enrollment của khóa học
curl -X GET "http://localhost:8080/api/enrollments/course/{courseId}/count?status=ACTIVE" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

## 📖 Lesson Management APIs

### Tạo bài học mới
curl -X POST http://localhost:8080/api/lessons \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Introduction to Variables",
    "content": "Lesson content here",
    "courseId": "course-uuid",
    "type": "VIDEO",
    "orderIndex": 1
  }'

### Lấy thông tin bài học theo ID
curl -X GET http://localhost:8080/api/lessons/{lessonId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy danh sách bài học theo khóa học
curl -X GET http://localhost:8080/api/lessons/course/{courseId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy bài học theo khóa học và loại
curl -X GET http://localhost:8080/api/lessons/course/{courseId}/type/VIDEO \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Cập nhật bài học
curl -X PUT http://localhost:8080/api/lessons/{lessonId} \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Updated Lesson Title",
    "content": "Updated content"
  }'

### Sắp xếp lại thứ tự bài học
curl -X POST http://localhost:8080/api/lessons/course/{courseId}/reorder \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '["lesson-id-1", "lesson-id-2", "lesson-id-3"]'

### Xóa bài học
curl -X DELETE http://localhost:8080/api/lessons/{lessonId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Đếm số lượng bài học trong khóa học
curl -X GET http://localhost:8080/api/lessons/course/{courseId}/count \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

## 🧩 Quiz Management APIs

### Tạo quiz mới
curl -X POST http://localhost:8080/api/quizzes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Java Basics Quiz",
    "description": "Test your Java knowledge",
    "courseId": "course-uuid",
    "lessonId": "lesson-uuid",
    "timeLimit": 30,
    "maxAttempts": 3
  }'

### Lấy thông tin quiz theo ID
curl -X GET http://localhost:8080/api/quizzes/{quizId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy danh sách quiz theo khóa học
curl -X GET http://localhost:8080/api/quizzes/course/{courseId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy danh sách quiz theo bài học
curl -X GET http://localhost:8080/api/quizzes/lesson/{lessonId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy danh sách quiz có sẵn
curl -X GET http://localhost:8080/api/quizzes/available \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Cập nhật quiz
curl -X PUT http://localhost:8080/api/quizzes/{quizId} \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Updated Quiz Title",
    "timeLimit": 45
  }'

### Xóa quiz
curl -X DELETE http://localhost:8080/api/quizzes/{quizId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

## 🎯 Quiz Attempt APIs

### Bắt đầu làm quiz
curl -X POST "http://localhost:8080/api/quizzes/{quizId}/attempts/start?studentId={studentId}" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Nộp bài quiz
curl -X POST http://localhost:8080/api/quizzes/attempts/{attemptId}/submit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '["answer-id-1", "answer-id-2", "answer-id-3"]'

### Lấy danh sách lần làm quiz của student
curl -X GET http://localhost:8080/api/quizzes/{quizId}/attempts/student/{studentId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Lấy lần làm quiz tốt nhất của student
curl -X GET http://localhost:8080/api/quizzes/{quizId}/attempts/student/{studentId}/best \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

### Kiểm tra student có thể làm quiz không
curl -X GET "http://localhost:8080/api/quizzes/{quizId}/can-take?studentId={studentId}" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

## 📋 Lưu ý quan trọng:

1. **JWT Token**: Thay thế `YOUR_JWT_TOKEN` bằng token thực tế nhận được từ API login
2. **UUID Parameters**: Thay thế các `{userId}`, `{courseId}`, `{lessonId}`, etc. bằng UUID thực tế
3. **Base URL**: Đảm bảo ứng dụng đang chạy trên `http://localhost:8080`
4. **Content-Type**: Luôn sử dụng `application/json` cho các request có body
5. **Authorization**: Hầu hết các API đều yêu cầu JWT token trong header Authorization

Tất cả các API đều trả về response theo format:
```json
{
  "success": "true/false",
  "message": "Success/Error message",
  "data": "{...}"
}
```

## 🚀 Ví dụ sử dụng thực tế:

### 1. Đăng ký và đăng nhập
```bash
# Đăng ký user mới
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "securePassword123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "0123456789"
  }'

# Đăng nhập để lấy token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "securePassword123"
  }'
```

### 2. Tạo khóa học và bài học
```bash
# Tạo khóa học mới (cần token từ login)
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "title": "Spring Boot Fundamentals",
    "description": "Learn Spring Boot from scratch",
    "instructorId": "550e8400-e29b-41d4-a716-446655440000",
    "price": 199.99
  }'

# Tạo bài học cho khóa học
curl -X POST http://localhost:8080/api/lessons \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "title": "Introduction to Spring Boot",
    "content": "In this lesson, we will learn the basics of Spring Boot...",
    "courseId": "550e8400-e29b-41d4-a716-446655440001",
    "type": "VIDEO",
    "orderIndex": 1
  }'
```

### 3. Đăng ký khóa học và theo dõi tiến độ
```bash
# Student đăng ký khóa học
curl -X POST "http://localhost:8080/api/enrollments/enroll?studentId=550e8400-e29b-41d4-a716-446655440002&courseId=550e8400-e29b-41d4-a716-446655440001" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# Cập nhật tiến độ học
curl -X PUT "http://localhost:8080/api/enrollments/550e8400-e29b-41d4-a716-446655440003/progress?progress=50.0" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
