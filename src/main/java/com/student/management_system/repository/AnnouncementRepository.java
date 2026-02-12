package com.student.management_system.repository;

import com.student.management_system.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // Show newest announcements first
    List<Announcement> findAllByOrderByPostedAtDesc();
}