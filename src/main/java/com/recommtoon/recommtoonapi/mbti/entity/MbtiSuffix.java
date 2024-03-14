package com.recommtoon.recommtoonapi.mbti.entity;

import static com.recommtoon.recommtoonapi.webtoon.entity.Genre.*;

import com.recommtoon.recommtoonapi.exception.NotFoundException;
import com.recommtoon.recommtoonapi.webtoon.entity.Genre;
import java.util.Arrays;
import java.util.Set;
import lombok.Getter;

@Getter
public enum MbtiSuffix {
    TJ(Set.of(ACTION, THRILLER, FANTASY)),
    TP(Set.of(ACTION, ROMANCE, DAILY)),
    FJ(Set.of(FANTASY, MUNCHKIN, ACTION)),
    FP(Set.of(FANTASY, ROMANCE, COMIC));

    private final Set<Genre> favoriteGenres;

    MbtiSuffix(Set<Genre> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }

    public static Set<Genre> getGenres(String mbtiSuffix) {
        return Arrays.stream(values())
                .filter(suffix -> suffix.name().equals(mbtiSuffix.toUpperCase()))
                .findFirst()
                .map(MbtiSuffix::getFavoriteGenres)
                .orElseThrow(() -> new NotFoundException("잘못된 MBTI 값 입니다." + mbtiSuffix));
    }
}
