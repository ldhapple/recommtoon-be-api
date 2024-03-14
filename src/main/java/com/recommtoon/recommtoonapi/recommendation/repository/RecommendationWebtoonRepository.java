package com.recommtoon.recommtoonapi.recommendation.repository;

import com.recommtoon.recommtoonapi.recommendation.entity.RecommendationWebtoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendationWebtoonRepository extends JpaRepository<RecommendationWebtoon, Long>, RecommendationWebtoonRepositoryForBulk {

    @Modifying
    @Query("delete from RecommendationWebtoon rw where rw.recommendation.id = :recommendationId")
    void deleteByRecommendationId(@Param("recommendationId") Long recommendationId);
}
