package com.lms.core.repository;

import com.lms.core.domain.Course;
import com.lms.core.domain.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
    List<Course> findByInstructorId(UUID instructorId);
    
    @Query("SELECT c FROM Course c WHERE c.title LIKE %:keyword% OR c.description LIKE %:keyword%")
    Page<Course> searchByKeyword(String keyword, Pageable pageable);
}