package com.recommtoon.recommtoonapi.webtoon.repository;

import com.recommtoon.recommtoonapi.webtoon.dto.RatingWebtoonDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomWebtoonRepository {
    List<RatingWebtoonDto> findWebtoonsNotEvaluated(Long userId);
}
