package com.project.livingauction.oauth.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.project.livingauction.oauth.model.CustomOAuth2User;
import com.project.livingauction.oauth.repository.TokenRepository;
import com.project.livingauction.user.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    
    @Value ("${frontend.redirect-url}")
    private String frontRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
            throws IOException {

        CustomOAuth2User principal = (CustomOAuth2User) auth.getPrincipal();
        User user = principal.getUser();

        String accessToken = jwtTokenProvider.createAccessToken(user.getId().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId().toString());

        tokenRepository.saveRefreshToken(user.getId().toString(), refreshToken, jwtTokenProvider.getRefreshTokenValidity());

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);

        
        String redirectUrl = String.format("%s?accessToken=%s&refreshToken=%s",
                frontRedirectUrl, accessToken, refreshToken);
        res.sendRedirect(redirectUrl);
    }
}
