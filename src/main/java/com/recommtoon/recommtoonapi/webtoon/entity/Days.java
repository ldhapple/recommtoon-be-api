package com.recommtoon.recommtoonapi.webtoon.entity;

import lombok.Getter;

@Getter
public enum Days {
    MON("월요웹툰"),
    TUE("화요웹툰"),
    WED("수요웹툰"),
    TUR("목요웹툰"),
    FRI("금요웹툰"),
    SAT("토요웹툰"),
    SUN("일요웹툰");

    private final String title;

    Days(String title) {
        this.title = title;
    }
}
