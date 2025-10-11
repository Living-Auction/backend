package com.project.livingauction.oauth.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepository {
	
    private final StringRedisTemplate redisTemplate; 

    public TokenRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String userId, String refreshToken, long duration) {
        redisTemplate.opsForValue().set("RefreshToken:" + userId, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("RefreshToken:" + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("RefreshToken:" + userId);
    }

    public void blacklistAccessToken(String token, long duration) {
        redisTemplate.opsForValue().set("BlackList:" + token, "blacklisted", duration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey("BlackList:" + token);
    }
}
