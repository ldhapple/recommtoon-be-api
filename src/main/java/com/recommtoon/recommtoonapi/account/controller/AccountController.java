package com.recommtoon.recommtoonapi.account.controller;

import com.recommtoon.recommtoonapi.account.dto.RegisterDto;
import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.service.AccountService;
import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

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

    @PostMapping("/register")
    public ApiSuccess<?> registerAccount(@Valid @RequestBody RegisterDto registerDto) {
        Account savedAccount = accountService.register(registerDto);

        return ApiUtil.success(savedAccount);
    }
}
