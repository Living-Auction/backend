package com.project.livingauction.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor 
public class LogoutRequestDto {
    private String userId;
    private String accessToken;
}
