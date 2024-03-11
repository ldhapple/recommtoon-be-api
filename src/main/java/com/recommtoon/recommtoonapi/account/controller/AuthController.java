package com.recommtoon.recommtoonapi.account.controller;

import com.recommtoon.recommtoonapi.account.service.AuthService;
import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiError;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import com.recommtoon.recommtoonapi.util.CookieUtil;
import com.recommtoon.recommtoonapi.util.JwtUtil;
import com.recommtoon.recommtoonapi.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "인증 컨트롤러")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CookieUtil cookieUtil;
    private final AuthService authService;

    @Operation(summary = "토큰 갱신", description = "Access 토큰 갱신")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "기존 토큰 유효성 검증 실패", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
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

    @Operation(summary = "로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그 아웃 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "토큰 유효성 검증 실패", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/logout")
    public ApiSuccess<?> logout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");

        authService.validateAccessToken(accessToken);
        authService.deleteCachedTokens(accessToken);

        return ApiUtil.success("로그아웃이 완료되었습니다.");
    }
}
