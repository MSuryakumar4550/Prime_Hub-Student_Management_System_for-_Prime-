package com.student.management_system.controller;

import com.student.management_system.entity.Announcement;
import com.student.management_system.entity.LeaveApplication;
import com.student.management_system.entity.PersonalNote;
import com.student.management_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.student.management_system.dto.*;
import java.util.List;

@RestController
@RequestMapping("/api/school")
@CrossOrigin(origins = "*")
public class SchoolController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private NoteService noteService;

    // --- ANNOUNCEMENTS ---
    @PostMapping("/announcements")
    public Announcement postAnnouncement(@RequestBody AnnouncementRequest request) {
        return announcementService.postAnnouncement(request);
    }

    @GetMapping("/announcements")
    public List<Announcement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    // --- LEAVES ---
    @PostMapping("/leave/apply")
    public LeaveApplication applyForLeave(@RequestBody LeaveRequest request) {
        return leaveService.applyForLeave(request);
    }

    @GetMapping("/leave/pending")
    public List<LeaveApplication> getPendingLeaves() {
        return leaveService.getPendingLeaves();
    }

    @PutMapping("/leave/status/{leaveId}")
    public LeaveApplication updateLeaveStatus(@PathVariable Long leaveId,
            @RequestParam LeaveApplication.LeaveStatus status) {
        return leaveService.updateLeaveStatus(leaveId, status);
    }

    // 1. Create a Note
    // URL: POST http://localhost:8080/api/school/notes?teacherId=1&studentId=2
    @PostMapping("/notes")
    public PersonalNote addNote(@RequestBody NoteRequest request) {
        return noteService.addNote(request);
    }

    // 2. View Notes for a specific student
    // URL: GET http://localhost:8080/api/school/notes?teacherId=1&studentId=2
    @GetMapping("/notes")
    public List<PersonalNote> getNotes(
            @RequestParam Long teacherId,
            @RequestParam Long studentId) {
        return noteService.getNotesForStudent(teacherId, studentId);
    }

}