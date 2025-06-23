package com.lms.core.domain;

import com.lms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quiz_options")
@Getter
@Setter
public class QuizOption extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;
    
    @Column(name = "option_text", nullable = false)
    private String optionText;
    
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
}