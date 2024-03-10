package com.recommtoon.recommtoonapi.webtoon.repository;

import com.recommtoon.recommtoonapi.webtoon.dto.RatingWebtoonDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomWebtoonRepository {
    Page<RatingWebtoonDto> findWebtoonsNotEvaluatedAndShuffled(Long userId, Pageable pageable);
}
