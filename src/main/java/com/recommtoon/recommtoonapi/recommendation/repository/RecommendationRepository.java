package com.recommtoon.recommtoonapi.recommendation.repository;

import com.recommtoon.recommtoonapi.recommendation.entity.Recommendation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    Optional<Recommendation> findByAccountId(Long userId);
}
