package com.lms.infra.service;

import com.lms.common.exception.ResourceNotFoundException;
import com.lms.core.domain.*;
import com.lms.core.repository.*;
import com.lms.core.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizServiceImpl implements QuizService {
    
    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;
    
    @Override
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Quiz> findById(UUID id) {
        return quizRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Quiz> findByCourseId(UUID courseId) {
        return quizRepository.findByCourse_Id(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quiz> findByLessonId(UUID lessonId) {
        return quizRepository.findByLesson_Id(lessonId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Quiz> findAvailableQuizzes() {
        return quizRepository.findAvailableQuizzes(LocalDateTime.now());
    }
    
    @Override
    public Quiz updateQuiz(UUID id, Quiz quiz) {
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + id));
        
        existingQuiz.setTitle(quiz.getTitle());
        existingQuiz.setDescription(quiz.getDescription());
        existingQuiz.setTimeLimitMinutes(quiz.getTimeLimitMinutes());
        existingQuiz.setMaxAttempts(quiz.getMaxAttempts());
        existingQuiz.setPassingScore(quiz.getPassingScore());
        existingQuiz.setIsActive(quiz.getIsActive());
        existingQuiz.setAvailableFrom(quiz.getAvailableFrom());
        existingQuiz.setAvailableUntil(quiz.getAvailableUntil());
        
        return quizRepository.save(existingQuiz);
    }
    
    @Override
    public void deleteQuiz(UUID id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + id));
        quiz.setIsDeleted(true);
        quizRepository.save(quiz);
    }
    
    @Override
    public QuizAttempt startQuizAttempt(UUID studentId, UUID quizId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));
        
        // Check if student can take the quiz
        if (!canStudentTakeQuiz(studentId, quizId)) {
            throw new IllegalStateException("Student cannot take this quiz");
        }
        
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(student);  // Changed from setStudent to setUser
        attempt.setQuiz(quiz);
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setStatus(AttemptStatus.IN_PROGRESS);
        
        return quizAttemptRepository.save(attempt);
    }
    
    @Override
    public QuizAttempt submitQuizAttempt(UUID attemptId, List<UUID> selectedAnswerIds) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz attempt not found with id: " + attemptId));
        
        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Quiz attempt is not in progress");
        }
        
        // Calculate score (simplified - you might want to implement more complex scoring)
        double score = calculateScore(attempt.getQuiz(), selectedAnswerIds);
        
        attempt.setScore(score);
        attempt.setCompletedAt(LocalDateTime.now());  // Changed from setSubmittedAt to setCompletedAt
        attempt.setStatus(score >= attempt.getQuiz().getPassingScore() ? AttemptStatus.PASSED : AttemptStatus.FAILED);
        
        return quizAttemptRepository.save(attempt);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QuizAttempt> getStudentAttempts(UUID studentId, UUID quizId) {
        return quizAttemptRepository.findByUser_IdAndQuiz_Id(studentId, quizId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<QuizAttempt> getBestAttempt(UUID studentId, UUID quizId) {
        return quizAttemptRepository.findBestAttempt(studentId, quizId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canStudentTakeQuiz(UUID studentId, UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));
        
        // Check if quiz is active
        if (!quiz.getIsActive()) {
            return false;
        }
        
        // Check availability window
        LocalDateTime now = LocalDateTime.now();
        if (quiz.getAvailableFrom() != null && now.isBefore(quiz.getAvailableFrom())) {
            return false;
        }
        if (quiz.getAvailableUntil() != null && now.isAfter(quiz.getAvailableUntil())) {
            return false;
        }
        
        // Check attempt limit
        Long attemptCount = quizAttemptRepository.countByStudentIdAndQuizId(studentId, quizId);
        return quiz.getMaxAttempts() == null || attemptCount < quiz.getMaxAttempts();
    }
    
    private double calculateScore(Quiz quiz, List<UUID> selectedAnswerIds) {
        // Simplified scoring logic - implement based on your requirements
        // This would typically involve checking correct answers against selected answers
        return 0.0; // Placeholder
    }
}