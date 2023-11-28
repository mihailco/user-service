package com.example.userservice.service;

import com.example.userservice.entity.UserTokenEntity;
import com.example.userservice.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    final private UserTokenRepository repository;

    public UserTokenEntity create(UserTokenEntity entity) {
        return repository.save(entity);
    }

    public void deleteExpiredTokens() {
        repository.deleteAllByExpiredAtBefore(new Date());
    }

    public int deleteByRefreshToken(String refreshToken){
        return repository.deleteByRefreshToken(refreshToken);
    }

    public void deleteByAccessToken(String accessToken){
        repository.deleteByAccessToken(accessToken);
    }
}
