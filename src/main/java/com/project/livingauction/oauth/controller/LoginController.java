package com.project.livingauction.oauth.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.livingauction.oauth.dto.LogoutRequestDto;
import com.project.livingauction.oauth.dto.ReissueTokenDto;
import com.project.livingauction.oauth.dto.TokenResponseDto;
import com.project.livingauction.oauth.repository.TokenRepository;
import com.project.livingauction.oauth.service.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@CrossOrigin 
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

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