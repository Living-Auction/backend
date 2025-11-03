package com.project.livingauction.user.dto;

import com.project.livingauction.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private User user;
    private String accessToken;
    private String refreshToken;
}