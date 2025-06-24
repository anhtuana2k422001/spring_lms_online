package com.lms.infra.service;

import com.lms.common.exception.ResourceNotFoundException;
import com.lms.core.domain.Course;
import com.lms.core.repository.CourseRepository;
import com.lms.core.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {
    
    private final CourseRepository courseRepository;
    
    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(UUID id) {
        return courseRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Course> searchCourses(String keyword, Pageable pageable) {
        return courseRepository.searchByKeyword(keyword, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Course> findByInstructorId(UUID instructorId) {
        return courseRepository.findByInstructor_Id(instructorId);
    }
    
    @Override
    public Course updateCourse(UUID id, Course course) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        
        existingCourse.setTitle(course.getTitle());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setPrice(course.getPrice());
        existingCourse.setStatus(course.getStatus());
        existingCourse.setThumbnailUrl(course.getThumbnailUrl());
        
        return courseRepository.save(existingCourse);
    }
    
    @Override
    public void deleteCourse(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setIsDeleted(true);
        courseRepository.save(course);
    }
}