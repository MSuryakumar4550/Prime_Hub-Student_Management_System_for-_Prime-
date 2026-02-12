package com.student.management_system.service;

import com.student.management_system.entity.Announcement;
import com.student.management_system.entity.User;
import com.student.management_system.repository.AnnouncementRepository;
import com.student.management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.student.management_system.dto.AnnouncementRequest;
import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private UserRepository userRepository;

    public Announcement postAnnouncement(AnnouncementRequest request) {
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setPostedBy(teacher);

        return announcementRepository.save(announcement);
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByPostedAtDesc();
    }
}