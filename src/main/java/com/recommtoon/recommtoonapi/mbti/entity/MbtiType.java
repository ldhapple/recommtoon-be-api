package com.recommtoon.recommtoonapi.mbti.entity;

import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.ACTION;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.COMIC;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.DAILY;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.DRAMA;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.FANTASY;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.HORROR;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.MARTIAL_ARTS;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.MUNCHKIN;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.ROMANCE;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.SCHOOL;
import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.THRILLER;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.recommtoon.recommtoonapi.webtoon.entity.Genre;
import java.util.Set;
import lombok.Getter;

@Getter
public enum MbtiType {
    ISTJ(Set.of(ROMANCE, DAILY, COMIC, ACTION)),
    ESTJ(Set.of(ACTION, ROMANCE, THRILLER, MUNCHKIN)),
    ISFJ(Set.of(ACTION, FANTASY, COMIC, HORROR)),
    ESFJ(Set.of(ROMANCE, ACTION, FANTASY, COMIC)),
    ISFP(Set.of(ROMANCE, ACTION, SCHOOL, MUNCHKIN)),
    ESFP(Set.of(ACTION, ROMANCE, DRAMA, FANTASY)),
    ISTP(Set.of(FANTASY, MUNCHKIN, MARTIAL_ARTS, ACTION)),
    ESTP(Set.of(ACTION, FANTASY, THRILLER, COMIC)),
    INTJ(Set.of(ACTION, THRILLER, ROMANCE, DRAMA)),
    ENTJ(Set.of(THRILLER, ACTION, FANTASY, MARTIAL_ARTS)),
    INFJ(Set.of(FANTASY, ROMANCE, ACTION, DAILY)),
    ENFJ(Set.of(FANTASY, COMIC, ACTION, MUNCHKIN)),
    INFP(Set.of(ROMANCE, DAILY, ACTION, SCHOOL)),
    ENFP(Set.of(ACTION, ROMANCE, DRAMA, DAILY)),
    INTP(Set.of(FANTASY, MUNCHKIN, ACTION, ROMANCE)),
    ENTP(Set.of(DRAMA, ROMANCE, FANTASY, MUNCHKIN));

    @JsonCreator
    public static MbtiType from(String s) {
        return MbtiType.valueOf(s.toUpperCase());
    }

    private final Set<Genre> favoriteGenres;

    MbtiType(Set<Genre> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }
}
