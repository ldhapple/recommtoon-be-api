package com.recommtoon.recommtoonapi.mbti.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MbtiType {
    ISTJ, ESTJ, ISFJ, ESFJ, ISFP, ESFP, ISTP, ESTP,
    INTJ, ENTJ, INFJ, ENFJ, INFP, ENFP, INTP, ENTP;

    @JsonCreator
    public static MbtiType from(String s) {
        return MbtiType.valueOf(s.toUpperCase());
    }
}
