package com.student.management_system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    // 1. THE SECRET KEY (Must be long and secure!)
    // This is a random hex string I generated for you. Do not change it unless you
    // generate a new 256-bit key.
    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // 2. TOKEN VALIDITY (24 Hours)
    private static final long EXPIRATION_TIME = 86400000;

    // 3. GENERATE TOKEN (The "Ticket Printer")
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 4. EXTRACT EMAIL (Read the Ticket)
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 5. VALIDATE TOKEN (Check if Ticket is fake or expired)
    public boolean validateToken(String token, String userEmail) {
        final String emailInToken = extractEmail(token);
        return (emailInToken.equals(userEmail) && !isTokenExpired(token));
    }

    // --- HELPER METHODS (Internal Logic) ---
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}