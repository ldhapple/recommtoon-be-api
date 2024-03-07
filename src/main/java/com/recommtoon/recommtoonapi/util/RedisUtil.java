package com.recommtoon.recommtoonapi.util;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(String username, String refreshToken, long duration) {
        redisTemplate.opsForValue().set(username, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(username);
    }

    public void saveAccessToken(String username, String accessToken, long duration) {
        redisTemplate.opsForValue().set("access_token:" + username, accessToken, duration, TimeUnit.SECONDS);
    }

    public String getAccessToken(String username) {
        return redisTemplate.opsForValue().get("access_token:" + username);
    }

    public void deleteAccessToken(String username) {
        redisTemplate.delete("access_token:" + username);
    }
}
