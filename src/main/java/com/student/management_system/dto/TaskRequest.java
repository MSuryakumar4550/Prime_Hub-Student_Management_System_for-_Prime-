package com.student.management_system.dto;

import com.student.management_system.entity.Task;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskRequest {
    private Long teacherId;
    private String title;
    private String description;
    private Task.Priority priority;
    private Task.TaskType taskType;
    private LocalDateTime dueDate;
}