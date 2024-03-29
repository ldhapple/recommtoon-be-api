package com.recommtoon.recommtoonapi.account.entity;

import com.recommtoon.recommtoonapi.base.entity.BaseEntity;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbti_id")
    private Mbti mbti;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String realName;
    private String username;
    private String nickName;
    private String password;
    private int age;

    @Builder
    public Account(String realName, String username, String nickName, String password, Gender gender, Mbti mbti, Role role, int age) {
        this.realName = realName;
        this.username = username;
        this.nickName = nickName;
        this.password = password;
        this.gender = gender;
        this.mbti = mbti;
        this.role = role;
        this.age = age;
    }
}
