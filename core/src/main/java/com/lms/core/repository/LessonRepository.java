package com.lms.core.repository;

import com.lms.core.domain.Lesson;
import com.lms.core.domain.LessonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    List<Lesson> findByCourse_IdOrderByOrderIndex(UUID courseId);
    List<Lesson> findByCourse_IdAndType(UUID courseId, LessonType type);
    
    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId AND l.orderIndex > :orderIndex ORDER BY l.orderIndex ASC")
    List<Lesson> findNextLessons(UUID courseId, Integer orderIndex);
    
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course.id = :courseId")
    Long countByCourseId(UUID courseId);
}