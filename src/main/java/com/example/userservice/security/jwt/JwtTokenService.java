package com.example.userservice.security.jwt;

public interface JwtTokenService {
    String generateJwtToken(String username, int expirationMs, String secret);
    String getUserNameFromJwtToken(String token, String secret);
    boolean validateJwtToken(String authToken, String secret);
}
