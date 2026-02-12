package com.student.management_system.security;

import com.student.management_system.entity.User;
import com.student.management_system.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get the "Authorization" Header
        final String authHeader = request.getHeader("Authorization");
        final String userEmail;
        final String jwtToken;

        // 2. Check if Header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Pass the request along (it will likely fail later if auth is
                                                     // needed)
            return;
        }

        // 3. Extract the Token
        jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
        userEmail = jwtUtils.extractEmail(jwtToken); // Read user from token

        // 4. If User is found & not already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Find User in DB
            User user = userRepository.findByEmail(userEmail).orElse(null);

            // 6. Validate Token
            if (user != null && jwtUtils.validateToken(jwtToken, user.getEmail())) {

                // 7. Create Authentication Token (The "Entry Pass")
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 8. Set the User in the Security Context (Log them in!)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9. Continue the filter chain
        filterChain.doFilter(request, response);
    }
}