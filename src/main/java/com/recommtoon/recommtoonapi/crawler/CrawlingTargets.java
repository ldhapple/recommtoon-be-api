package com.recommtoon.recommtoonapi.crawler;

import lombok.Getter;

@Getter
public enum CrawlingTargets {
    BODY("body"),
    SRC("src"),
    ITEM("item"),
    TITLE("EpisodeListInfo__title--mYLjC"),
    DAYS_TAB("SubNavigationBar__link--PXX5B"),
    THUMBNAIL("Poster__image--d9XTI"),
    AUTHOR("ContentMetaInfo__link--xTtO6"),
    GENRES("TagGroup__tag--xu0OH"),
    STORY("EpisodeListInfo__summary--Jd1WG"),
    DAYS("ContentMetaInfo__info_item--utGrf");

    private final String className;

    CrawlingTargets(String className) {
        this.className = className;
    }
}
