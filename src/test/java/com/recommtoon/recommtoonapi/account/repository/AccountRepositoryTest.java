package com.recommtoon.recommtoonapi.account.repository;

import com.recommtoon.recommtoonapi.account.entity.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        Account saveUsername = Account.builder()
                .username("abcd1234")
                .build();

        Account saveNickName = Account.builder()
                .nickName("닉네임")
                .build();

        accountRepository.save(saveUsername);
        accountRepository.save(saveNickName);
    }

    @Test
    void findByUsername() {
        Account findAccount = accountRepository.findByUsername("abcd1234");
        Account noAccount = accountRepository.findByUsername("1234");

        assertThat(findAccount.getUsername()).isEqualTo("abcd1234");
        assertThat(noAccount).isNull();
    }

    @Test
    void findByNickName() {
        Account findAccount = accountRepository.findByNickName("닉네임");
        Account noAccount = accountRepository.findByUsername("1234");

        assertThat(findAccount.getNickName()).isEqualTo("닉네임");
        assertThat(noAccount).isNull();
    }
}