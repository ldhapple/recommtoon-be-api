package com.recommtoon.recommtoonapi.mbti.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mbti {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MbtiType mbtiType;

    public static Mbti create(MbtiType mbtiType) {
        Mbti mbti = new Mbti();

        mbti.mbtiType = mbtiType;

        return mbti;
    }
}
