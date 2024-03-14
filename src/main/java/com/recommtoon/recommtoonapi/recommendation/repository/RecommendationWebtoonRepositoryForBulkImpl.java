package com.recommtoon.recommtoonapi.recommendation.repository;

import com.recommtoon.recommtoonapi.recommendation.entity.RecommendationWebtoon;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecommendationWebtoonRepositoryForBulkImpl implements RecommendationWebtoonRepositoryForBulk{
    private final JdbcTemplate jdbcTemplate;

    public RecommendationWebtoonRepositoryForBulkImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void batchInsertRecommendationWebtoons(Set<RecommendationWebtoon> recommendationWebtoons) {
        String sql = "insert into recommendation_webtoon (recommendation_id, webtoon_id) values (?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (RecommendationWebtoon recommendationWebtoon : recommendationWebtoons) {
            Object[] values = {
                    recommendationWebtoon.getRecommendation().getId(),
                    recommendationWebtoon.getWebtoon().getId()
            };
            batchArgs.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
