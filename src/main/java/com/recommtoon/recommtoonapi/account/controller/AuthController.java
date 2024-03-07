package com.recommtoon.recommtoonapi.account.controller;

import com.recommtoon.recommtoonapi.account.entity.Role;
import com.recommtoon.recommtoonapi.util.JwtUtil;
import com.recommtoon.recommtoonapi.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        String authHeader = request.getHeader("Authorization");
        String accessToken = authHeader.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);

        String storedRefreshToken = redisUtil.getRefreshToken(username);

        if (jwtUtil.isExpired(storedRefreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("만료된 Refresh 토큰입니다.");
        }

        String newAccessToken = jwtUtil.createAccessToken(username, "USER", 60 * 60 * 10L);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + newAccessToken);
        return ResponseEntity.ok().headers(responseHeaders).body("Access 토큰이 갱신되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            String username = jwtUtil.getUsername(token);

            redisUtil.deleteRefreshToken(username);
            return ResponseEntity.ok().body("로그아웃이 완료되었습니다.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 되어있지 않습니다.");
    }
}
