package com.student.management_system.config;

import com.student.management_system.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for Stateless APIs
                .authorizeHttpRequests(auth -> auth

                        // --- 1. PUBLIC ENDPOINTS ---
                        .requestMatchers("/api/auth/**").permitAll() // Login/Register

                        // --- 2. ADMIN CONTROLLER ---
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // --- 3. TASK CONTROLLER RULES (Specific) ---
                        .requestMatchers("/api/tasks/create", "/api/tasks/assign/**", "/api/tasks/grade")
                        .hasRole("TEACHER")
                        .requestMatchers("/api/tasks/submit").hasRole("STUDENT")

                        // --- 4. SCHOOL CONTROLLER RULES (Mixed) ---
                        // Announcements: Teachers/Admins Post, Everyone Reads
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/school/announcements")
                        .hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/school/announcements")
                        .authenticated()

                        // Leaves: Student Applies, Teacher Approves
                        .requestMatchers("/api/school/leave/apply").hasRole("STUDENT")
                        .requestMatchers("/api/school/leave/pending", "/api/school/leave/status/**")
                        .hasAnyRole("TEACHER", "ADMIN")

                        // Notes: Teacher Writes/Reads
                        .requestMatchers("/api/school/notes/**").hasAnyRole("TEACHER", "ADMIN")

                        // --- 5. STUDENT DASHBOARD ---
                        .requestMatchers("/api/student/**").hasRole("STUDENT")

                        // --- 6. TEACHER DASHBOARD (General) ---
                        .requestMatchers("/api/teacher/**").hasRole("TEACHER")

                        // --- 7. CATCH ALL ---
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}