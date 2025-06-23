package com.lms.core.domain;

import com.lms.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
public class Lesson extends BaseEntity {
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "video_url")
    private String videoUrl;
    
    @Column(name = "order_index")
    private Integer orderIndex;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Enumerated(EnumType.STRING)
    private LessonType type = LessonType.TEXT;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}