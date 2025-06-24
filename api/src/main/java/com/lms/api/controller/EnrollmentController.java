package com.lms.api.controller;

import com.lms.common.response.ApiResponse;
import com.lms.core.domain.Enrollment;
import com.lms.core.domain.EnrollmentStatus;
import com.lms.core.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse<Enrollment>> enrollStudent(
            @RequestParam UUID studentId, 
            @RequestParam UUID courseId) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(enrollment));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to enroll student: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Enrollment>> getEnrollmentById(@PathVariable UUID id) {
        try {
            Optional<Enrollment> enrollmentOpt = enrollmentService.findById(id);

            return enrollmentOpt.map(enrollment -> ResponseEntity.ok(ApiResponse.success(enrollment))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Enrollment not found")));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get enrollment: " + e.getMessage()));
        }
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<Page<Enrollment>>> getStudentEnrollments(
            @PathVariable UUID studentId, 
            Pageable pageable) {
        try {
            Page<Enrollment> enrollments = enrollmentService.findByStudentId(studentId, pageable);
            return ResponseEntity.ok(ApiResponse.success(enrollments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get student enrollments: " + e.getMessage()));
        }
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<Page<Enrollment>>> getCourseEnrollments(
            @PathVariable UUID courseId, 
            Pageable pageable) {
        try {
            Page<Enrollment> enrollments = enrollmentService.findByCourseId(courseId, pageable);
            return ResponseEntity.ok(ApiResponse.success(enrollments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get course enrollments: " + e.getMessage()));
        }
    }
    
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkEnrollment(
            @RequestParam UUID studentId, 
            @RequestParam UUID courseId) {
        try {
            boolean isEnrolled = enrollmentService.isStudentEnrolled(studentId, courseId);
            return ResponseEntity.ok(ApiResponse.success(isEnrolled));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to check enrollment: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/progress")
    public ResponseEntity<ApiResponse<Enrollment>> updateProgress(
            @PathVariable UUID id, 
            @RequestParam Double progress) {
        try {
            Enrollment updatedEnrollment = enrollmentService.updateProgress(id, progress);
            return ResponseEntity.ok(ApiResponse.success(updatedEnrollment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to update progress: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Enrollment>> updateStatus(
            @PathVariable UUID id, 
            @RequestParam EnrollmentStatus status) {
        try {
            Enrollment updatedEnrollment = enrollmentService.updateStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success(updatedEnrollment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to update status: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/unenroll")
    public ResponseEntity<ApiResponse<String>> unenrollStudent(
            @RequestParam UUID studentId, 
            @RequestParam UUID courseId) {
        try {
            enrollmentService.unenrollStudent(studentId, courseId);
            return ResponseEntity.ok(ApiResponse.success("Student unenrolled successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to unenroll student: " + e.getMessage()));
        }
    }
    
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<ApiResponse<Long>> getEnrollmentCount(
            @PathVariable UUID courseId, 
            @RequestParam(required = false) EnrollmentStatus status) {
        try {
            Long count = enrollmentService.getEnrollmentCount(courseId, status);
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get enrollment count: " + e.getMessage()));
        }
    }
}