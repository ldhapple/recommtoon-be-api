package com.recommtoon.recommtoonapi.account.repository;

import com.recommtoon.recommtoonapi.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Boolean existsByUsername(String username);
    Boolean existsByNickName(String nickname);

    Account findByUsername(String username);
    Account findByNickName(String nickname);
}
