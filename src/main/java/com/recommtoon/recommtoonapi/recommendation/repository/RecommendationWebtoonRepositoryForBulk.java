package com.recommtoon.recommtoonapi.recommendation.repository;

import com.recommtoon.recommtoonapi.recommendation.entity.RecommendationWebtoon;
import java.util.Set;

public interface RecommendationWebtoonRepositoryForBulk {
    void batchInsertRecommendationWebtoons(Set<RecommendationWebtoon> recommendationWebtoons);
}
