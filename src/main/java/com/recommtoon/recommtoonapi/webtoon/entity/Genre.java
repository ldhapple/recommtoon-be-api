package com.recommtoon.recommtoonapi.webtoon.entity;

import lombok.Getter;

@Getter
public enum Genre {
    ACTION("액션"),
    DRAMA("드라마"),
    FANTASY("판타지"),
    ROMANCE("로맨스"),
    DAILY("일상"),
    MUNCHKIN("먼치킨"),
    SCHOOL("학원물"),
    COMIC("코믹"),
    HORROR("공포"),
    THRILLER("스릴러"),
    MARTIAL_ARTS("무협");

    private final String koreanName;

    Genre(String koreanName) {
        this.koreanName = koreanName;
    }

    public static Genre isExistGenre(String crawlingGenre) {
        for (Genre genre : Genre.values()) {
            if (genre.getKoreanName().equals(crawlingGenre)) {
                return genre;
            }
        }

        return null;
    }
}
