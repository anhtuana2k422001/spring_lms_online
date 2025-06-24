package com.lms.core.service;

import com.lms.core.domain.Quiz;
import com.lms.core.domain.QuizAttempt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizService {
    Quiz createQuiz(Quiz quiz);
    Optional<Quiz> findById(UUID id);
    List<Quiz> findByCourseId(UUID courseId);
    List<Quiz> findByLessonId(UUID lessonId);
    List<Quiz> findAvailableQuizzes();
    Quiz updateQuiz(UUID id, Quiz quiz);
    void deleteQuiz(UUID id);
    
    // Quiz Attempt methods
    QuizAttempt startQuizAttempt(UUID studentId, UUID quizId);
    QuizAttempt submitQuizAttempt(UUID attemptId, List<UUID> selectedAnswerIds);
    List<QuizAttempt> getStudentAttempts(UUID studentId, UUID quizId);
    Optional<QuizAttempt> getBestAttempt(UUID studentId, UUID quizId);
    boolean canStudentTakeQuiz(UUID studentId, UUID quizId);
}