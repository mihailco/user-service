package com.example.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthorizeRequest {
    @NotBlank
    String username;
    @NotBlank
    String password;
}
