package com.lms.core.repository;

import com.lms.core.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    List<Quiz> findByCourse_Id(UUID courseId);
    List<Quiz> findByLesson_Id(UUID lessonId);
    List<Quiz> findByIsActiveTrue();
    
    @Query("SELECT q FROM Quiz q WHERE q.course.id = :courseId AND q.isActive = true")
    List<Quiz> findActiveByCourseId(UUID courseId);
    
    @Query("SELECT q FROM Quiz q WHERE q.availableFrom <= :now AND (q.availableUntil IS NULL OR q.availableUntil >= :now) AND q.isActive = true")
    List<Quiz> findAvailableQuizzes(LocalDateTime now);
}