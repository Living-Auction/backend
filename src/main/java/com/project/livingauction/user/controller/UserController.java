package com.project.livingauction.user.controller;

import com.project.livingauction.user.service.JwtTokenProvider;
import com.project.livingauction.user.dto.LoginResponseDto;
import com.project.livingauction.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.livingauction.user.dto.LoginRequestDto;
import com.project.livingauction.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/oauth")
    public ResponseEntity<LoginResponseDto> oauthLogin(@RequestBody LoginRequestDto request) {
        User user = userService.handleOAuthLogin(request);
        String token = jwtTokenProvider.createAccessToken(user.getId().toString());
        return ResponseEntity.ok(new LoginResponseDto(user, token));
    }
}