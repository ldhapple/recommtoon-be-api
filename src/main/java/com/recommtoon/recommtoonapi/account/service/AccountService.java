package com.recommtoon.recommtoonapi.account.service;

import com.recommtoon.recommtoonapi.account.dto.RegisterDto;
import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.entity.Role;
import com.recommtoon.recommtoonapi.account.repository.AccountRepository;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiType;
import com.recommtoon.recommtoonapi.mbti.repository.MbtiRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MbtiRepository mbtiRepository;

    public boolean isUsernameDuplicate(String username) {
        return accountRepository.findByUsername(username) == null;
    }

    public boolean isNickNameDuplicate(String nickName) {
        return accountRepository.findByNickName(nickName) == null;
    }

    public Account register(RegisterDto registerDto) {
        Mbti mbti = mbtiRepository.findByMbtiType(MbtiType.from(registerDto.getMbtiType()));

        Account account = Account.builder()
                .realName(registerDto.getRealName())
                .username(registerDto.getUsername())
                .nickName(registerDto.getNickname())
                .password(registerDto.getPassword())
                .gender(registerDto.getGender())
                .mbti(mbti)
                .role(Role.USER)
                .build();

        return accountRepository.save(account);
    }
}
