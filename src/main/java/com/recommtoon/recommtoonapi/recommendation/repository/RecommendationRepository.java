package com.recommtoon.recommtoonapi.recommendation.repository;

import com.recommtoon.recommtoonapi.recommendation.entity.Recommendation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @Query("select r from Recommendation r join fetch r.recommWebtoons w where r.account.id = :userId")
    Optional<Recommendation> findByAccountId(@Param(("userId")) Long userId);
}
