package com.lms.core.repository;

import com.lms.core.domain.Enrollment;
import com.lms.core.domain.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    Optional<Enrollment> findByStudent_IdAndCourse_Id(UUID studentId, UUID courseId);
    List<Enrollment> findByStudent_Id(UUID studentId);
    List<Enrollment> findByCourse_Id(UUID courseId);
    Page<Enrollment> findByStudent_Id(UUID studentId, Pageable pageable);
    Page<Enrollment> findByCourse_Id(UUID courseId, Pageable pageable);
    List<Enrollment> findByStatus(EnrollmentStatus status);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = :status")
    Long countByCourseIdAndStatus(UUID courseId, EnrollmentStatus status);
    
    boolean existsByStudentIdAndCourseId(UUID studentId, UUID courseId);
}