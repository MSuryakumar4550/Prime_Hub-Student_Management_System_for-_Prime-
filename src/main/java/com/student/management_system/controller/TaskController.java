package com.student.management_system.controller;

import com.student.management_system.entity.Task;
import com.student.management_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.student.management_system.dto.*;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*") // Allow React to access this
public class TaskController {

    @Autowired
    private TaskService taskService;

    // 1. Create a new Task
    // URL: POST http://localhost:8080/api/tasks/create?teacherId=1
    @PostMapping("/create")
    public Task createTask(@RequestBody TaskRequest request) {
        return taskService.createTask(request);
    }

    // 2. Assign Task to Individual Students
    // URL: POST http://localhost:8080/api/tasks/assign/students?taskId=5
    @PostMapping("/assign/students")
    public String assignToStudents(@RequestBody AssignmentRequest request) {
        taskService.assignToStudents(request.getTaskId(), request.getStudentIds());
        return "Task assigned to students successfully.";
    }

    // 3. Assign Task to a Team
    // URL: POST http://localhost:8080/api/tasks/assign/team?taskId=5&teamId=10
    @PostMapping("/assign/team")
    public String assignToTeam(@RequestBody AssignmentRequest request) {
        taskService.assignToTeam(request.getTaskId(), request.getTeamId());
        return "Task assigned to team successfully.";
    }

    // 4. SUBMIT TASK (You were missing this!)
    // URL: POST /api/tasks/submit?studentId=2&taskId=2
    @PostMapping("/submit")
    public String submitTask(@RequestParam Long studentId, @RequestParam Long taskId) {
        taskService.submitTask(studentId, taskId);
        return "Task submitted successfully.";
    }

    // 5. GRADE TASK (You were missing this too!)
    // URL: POST /api/tasks/grade
    @PostMapping("/grade")
    public String gradeTask(
            @RequestParam Long teacherId,
            @RequestParam Long taskId,
            @RequestParam Long studentId,
            @RequestParam int score,
            @RequestParam String feedback) {

        taskService.gradeTask(teacherId, taskId, studentId, score, feedback);
        return "Task graded successfully.";
    }
}