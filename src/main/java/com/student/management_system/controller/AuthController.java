package com.student.management_system.controller;

import com.student.management_system.entity.User;
import com.student.management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.student.management_system.security.JwtUtils;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    // URL: POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public java.util.Map<String, Object> login(@RequestBody LoginRequest request) {
        // 1. Verify User exists & Password is correct
        User user = userService.login(request.getEmail(), request.getPassword());

        if (user == null) {
            throw new RuntimeException("Invalid credentials");
        }

        // 2. GENERATE TOKEN (This was missing!)
        String token = jwtUtils.generateToken(user.getEmail());

        // 3. Send Token + User Info back to the client
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("token", token); // The Key Card
        response.put("type", "Bearer");
        response.put("role", user.getRole()); // "ADMIN", "TEACHER", etc.
        response.put("name", user.getName());
        response.put("email", user.getEmail());

        return response;
    }

    // Simple DTO class for login
    public static class LoginRequest {
        private String email;
        private String password;

        // Getters and Setters needed for JSON
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}