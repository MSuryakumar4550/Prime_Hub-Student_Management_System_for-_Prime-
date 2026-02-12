package com.student.management_system.controller;

import com.student.management_system.entity.TaskAssignment;
import com.student.management_system.service.TaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentDashboardController {

    @Autowired
    private TaskAssignmentService assignmentService;

    // 1. View My Dashboard (Get all assignments)
    // URL: GET http://localhost:8080/api/student/dashboard/1
    @GetMapping("/dashboard/{studentId}")
    public List<TaskAssignment> getMyDashboard(@PathVariable Long studentId) {
        return assignmentService.getStudentAssignments(studentId);
    }

    // 2. Submit a Task (Mark as Completed)
    // URL: POST http://localhost:8080/api/student/submit/55
    @PostMapping("/submit/{assignmentId}")
    public TaskAssignment submitTask(@PathVariable Long assignmentId) {
        return assignmentService.submitWork(assignmentId);
    }
}