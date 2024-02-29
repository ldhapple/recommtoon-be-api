package com.recommtoon.recommtoonapi.account.entity;

import com.recommtoon.recommtoonapi.base.entity.BaseEntity;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbti_id")
    private Mbti mbti;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String realName;
    private String username;
    private String password;


}
