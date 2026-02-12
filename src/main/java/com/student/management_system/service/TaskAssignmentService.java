package com.student.management_system.service;

import com.student.management_system.entity.TaskAssignment;
import com.student.management_system.repository.TaskAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskAssignmentService {

    @Autowired
    private TaskAssignmentRepository assignmentRepository;

    // Logic: Student (or Team) marks work as done
    public TaskAssignment submitWork(Long assignmentId) {
        TaskAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setStatus(TaskAssignment.AssignmentStatus.COMPLETED);
        assignment.setSubmissionDate(LocalDateTime.now()); // Track exactly when they finished

        return assignmentRepository.save(assignment);
    }

    // Logic: Teacher grades the work
    public TaskAssignment gradeWork(Long assignmentId, Integer score, String feedback) {
        TaskAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setScore(score);
        assignment.setFeedback(feedback);

        return assignmentRepository.save(assignment);
    }

    // Logic: Get tasks for a student (Dashboard)
    public List<TaskAssignment> getStudentAssignments(Long studentId) {
        return assignmentRepository.findByStudent_UserId(studentId);
    }
}