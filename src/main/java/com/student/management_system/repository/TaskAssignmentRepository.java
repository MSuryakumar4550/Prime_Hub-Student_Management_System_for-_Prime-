package com.student.management_system.repository;

import com.student.management_system.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {

    // 1. For Student Dashboard: Find all individual assignments for a student
    List<TaskAssignment> findByStudent_UserId(Long studentId);

    // 2. For Student Dashboard: Find all assignments for a specific Team
    List<TaskAssignment> findByTeam_TeamId(Long teamId);

    // 3. For Teacher Dashboard: See who has finished a specific task
    List<TaskAssignment> findByTask_TaskId(Long taskId);

    // 4. For Analytics: Count how many tasks a student has completed
    long countByStudent_UserIdAndStatus(Long studentId, TaskAssignment.AssignmentStatus status);

    // Find the specific assignment for a specific student and task
    Optional<TaskAssignment> findByStudent_UserIdAndTask_TaskId(Long studentId, Long taskId);
}