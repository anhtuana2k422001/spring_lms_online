package com.lms.infra.service;

import com.lms.common.exception.ResourceNotFoundException;
import com.lms.core.domain.Enrollment;
import com.lms.core.domain.EnrollmentStatus;
import com.lms.core.domain.User;
import com.lms.core.domain.Course;
import com.lms.core.repository.EnrollmentRepository;
import com.lms.core.repository.UserRepository;
import com.lms.core.repository.CourseRepository;
import com.lms.core.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    
    @Override
    public Enrollment enrollStudent(UUID studentId, UUID courseId) {
        // Check if already enrolled
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }
        
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgress(0.0);
        
        return enrollmentRepository.save(enrollment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Enrollment> findById(UUID id) {
        return enrollmentRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Enrollment> findByStudentAndCourse(UUID studentId, UUID courseId) {
        return enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Enrollment> findByStudentId(UUID studentId, Pageable pageable) {
        return enrollmentRepository.findByStudent_Id(studentId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Enrollment> findByCourseId(UUID courseId, Pageable pageable) {
        return enrollmentRepository.findByCourse_Id(courseId, pageable);
    }
    
    @Override
    public Enrollment updateProgress(UUID enrollmentId, Double progress) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));
        
        enrollment.setProgress(Math.min(100.0, Math.max(0.0, progress)));
        
        // Auto-complete if progress reaches 100%
        if (progress >= 100.0 && enrollment.getStatus() == EnrollmentStatus.ACTIVE) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollment.setCompletedAt(LocalDateTime.now());
        }
        
        return enrollmentRepository.save(enrollment);
    }
    
    @Override
    public Enrollment updateStatus(UUID enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));
        
        enrollment.setStatus(status);
        
        if (status == EnrollmentStatus.COMPLETED && enrollment.getCompletedAt() == null) {
            enrollment.setCompletedAt(LocalDateTime.now());
        }
        
        return enrollmentRepository.save(enrollment);
    }
    
    @Override
    public void unenrollStudent(UUID studentId, UUID courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for student: " + studentId + " and course: " + courseId));
        
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(UUID studentId, UUID courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getEnrollmentCount(UUID courseId, EnrollmentStatus status) {
        return enrollmentRepository.countByCourseIdAndStatus(courseId, status);
    }
}