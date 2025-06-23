package com.lms.core.domain;

import com.lms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "quiz_questions")
@Getter
@Setter
public class QuizQuestion extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;
    
    @Column(name = "points", nullable = false)
    private Double points;
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private Set<QuizOption> options;
}