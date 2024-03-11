package com.recommtoon.recommtoonapi.webtoon.service;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.repository.AccountRepository;
import com.recommtoon.recommtoonapi.exception.NotFoundException;
import com.recommtoon.recommtoonapi.webtoon.dto.RatingWebtoonDto;
import com.recommtoon.recommtoonapi.webtoon.repository.WebtoonRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebtoonCacheService {

    private final AccountRepository accountRepository;
    private final WebtoonRepository webtoonRepository;

    @Cacheable(value = "notEvaluatedWebtoons", key = "#loginUsername", unless = "#result == null", cacheManager = "cacheManager")
    public List<RatingWebtoonDto> getCachedNotEvaluatedWebtoon(String loginUsername) {
        Account loginUser = accountRepository.findByUsername(loginUsername)
                .orElseThrow(() -> new NotFoundException("계정 정보가 존재하지 않습니다."));

        List<RatingWebtoonDto> contents = webtoonRepository.findWebtoonsNotEvaluated(loginUser.getId());
        Collections.shuffle(contents);

        return contents;
    }
}
