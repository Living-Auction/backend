package com.project.livingauction.oauth.filter;


import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.project.livingauction.oauth.repository.TokenRepository;
import com.project.livingauction.oauth.service.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null && !tokenRepository.isBlacklisted(token)) {
            try {
                jwtTokenProvider.parseToken(token);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
}
