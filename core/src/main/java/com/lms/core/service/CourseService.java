package com.lms.core.service;

import com.lms.core.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {
    Course createCourse(Course course);
    Optional<Course> findById(UUID id);
    Page<Course> findAll(Pageable pageable);
    Page<Course> searchCourses(String keyword, Pageable pageable);
    List<Course> findByInstructorId(UUID instructorId);
    Course updateCourse(UUID id, Course course);
    void deleteCourse(UUID id);
}