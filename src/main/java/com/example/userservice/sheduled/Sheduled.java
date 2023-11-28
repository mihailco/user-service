package com.example.userservice.sheduled;

import com.example.userservice.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Sheduled {
    private final UserTokenService tokenService;

    @Scheduled(cron = "0 0 3 * * *")
    public void deleteExpiredTokens(){
        tokenService.deleteExpiredTokens();
    }
}
