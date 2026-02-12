package com.student.management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority; // NORMAL, HIGH

    @Enumerated(EnumType.STRING)
    private TaskType taskType; // INDIVIDUAL, TEAM

    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Priority {
        NORMAL, HIGH
    }

    public enum TaskType {
        INDIVIDUAL, TEAM
    }
}