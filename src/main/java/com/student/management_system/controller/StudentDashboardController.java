package com.student.management_system.controller;

import com.student.management_system.entity.TaskAssignment;
import com.student.management_system.entity.User;
import com.student.management_system.repository.TeamRepository;
import com.student.management_system.service.TaskAssignmentService;
import com.student.management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentDashboardController {

    @Autowired
    private TaskAssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/dashboard/{studentId}")
    public List<TaskAssignment> getMyDashboard(@PathVariable Long studentId) {
        return assignmentService.getStudentAssignments(studentId);
    }

    @GetMapping("/{studentId}/tasks")
    public List<TaskAssignment> getStudentTasks(@PathVariable Long studentId) {
        return assignmentService.getStudentAssignments(studentId);
    }

    @GetMapping("/{studentId}/team")
    public ResponseEntity<?> getStudentTeam(@PathVariable Long studentId) {
        return teamRepository.findByMembers_UserId(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }

    @PostMapping("/submit/{assignmentId}")
    public TaskAssignment submitTask(
            @PathVariable Long assignmentId,
            @RequestBody(required = false) Map<String, String> body) {
        String content = body != null ? body.get("content") : null;
        return assignmentService.submitWork(assignmentId, content);
    }

    @PutMapping("/profile/{studentId}")
    public User updateProfile(
            @PathVariable Long studentId,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "password", required = false) String password, // MODIFIED: Added password param
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // TRICK: Direct lookup is FAANG-standard. Never use .getAllUsers() to find one
        // ID.
        // We fetch the existing user so we don't lose the 'name' or 'email'.
        User existingUser = userService.getUserById(studentId);

        try {
            // 1. Handle File Upload
            if (file != null && !file.isEmpty()) {
                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = studentId + "_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                String fileUrl = "http://localhost:8080/uploads/" + fileName;
                existingUser.setProfileImageUrl(fileUrl);
            }

            // 2. Handle Phone Number
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                existingUser.setPhoneNumber(phoneNumber);
            }

            // 3. Handle Password (NEW)
            if (password != null && !password.isEmpty()) {
                // We set the plain password here; your UserService.updateUser
                // should handle the BCrypt hashing before saving.
                existingUser.setPassword(password);
            }

            // DO: Pass the whole existingUser object so the name/email/role stay intact
            return userService.updateUser(studentId, existingUser);

        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }
}