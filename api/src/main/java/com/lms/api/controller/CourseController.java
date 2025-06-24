package com.lms.api.controller;

import com.lms.common.response.ApiResponse;
import com.lms.core.domain.Course;
import com.lms.core.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    
    private final CourseService courseService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to create course: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable UUID id) {
        try {
            Optional<Course> courseOpt = courseService.findById(id);

            return courseOpt.map(course -> ResponseEntity.ok(ApiResponse.success(course))).orElseGet(()
                    -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Course not found"))
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get course: " + e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Course>>> getAllCourses(Pageable pageable) {
        try {
            Page<Course> courses = courseService.findAll(pageable);
            return ResponseEntity.ok(ApiResponse.success(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get courses: " + e.getMessage()));
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Course>>> searchCourses(
            @RequestParam String keyword, 
            Pageable pageable) {
        try {
            Page<Course> courses = courseService.searchCourses(keyword, pageable);
            return ResponseEntity.ok(ApiResponse.success(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to search courses: " + e.getMessage()));
        }
    }
    
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<ApiResponse<List<Course>>> getCoursesByInstructor(@PathVariable UUID instructorId) {
        try {
            List<Course> courses = courseService.findByInstructorId(instructorId);
            return ResponseEntity.ok(ApiResponse.success(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get instructor courses: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(@PathVariable UUID id, @RequestBody Course course) {
        try {
            Course updatedCourse = courseService.updateCourse(id, course);
            return ResponseEntity.ok(ApiResponse.success(updatedCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to update course: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCourse(@PathVariable UUID id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(ApiResponse.success("Course deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to delete course: " + e.getMessage()));
        }
    }
}