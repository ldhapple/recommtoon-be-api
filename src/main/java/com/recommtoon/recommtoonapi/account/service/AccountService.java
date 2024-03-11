package com.recommtoon.recommtoonapi.account.service;

import com.recommtoon.recommtoonapi.account.dto.RegisterDto;
import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.entity.Role;
import com.recommtoon.recommtoonapi.account.repository.AccountRepository;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiType;
import com.recommtoon.recommtoonapi.mbti.repository.MbtiRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MbtiRepository mbtiRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean isUsernameDuplicate(String username) {
        return !accountRepository.existsByUsername(username);
    }

    public boolean isNickNameDuplicate(String nickName) {
        return !accountRepository.existsByNickName(nickName);
    }

    public Account register(RegisterDto registerDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(registerDto.getPassword());
        Mbti mbti = mbtiRepository.findByMbtiType(MbtiType.from(registerDto.getMbtiType()));

        Account account = Account.builder()
                .realName(registerDto.getRealName())
                .username(registerDto.getUsername())
                .nickName(registerDto.getNickname())
                .password(encodedPassword)
                .gender(registerDto.getGender())
                .mbti(mbti)
                .role(Role.USER)
                .build();

        return accountRepository.save(account);
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsernameWithMbti(username);
    }
}
