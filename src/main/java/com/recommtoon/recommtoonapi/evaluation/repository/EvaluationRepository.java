package com.recommtoon.recommtoonapi.evaluation.repository;

import com.recommtoon.recommtoonapi.evaluation.entity.Evaluation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Long countByAccountId(Long id);

    Optional<Evaluation> findByAccountIdAndWebtoonId(Long accountId, Long webtoonId);
}
