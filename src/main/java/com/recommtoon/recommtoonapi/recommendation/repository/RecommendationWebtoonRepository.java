package com.recommtoon.recommtoonapi.recommendation.repository;

import com.recommtoon.recommtoonapi.recommendation.entity.RecommendationWebtoon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationWebtoonRepository extends JpaRepository<RecommendationWebtoon, Long>, RecommendationWebtoonRepositoryForBulk {
}
