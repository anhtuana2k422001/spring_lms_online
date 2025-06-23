package com.lms.core.domain;

import com.lms.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String name;
    
    private String description;
}