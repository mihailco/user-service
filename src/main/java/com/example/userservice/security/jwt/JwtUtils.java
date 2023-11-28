package com.example.userservice.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class JwtUtils {

    @Value("${jwt.access.secret}")
    private String accessJwtSecret;

    @Value("${jwt.access.ttl}")
    private int accessJwtExpirationMs;

    @Value("${jwt.refresh.secret}")
    private String refreshJwtSecret;

    @Value("${jwt.refresh.ttl}")
    private int refreshJwtExpirationMs;

    final JwtTokenService jwtTokenService;

    public String generateJwt(String username, TokenType tokenType) {
        String secret = "";
        int ttl = 0;

        if (tokenType == TokenType.ACCESS) {
            secret = accessJwtSecret;
            ttl = accessJwtExpirationMs;
        } else if (tokenType == TokenType.REFRESH) {
            secret = refreshJwtSecret;
            ttl = refreshJwtExpirationMs;
        }

        return jwtTokenService.generateJwtToken(username, ttl, secret);
    }


    public boolean validateJwtToken(String authToken, TokenType tokenType) {
        String secret = "";

        if (tokenType == TokenType.ACCESS) {
            secret = accessJwtSecret;
        } else if (tokenType == TokenType.REFRESH) {
            secret = refreshJwtSecret;
        }

        return jwtTokenService.validateJwtToken(authToken, secret);
    }

    public String getUserNameFromJwtToken(String token, TokenType tokenType) {
        String secret = "";

        if (tokenType == TokenType.ACCESS) {
            secret = accessJwtSecret;
        } else if (tokenType == TokenType.REFRESH) {
            secret = refreshJwtSecret;
        }

        return jwtTokenService.getUserNameFromJwtToken(token, secret);
    }
}