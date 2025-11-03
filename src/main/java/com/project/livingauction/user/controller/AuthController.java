package com.project.livingauction.user.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.livingauction.user.dto.LoginRequestDto;
import com.project.livingauction.user.dto.LoginResponseDto;
import com.project.livingauction.user.dto.LogoutRequestDto;
import com.project.livingauction.user.dto.ReissueTokenDto;
import com.project.livingauction.user.dto.TokenResponseDto;
import com.project.livingauction.user.entity.User;
import com.project.livingauction.user.repository.TokenRepository;
import com.project.livingauction.user.service.JwtTokenProvider;
import com.project.livingauction.user.service.UserService;

import lombok.RequiredArgsConstructor;

@CrossOrigin 
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;


//    @PostMapping("/login")
//    public TokenResponseDto login(@RequestBody LoginRequestDto request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid password");
//        }
//
//        String accessToken = jwtTokenProvider.createAccessToken(user.getId().toString());
//        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId().toString());
//
//        tokenRepository.saveRefreshToken(user.getId().toString(), refreshToken,
//                jwtTokenProvider.getRefreshTokenValidity());
//
//        return new TokenResponseDto(accessToken, refreshToken);
//    }
    
    @PostMapping("/oauthLogin")
    public ResponseEntity<LoginResponseDto> oauthLogin(@RequestBody LoginRequestDto request) {
        User user = userService.handleOAuthLogin(request);
        String accessToken = jwtTokenProvider.createAccessToken(user.getId().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId().toString());
        
        tokenRepository.saveRefreshToken(user.getId().toString(), refreshToken,
    		  jwtTokenProvider.getRefreshTokenValidity());
        
        return ResponseEntity.ok(new LoginResponseDto(user, accessToken, refreshToken));
    }
    
    @PostMapping("/logout")
    public String logout(@RequestBody LogoutRequestDto logOutRequestDto) {
        tokenRepository.deleteRefreshToken(logOutRequestDto.getUserId());

        long expiration = jwtTokenProvider.parseToken(logOutRequestDto.getUserId()).getExpiration().getTime()
                - System.currentTimeMillis();
        tokenRepository.blacklistAccessToken(logOutRequestDto.getAccessToken(), expiration);

        return "Logged out successfully";
    }
    
    @PostMapping("/reissueToken")
    public TokenResponseDto reissueToken(@RequestBody ReissueTokenDto reissueTokenDto) {
        String savedRefreshToken = tokenRepository.getRefreshToken(reissueTokenDto.getUserId());

        if (savedRefreshToken != null && savedRefreshToken.equals(reissueTokenDto.getRefreshToken())) {
            String newAccessToken = jwtTokenProvider.createAccessToken(reissueTokenDto.getUserId());
            return new TokenResponseDto(newAccessToken, reissueTokenDto.getRefreshToken());
        }
        throw new RuntimeException("Invalid refresh token");
    }
}