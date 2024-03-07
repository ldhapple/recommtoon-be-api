package com.recommtoon.recommtoonapi.account.controller;

import com.recommtoon.recommtoonapi.account.entity.Role;
import com.recommtoon.recommtoonapi.util.JwtUtil;
import com.recommtoon.recommtoonapi.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh 토큰이 유효하지 않습니다.");
        }

        String username = jwtUtil.getUsername(refreshToken);
        String storedRefreshToken = redisUtil.getRefreshToken(username);

        if (!refreshToken.equals(storedRefreshToken) || jwtUtil.isExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh 토큰이 유효하지 않습니다.");
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        String cachedAccessToken = redisUtil.getAccessToken(username);

        if (cachedAccessToken == null || jwtUtil.isExpired(cachedAccessToken)) {
            String newAccessToken = jwtUtil.createAccessToken(username, "USER", 60 * 60 * 10L);
            redisUtil.saveAccessToken(username, newAccessToken, 60 * 60 * 10L); // 새 Access 토큰 캐싱
            responseHeaders.set("Authorization", "Bearer " + newAccessToken);
        } else {
            responseHeaders.set("Authorization", "Bearer " + cachedAccessToken);
        }

        return ResponseEntity.ok().headers(responseHeaders).body("Access 토큰이 갱신되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            String username = jwtUtil.getUsername(token);

            redisUtil.deleteRefreshToken(username);
            redisUtil.deleteAccessToken(username);
            return ResponseEntity.ok().body("로그아웃이 완료되었습니다.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 되어있지 않습니다.");
    }
}
