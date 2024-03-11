package com.recommtoon.recommtoonapi.account.service;

import com.recommtoon.recommtoonapi.exception.InvalidRefreshTokenException;
import com.recommtoon.recommtoonapi.exception.UnAuthorizedException;
import com.recommtoon.recommtoonapi.util.JwtUtil;
import com.recommtoon.recommtoonapi.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    
    public void validateRefreshToken(String refreshToken, String storedRefreshToken) {
        checkRefreshTokenNull(refreshToken);
        validateStoredRefreshToken(refreshToken, storedRefreshToken);
    }

    public void validateAccessToken(String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new UnAuthorizedException("로그인이 되어있지 않습니다.");
        }
    }

    public String getStoredRefreshToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        return redisUtil.getRefreshToken(username);
    }

    public String getCachedAccessToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        return redisUtil.getAccessToken(username);
    }

    public boolean checkAccessTokenExpiration(String accessToken) {
        boolean isExpired = false;

        try {
            isExpired = jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            isExpired = true;
        }

        return isExpired;
    }

    public Map<String, String> refreshAccessToken(String refreshToken, String cachedAccessToken, boolean cachedTokenExpired) {
        Map<String, String> responseBody = new HashMap<>();

        String username = jwtUtil.getUsername(refreshToken);

        if (cachedAccessToken == null || cachedTokenExpired) {
            String newAccessToken = jwtUtil.createAccessToken(username, "USER", 60 * 60 * 10L);
            redisUtil.saveAccessToken(username, newAccessToken, 60 * 60 * 10L); // 새 Access 토큰 캐싱
            responseBody.put("accessToken", newAccessToken);
        } else {
            responseBody.put("accessToken", cachedAccessToken);
        }

        return responseBody;
    }

    public void deleteCachedTokens(String accessToken) {
        String token = accessToken.split(" ")[1];
        String username = jwtUtil.getUsername(token);

        redisUtil.deleteRefreshToken(username);
        redisUtil.deleteAccessToken(username);
    }

    private void validateStoredRefreshToken(String refreshToken, String storedRefreshToken) {
        if (!refreshToken.equals(storedRefreshToken) || jwtUtil.isExpired(storedRefreshToken)) {
            throw new InvalidRefreshTokenException("Refresh 토큰이 유효하지 않습니다.");
        }
    }

    private void checkRefreshTokenNull(String refreshToken) throws InvalidRefreshTokenException {
        if (refreshToken == null) {
            throw new InvalidRefreshTokenException("Refresh 토큰이 유효하지 않습니다.");
        }
    }
}
