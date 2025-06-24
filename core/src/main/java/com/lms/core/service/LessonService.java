package com.lms.core.service;

import com.lms.core.domain.Lesson;
import com.lms.core.domain.LessonType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {
    Lesson createLesson(Lesson lesson);
    Optional<Lesson> findById(UUID id);
    List<Lesson> findByCourseId(UUID courseId);
    List<Lesson> findByCourseIdAndType(UUID courseId, LessonType type);
    Lesson updateLesson(UUID id, Lesson lesson);
    void deleteLesson(UUID id);
    List<Lesson> reorderLessons(UUID courseId, List<UUID> lessonIds);
    Long getTotalLessonsByCourse(UUID courseId);
}