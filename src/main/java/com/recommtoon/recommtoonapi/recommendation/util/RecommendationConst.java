package com.recommtoon.recommtoonapi.recommendation.util;

import lombok.Getter;

@Getter
public enum RecommendationConst {
    MINIMUM_EAVLUATION_COUNT(10),
    RECOMMENDATION_COUNT(24),
    TOTAL_RECOMMENDATION_COUNT(40),
    HIGH_RATING_THRESHOLD(1);

    private final int value;

    RecommendationConst(int value) {
        this.value = value;
    }
}
