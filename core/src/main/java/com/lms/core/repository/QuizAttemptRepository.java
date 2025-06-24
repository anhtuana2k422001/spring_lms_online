package com.lms.core.repository;

import com.lms.core.domain.QuizAttempt;
import com.lms.core.domain.AttemptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {
    List<QuizAttempt> findByUser_IdAndQuiz_Id(UUID studentId, UUID quizId);
    List<QuizAttempt> findByUser_Id(UUID studentId);
    List<QuizAttempt> findByQuiz_Id(UUID quizId);
    
    @Query("SELECT COUNT(qa) FROM QuizAttempt qa WHERE qa.user.id = :studentId AND qa.quiz.id = :quizId")
    Long countByStudentIdAndQuizId(UUID studentId, UUID quizId);
    
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.user.id = :studentId AND qa.quiz.id = :quizId ORDER BY qa.score DESC")
    Optional<QuizAttempt> findBestAttempt(UUID studentId, UUID quizId);
    
    List<QuizAttempt> findByStatus(AttemptStatus status);
}