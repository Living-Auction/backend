package com.project.livingauction.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor 
public class ReissueTokenDto {
    private String userId;
    private String refreshToken;
}
