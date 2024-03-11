package com.recommtoon.recommtoonapi.account.repository;

import com.recommtoon.recommtoonapi.account.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Boolean existsByUsername(String username);
    Boolean existsByNickName(String nickname);

    Optional<Account> findByUsername(String username);
    Optional<Account> findByNickName(String nickname);

    @Query("select a from Account a join fetch a.mbti where a.username = :username")
    Account findByUsernameWithMbti(@Param("username") String username);
}
