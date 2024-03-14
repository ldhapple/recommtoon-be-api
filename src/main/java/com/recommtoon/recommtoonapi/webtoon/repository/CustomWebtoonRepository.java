package com.recommtoon.recommtoonapi.webtoon.repository;

import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiType;
import com.recommtoon.recommtoonapi.mbtitoon.dto.MbtiFavoriteToonDto;
import com.recommtoon.recommtoonapi.webtoon.dto.RatingWebtoonDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomWebtoonRepository {
    List<RatingWebtoonDto> findWebtoonsNotEvaluated(Long userId);
    List<MbtiFavoriteToonDto> findTopRatedWebtoonsByMbti(MbtiType mbtiTypepe);
}
