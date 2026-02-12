package com.student.management_system.service;

import com.student.management_system.entity.LeaveApplication;
import com.student.management_system.entity.User;
import com.student.management_system.repository.LeaveApplicationRepository;
import com.student.management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.student.management_system.dto.LeaveRequest;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveApplicationRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    public LeaveApplication applyForLeave(LeaveRequest request) {
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        LeaveApplication leave = new LeaveApplication();
        leave.setStudent(student);
        leave.setReason(request.getReason());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setStatus(LeaveApplication.LeaveStatus.PENDING);

        return leaveRepository.save(leave);
    }

    public List<LeaveApplication> getPendingLeaves() {
        return leaveRepository.findByStatus(LeaveApplication.LeaveStatus.PENDING);
    }

    public LeaveApplication updateLeaveStatus(Long leaveId, LeaveApplication.LeaveStatus newStatus) {
        LeaveApplication leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave application not found"));

        leave.setStatus(newStatus);
        return leaveRepository.save(leave);
    }
}