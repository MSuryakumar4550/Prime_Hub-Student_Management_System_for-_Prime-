package com.student.management_system.service;

import com.student.management_system.entity.Task;
import com.student.management_system.entity.TaskAssignment;
import com.student.management_system.entity.Team;
import com.student.management_system.entity.User;
import com.student.management_system.repository.TaskAssignmentRepository;
import com.student.management_system.repository.TaskRepository;
import com.student.management_system.repository.TeamRepository;
import com.student.management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.student.management_system.dto.TaskRequest;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    // --- LOGIC 1: Create a Task Definition ---
    public Task createTask(TaskRequest request) {
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setTaskType(request.getTaskType());
        task.setDueDate(request.getDueDate());
        task.setCreatedBy(teacher);

        return taskRepository.save(task);
    }

    // --- LOGIC 2: Assign to INDIVIDUAL Students ---
    public void assignToStudents(Long taskId, List<Long> studentIds) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        List<User> students = userRepository.findAllById(studentIds);

        for (User student : students) {
            TaskAssignment assignment = new TaskAssignment();
            assignment.setTask(task);
            assignment.setStudent(student); // Link to Student
            assignment.setTeam(null); // No Team
            assignment.setStatus(TaskAssignment.AssignmentStatus.PENDING);

            assignmentRepository.save(assignment);
        }
    }

    // --- LOGIC 3: Assign to a TEAM ---
    public void assignToTeam(Long taskId, Long teamId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        // Create ONE assignment for the whole team
        TaskAssignment assignment = new TaskAssignment();
        assignment.setTask(task);
        assignment.setTeam(team); // Link to Team
        assignment.setStudent(null); // No single student
        assignment.setStatus(TaskAssignment.AssignmentStatus.PENDING);

        assignmentRepository.save(assignment);
    }

    // 4. LOGIC: Submit a Task
    public void submitTask(Long studentId, Long taskId) {
        TaskAssignment assignment = assignmentRepository.findByStudent_UserIdAndTask_TaskId(studentId, taskId)
                .orElseThrow(() -> new RuntimeException("Assignment not found for this student and task"));

        assignment.setSubmissionDate(java.time.LocalDateTime.now());
        assignment.setStatus(TaskAssignment.AssignmentStatus.COMPLETED); // Or SUBMITTED depending on your Enum

        assignmentRepository.save(assignment);
    }

    // 5. LOGIC: Grade a Task
    public void gradeTask(Long teacherId, Long taskId, Long studentId, int score, String feedback) {
        TaskAssignment assignment = assignmentRepository.findByStudent_UserIdAndTask_TaskId(studentId, taskId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Verify the teacher exists (Optional but good practice)
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        assignment.setScore(score);
        assignment.setFeedback(feedback);
        assignment.setEvaluatedBy(teacher);

        assignmentRepository.save(assignment);
    }
}