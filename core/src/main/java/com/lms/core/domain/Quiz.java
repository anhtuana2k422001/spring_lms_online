package com.lms.core.domain;

import com.lms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "quizzes")
@Getter
@Setter
public class Quiz extends BaseEntity {
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    
    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;
    
    @Column(name = "max_attempts")
    private Integer maxAttempts;
    
    @Column(name = "passing_score")
    private Double passingScore;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "available_from")
    private LocalDateTime availableFrom;
    
    @Column(name = "available_until")
    private LocalDateTime availableUntil;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<QuizQuestion> questions;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<QuizAttempt> attempts;
}