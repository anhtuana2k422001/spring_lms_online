package com.lms.api.controller;

import com.lms.common.response.ApiResponse;
import com.lms.core.domain.Lesson;
import com.lms.core.domain.LessonType;
import com.lms.core.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    
    private final LessonService lessonService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Lesson>> createLesson(@RequestBody Lesson lesson) {
        try {
            Lesson createdLesson = lessonService.createLesson(lesson);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdLesson));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to create lesson: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Lesson>> getLessonById(@PathVariable UUID id) {
        try {
            Optional<Lesson> lessonOpt = lessonService.findById(id);

            return lessonOpt.map(lesson -> ResponseEntity.ok(ApiResponse.success(lesson))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Lesson not found")));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get lesson: " + e.getMessage()));
        }
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Lesson>>> getLessonsByCourse(@PathVariable UUID courseId) {
        try {
            List<Lesson> lessons = lessonService.findByCourseId(courseId);
            return ResponseEntity.ok(ApiResponse.success(lessons));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get course lessons: " + e.getMessage()));
        }
    }
    
    @GetMapping("/course/{courseId}/type/{type}")
    public ResponseEntity<ApiResponse<List<Lesson>>> getLessonsByCourseAndType(
            @PathVariable UUID courseId, 
            @PathVariable LessonType type) {
        try {
            List<Lesson> lessons = lessonService.findByCourseIdAndType(courseId, type);
            return ResponseEntity.ok(ApiResponse.success(lessons));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get lessons by type: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Lesson>> updateLesson(@PathVariable UUID id, @RequestBody Lesson lesson) {
        try {
            Lesson updatedLesson = lessonService.updateLesson(id, lesson);
            return ResponseEntity.ok(ApiResponse.success(updatedLesson));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to update lesson: " + e.getMessage()));
        }
    }
    
    @PostMapping("/course/{courseId}/reorder")
    public ResponseEntity<ApiResponse<List<Lesson>>> reorderLessons(
            @PathVariable UUID courseId, 
            @RequestBody List<UUID> lessonIds) {
        try {
            List<Lesson> reorderedLessons = lessonService.reorderLessons(courseId, lessonIds);
            return ResponseEntity.ok(ApiResponse.success(reorderedLessons));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to reorder lessons: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteLesson(@PathVariable UUID id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.ok(ApiResponse.success("Lesson deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to delete lesson: " + e.getMessage()));
        }
    }
    
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<ApiResponse<Long>> getLessonCount(@PathVariable UUID courseId) {
        try {
            Long count = lessonService.getTotalLessonsByCourse(courseId);
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get lesson count: " + e.getMessage()));
        }
    }
}