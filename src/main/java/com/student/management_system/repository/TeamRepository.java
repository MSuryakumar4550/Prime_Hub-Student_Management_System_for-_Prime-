package com.student.management_system.repository;

import com.student.management_system.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    // Find all teams created by a specific teacher
    List<Team> findByCreatedBy_UserId(Long teacherId);
}