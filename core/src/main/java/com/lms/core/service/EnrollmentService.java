package com.lms.core.service;

import com.lms.core.domain.Enrollment;
import com.lms.core.domain.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentService {
    Enrollment enrollStudent(UUID studentId, UUID courseId);
    Optional<Enrollment> findById(UUID id);
    Optional<Enrollment> findByStudentAndCourse(UUID studentId, UUID courseId);
    Page<Enrollment> findByStudentId(UUID studentId, Pageable pageable);
    Page<Enrollment> findByCourseId(UUID courseId, Pageable pageable);
    Enrollment updateProgress(UUID enrollmentId, Double progress);
    Enrollment updateStatus(UUID enrollmentId, EnrollmentStatus status);
    void unenrollStudent(UUID studentId, UUID courseId);
    boolean isStudentEnrolled(UUID studentId, UUID courseId);
    Long getEnrollmentCount(UUID courseId, EnrollmentStatus status);
}