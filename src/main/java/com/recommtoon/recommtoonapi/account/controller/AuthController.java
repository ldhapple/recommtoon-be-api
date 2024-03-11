package com.recommtoon.recommtoonapi.account.controller;

import com.recommtoon.recommtoonapi.account.service.AuthService;
import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import com.recommtoon.recommtoonapi.util.CookieUtil;
import com.recommtoon.recommtoonapi.util.JwtUtil;
import com.recommtoon.recommtoonapi.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
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

    private final CookieUtil cookieUtil;
    private final AuthService authService;

    @PostMapping("/refresh")
    public ApiSuccess<?> refreshAccessToken(HttpServletRequest request) {

        String refreshToken = cookieUtil.getCookieValue("refreshToken", request);
        String storedRefreshToken = authService.getStoredRefreshToken(refreshToken);
        authService.validateRefreshToken(refreshToken, storedRefreshToken);

        String cachedAccessToken = authService.getCachedAccessToken(refreshToken);
        boolean cachedTokenExpired = authService.checkAccessTokenExpiration(cachedAccessToken);

        Map<String, String> responseBody = authService.refreshAccessToken(refreshToken, cachedAccessToken,
                cachedTokenExpired);

        return ApiUtil.success(responseBody);
    }

    @PostMapping("/logout")
    public ApiSuccess<?> logout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");

        authService.validateAccessToken(accessToken);
        authService.deleteCachedTokens(accessToken);

        return ApiUtil.success("로그아웃이 완료되었습니다.");
    }
}
