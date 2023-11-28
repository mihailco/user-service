package com.example.userservice.security.jwt.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.userservice.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Component
public class JwtTokenServiceImpl implements JwtTokenService {
    Logger logger = Logger.getLogger(JwtTokenServiceImpl.class.getName());

    public String generateJwtToken(String username, int expirationMs, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Date expirationDate = new Date(System.currentTimeMillis() + expirationMs);
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(expirationDate)
                .sign(algorithm);
    }

    public String getUserNameFromJwtToken(String token, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    public boolean validateJwtToken(String authToken, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(authToken);
            return true;
        } catch (JWTVerificationException e) {
            logger.log(Level.SEVERE, "Invalid JWT token: " + e.getMessage());
            return false;
        }
    }
}
