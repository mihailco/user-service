package com.example.userservice.controller;

import com.example.userservice.dto.request.AuthorizeRequest;
import com.example.userservice.dto.request.RefreshTokenRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.request.SignOutRequest;
import com.example.userservice.dto.response.TokenResponse;
import com.example.userservice.exceptions.IncorrectCredentialsException;
import com.example.userservice.exceptions.UnauthorizedException;
import com.example.userservice.mapper.AuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    final private AuthMapper authMapper;

    @PostMapping("/register")
    public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
        return authMapper.register(request);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authorize(@Valid @RequestBody AuthorizeRequest request) {
        try {
            return ResponseEntity.ok(authMapper.authorize(request));
        } catch (IncorrectCredentialsException e) {
            return ResponseEntity.badRequest().body("invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            return ResponseEntity.ok(authMapper.refresh(request));
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signout")
    public void signout(@Valid @RequestBody SignOutRequest request) {
            authMapper.signout(request);

    }


}
