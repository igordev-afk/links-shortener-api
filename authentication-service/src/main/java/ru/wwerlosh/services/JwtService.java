package ru.wwerlosh.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String jwtToken);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String jwtToken, UserDetails userDetails);
}
