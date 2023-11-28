package com.example.userservice.mapper;

import com.example.userservice.dto.request.AuthorizeRequest;
import com.example.userservice.dto.request.RefreshTokenRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.request.SignOutRequest;
import com.example.userservice.dto.response.TokenResponse;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.entity.UserTokenEntity;
import com.example.userservice.exceptions.IncorrectCredentialsException;
import com.example.userservice.exceptions.UnauthorizedException;
import com.example.userservice.security.jwt.JwtUtils;
import com.example.userservice.security.jwt.TokenType;
import com.example.userservice.service.UserService;
import com.example.userservice.service.UserTokenService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
@RequiredArgsConstructor
public class AuthMapper {
    @PersistenceContext
    private EntityManager entityManager;
    final UserService userService;
    final UserTokenService tokenService;
    final JwtUtils jwtUtils;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;

    public TokenResponse register(RegisterRequest request) {
        UserEntity user = modelMapper.map(request, UserEntity.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userService.register(user);
        return createSaveTokenResponse(user);
    }

    public TokenResponse authorize(AuthorizeRequest request) throws IncorrectCredentialsException, UnauthorizedException {
        UserEntity user = userService.getByUsername(request.getUsername());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("invalid password");

        }
        return createSaveTokenResponse(user);
    }

    public TokenResponse refresh(RefreshTokenRequest request) throws UnauthorizedException {
        if (tokenService.deleteByRefreshToken(request.getRefreshToken()) != 0) {
            throw new UnauthorizedException("invalid token");
        }
        String username = jwtUtils.getUserNameFromJwtToken(request.getAccessToken(), TokenType.ACCESS);

        UserEntity user = entityManager.getReference(UserEntity.class, username);
        return createSaveTokenResponse(user);
    }

    public void signout(SignOutRequest request) {
        tokenService.deleteByAccessToken(request.getAccessToken());
    }

    private TokenResponse createSaveTokenResponse(UserEntity user) {
        String username = user.getUsername();
        String access = jwtUtils.generateJwt(username, TokenType.ACCESS);
        String refresh = jwtUtils.generateJwt(username, TokenType.REFRESH);
        int ttl = jwtUtils.getRefreshJwtExpirationMs();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, ttl);
        UserTokenEntity userToken = UserTokenEntity.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .expiredAt(calendar.getTime())
                .user(user)
                .build();

        tokenService.create(userToken);

        return TokenResponse.builder().accessToken(access).refreshToken(refresh).ttl(jwtUtils.getAccessJwtExpirationMs()).build();
    }
}
