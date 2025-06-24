package com.lms.infra.service;

import com.lms.common.exception.ResourceNotFoundException;
import com.lms.core.domain.Lesson;
import com.lms.core.domain.LessonType;
import com.lms.core.repository.LessonRepository;
import com.lms.core.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {
    
    private final LessonRepository lessonRepository;
    
    @Override
    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Lesson> findById(UUID id) {
        return lessonRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Lesson> findByCourseId(UUID courseId) {
        return lessonRepository.findByCourse_IdOrderByOrderIndex(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> findByCourseIdAndType(UUID courseId, LessonType type) {
        return lessonRepository.findByCourse_IdAndType(courseId, type);
    }
    
    @Override
    public Lesson updateLesson(UUID id, Lesson lesson) {
        Lesson existingLesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        
        existingLesson.setTitle(lesson.getTitle());
        existingLesson.setContent(lesson.getContent());
        existingLesson.setVideoUrl(lesson.getVideoUrl());
        existingLesson.setDurationMinutes(lesson.getDurationMinutes());
        existingLesson.setType(lesson.getType());
        existingLesson.setOrderIndex(lesson.getOrderIndex());
        
        return lessonRepository.save(existingLesson);
    }
    
    @Override
    public void deleteLesson(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        lesson.setIsDeleted(true);
        lessonRepository.save(lesson);
    }
    
    @Override
    public List<Lesson> reorderLessons(UUID courseId, List<UUID> lessonIds) {
        AtomicInteger orderIndex = new AtomicInteger(1);
        
        return lessonIds.stream()
                .map(lessonId -> {
                    Lesson lesson = lessonRepository.findById(lessonId)
                            .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
                    lesson.setOrderIndex(orderIndex.getAndIncrement());
                    return lessonRepository.save(lesson);
                })
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getTotalLessonsByCourse(UUID courseId) {
        return lessonRepository.countByCourseId(courseId);
    }
}