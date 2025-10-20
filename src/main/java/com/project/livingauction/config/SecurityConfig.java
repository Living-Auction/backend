package com.project.livingauction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.livingauction.oauth.filter.JwtAuthenticationFilter;
import com.project.livingauction.oauth.repository.TokenRepository;
import com.project.livingauction.oauth.service.CustomOAuth2UserService;
import com.project.livingauction.oauth.service.JwtTokenProvider;
import com.project.livingauction.oauth.service.OAuth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration @RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> {})
            .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/items/**", "/login/**", "/oauth2/**","/auth/**",
                                 "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
            	.redirectionEndpoint(re -> re.baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
                .successHandler((AuthenticationSuccessHandler)successHandler)
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, tokenRepository),
                             UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
