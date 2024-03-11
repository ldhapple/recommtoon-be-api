package com.recommtoon.recommtoonapi.account.controller;

import com.recommtoon.recommtoonapi.account.dto.RegisterDto;
import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.service.AccountService;
import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "계정관리 컨트롤러")
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "중복 체크", description = "아이디 및 닉네임 중복 체크")
    @ApiResponse(responseCode = "200", description = "중복이 발생한 경우 'true'를 반환한다.", content = @Content(schema = @Schema(implementation = boolean.class)))
    @ApiResponse(responseCode = "200", description = "중복이 발생하지 않은 경우 'false'를 반환한다.", content = @Content(schema = @Schema(implementation = boolean.class)))
    @GetMapping("/checkDuplicate/{field}/{value}")
    public ApiSuccess<Boolean> checkDuplicate(@PathVariable String field, @PathVariable String value) {
        boolean isDuplicate = false;

        if (field.equals("username")) {
            isDuplicate = accountService.isUsernameDuplicate(value);
        } else if (field.equals("nickname")) {
            isDuplicate = accountService.isNickNameDuplicate(value);
        }

        return ApiUtil.success(isDuplicate);
    }

    @Operation(summary = "회원 가입", description = "회원 데이터 저장")
    @ApiResponse(responseCode = "200", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = Account.class)))
    @PostMapping("/register")
    public ApiSuccess<?> registerAccount(@Valid @RequestBody RegisterDto registerDto) {
        Account savedAccount = accountService.register(registerDto);

        return ApiUtil.success(savedAccount);
    }
}
