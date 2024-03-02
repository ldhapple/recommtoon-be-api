package com.recommtoon.recommtoonapi.account.controller;

import com.recommtoon.recommtoonapi.account.dto.RegisterDto;
import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.service.AccountService;
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
    public ResponseEntity<Boolean> checkDuplicate(@PathVariable String field, @PathVariable String value) {
        boolean isDuplicate = false;

        if (field.equals("username")) {
            isDuplicate = accountService.isUsernameDuplicate(value);
        } else if (field.equals("nickname")) {
            isDuplicate = accountService.isNickNameDuplicate(value);
        }

        return ResponseEntity.ok(isDuplicate);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody RegisterDto registerDto) {
        Account savedAccount = accountService.register(registerDto);
        return ResponseEntity.ok(savedAccount);
    }
}
