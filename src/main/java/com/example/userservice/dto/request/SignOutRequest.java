package com.example.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignOutRequest {
    @NotBlank
    String accessToken;
}
