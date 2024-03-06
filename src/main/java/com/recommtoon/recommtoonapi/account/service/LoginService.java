package com.recommtoon.recommtoonapi.account.service;

import com.recommtoon.recommtoonapi.account.dto.CustomUserDetails;
import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account findAccount = accountRepository.findByUsername(username);

        if (findAccount != null) {
            return new CustomUserDetails(findAccount);
        }

        return null;
    }
}