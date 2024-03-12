package com.recommtoon.recommtoonapi.webtoon.service;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.repository.AccountRepository;
import com.recommtoon.recommtoonapi.evaluation.repository.EvaluationRepository;
import com.recommtoon.recommtoonapi.exception.NotFoundException;
import com.recommtoon.recommtoonapi.webtoon.dto.RatingWebtoonDto;
import com.recommtoon.recommtoonapi.webtoon.dto.WebtoonBoardDto;
import com.recommtoon.recommtoonapi.webtoon.entity.Genre;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.repository.CustomWebtoonRepository;
import com.recommtoon.recommtoonapi.webtoon.repository.WebtoonRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonCacheService webtoonCacheService;


    public Page<RatingWebtoonDto> getNoEvaluateCards(String loginUsername, int page,
                                                     int size) {
        List<RatingWebtoonDto> allNotEvaluatedWebtoons = webtoonCacheService.getCachedNotEvaluatedWebtoon(loginUsername);

        int fromIndex = page * size;
        int toIndex = Math.min((page + 1) * size, allNotEvaluatedWebtoons.size());
        List<RatingWebtoonDto> subList = allNotEvaluatedWebtoons.subList(fromIndex, toIndex);

        return new PageImpl<>(subList, PageRequest.of(page, size), allNotEvaluatedWebtoons.size());
    }

    public Webtoon findById(Long id) {
        return webtoonRepository.findById(id).stream()
                .findFirst()
                .orElseThrow();
    }

    public WebtoonBoardDto getWebtoonByTitleId(String titleId) {
        Webtoon webtoon = webtoonRepository.findByTitleId(titleId)
                .orElseThrow(() -> new NotFoundException("웹툰 정보가 존재하지 않습니다."));

        return WebtoonBoardDto.builder()
                .title(webtoon.getTitle())
                .link("https://comic.naver.com/webtoon/list?titleId=" + webtoon.getTitleId())
                .author(webtoon.getAuthor())
                .story(webtoon.getStory())
                .imgSrc(webtoon.getImgSrc())
                .genres(webtoon.getGenres().stream()
                        .map(Genre::getKoreanName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
