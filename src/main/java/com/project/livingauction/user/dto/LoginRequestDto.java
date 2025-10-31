package com.project.livingauction.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter  @Setter
public class LoginRequestDto {
    private String email;
    private String picture;
    private String provider;
    private String providerId;
}
