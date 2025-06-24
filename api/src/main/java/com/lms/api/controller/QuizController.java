package com.lms.api.controller;

import com.lms.common.response.ApiResponse;
import com.lms.core.domain.Quiz;
import com.lms.core.domain.QuizAttempt;
import com.lms.core.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    
    private final QuizService quizService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Quiz>> createQuiz(@RequestBody Quiz quiz) {
        try {
            Quiz createdQuiz = quizService.createQuiz(quiz);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdQuiz));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to create quiz: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Quiz>> getQuizById(@PathVariable UUID id) {
        try {
            Optional<Quiz> quizOpt = quizService.findById(id);

            return quizOpt.map(quiz -> ResponseEntity.ok(ApiResponse.success(quiz))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Quiz not found")));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get quiz: " + e.getMessage()));
        }
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Quiz>>> getQuizzesByCourse(@PathVariable UUID courseId) {
        try {
            List<Quiz> quizzes = quizService.findByCourseId(courseId);
            return ResponseEntity.ok(ApiResponse.success(quizzes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get course quizzes: " + e.getMessage()));
        }
    }
    
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<List<Quiz>>> getQuizzesByLesson(@PathVariable UUID lessonId) {
        try {
            List<Quiz> quizzes = quizService.findByLessonId(lessonId);
            return ResponseEntity.ok(ApiResponse.success(quizzes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get lesson quizzes: " + e.getMessage()));
        }
    }
    
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Quiz>>> getAvailableQuizzes() {
        try {
            List<Quiz> quizzes = quizService.findAvailableQuizzes();
            return ResponseEntity.ok(ApiResponse.success(quizzes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get available quizzes: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Quiz>> updateQuiz(@PathVariable UUID id, @RequestBody Quiz quiz) {
        try {
            Quiz updatedQuiz = quizService.updateQuiz(id, quiz);
            return ResponseEntity.ok(ApiResponse.success(updatedQuiz));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to update quiz: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteQuiz(@PathVariable UUID id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.ok(ApiResponse.success("Quiz deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to delete quiz: " + e.getMessage()));
        }
    }
    
    // Quiz Attempt endpoints
    @PostMapping("/{quizId}/attempts/start")
    public ResponseEntity<ApiResponse<QuizAttempt>> startQuizAttempt(
            @PathVariable UUID quizId, 
            @RequestParam UUID studentId) {
        try {
            QuizAttempt attempt = quizService.startQuizAttempt(studentId, quizId);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(attempt));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to start quiz attempt: " + e.getMessage()));
        }
    }
    
    @PostMapping("/attempts/{attemptId}/submit")
    public ResponseEntity<ApiResponse<QuizAttempt>> submitQuizAttempt(
            @PathVariable UUID attemptId, 
            @RequestBody List<UUID> selectedAnswerIds) {
        try {
            QuizAttempt attempt = quizService.submitQuizAttempt(attemptId, selectedAnswerIds);
            return ResponseEntity.ok(ApiResponse.success(attempt));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to submit quiz attempt: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{quizId}/attempts/student/{studentId}")
    public ResponseEntity<ApiResponse<List<QuizAttempt>>> getStudentAttempts(
            @PathVariable UUID quizId, 
            @PathVariable UUID studentId) {
        try {
            List<QuizAttempt> attempts = quizService.getStudentAttempts(studentId, quizId);
            return ResponseEntity.ok(ApiResponse.success(attempts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get student attempts: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{quizId}/attempts/student/{studentId}/best")
    public ResponseEntity<ApiResponse<QuizAttempt>> getBestAttempt(
            @PathVariable UUID quizId, 
            @PathVariable UUID studentId) {
        try {
            Optional<QuizAttempt> attemptOpt = quizService.getBestAttempt(studentId, quizId);

            return attemptOpt.map(quizAttempt -> ResponseEntity.ok(ApiResponse.success(quizAttempt))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("No attempts found")));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get best attempt: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{quizId}/can-take")
    public ResponseEntity<ApiResponse<Boolean>> canStudentTakeQuiz(
            @PathVariable UUID quizId, 
            @RequestParam UUID studentId) {
        try {
            boolean canTake = quizService.canStudentTakeQuiz(studentId, quizId);
            return ResponseEntity.ok(ApiResponse.success(canTake));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to check quiz eligibility: " + e.getMessage()));
        }
    }
}