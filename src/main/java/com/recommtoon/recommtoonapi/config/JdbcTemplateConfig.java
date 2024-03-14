package com.recommtoon.recommtoonapi.config;

import com.recommtoon.recommtoonapi.recommendation.repository.RecommendationWebtoonRepositoryForBulk;
import com.recommtoon.recommtoonapi.recommendation.repository.RecommendationWebtoonRepositoryForBulkImpl;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateConfig {

    private final DataSource dataSource;

    @Bean
    public RecommendationWebtoonRepositoryForBulk repository() {
        return new RecommendationWebtoonRepositoryForBulkImpl(dataSource);
    }
}
