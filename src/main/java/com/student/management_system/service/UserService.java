package com.student.management_system.service;

import com.student.management_system.entity.User;
import com.student.management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Needed for password hashing
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // To hash new passwords

    // 1. Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. Add User
    public User addUser(User user) {
        // Always encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // 3. Update User (The Logic you wanted)
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());

        // Only update/hash password if a new one is provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    // 4. Delete User
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found!");
        }
        userRepository.deleteById(id);
    }

    // 5. LOGIN LOGIC (Matches encrypted passwords)
    public User login(String email, String password) {
        // 1. Find user by email
        User user = userRepository.findByEmail(email).orElse(null);

        // 2. Check if user exists AND if password matches the hash
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        // 3. Login failed
        return null;
    }

}