package com.recommtoon.recommtoonapi.recommendation.service;

import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiSuffix;
import com.recommtoon.recommtoonapi.webtoon.entity.Genre;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.repository.WebtoonRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MbtiRecommendationService {

    private final WebtoonRepository webtoonRepository;

    public Set<Webtoon> addMbtiSuffixFavoriteWebtoon(Mbti userMbti, Set<Webtoon> personalRecommendationResult) {

        Set<Webtoon> result = personalRecommendationResult;

        int recommendationCount = personalRecommendationResult.size();

        String userMbtiSuffix = userMbti.getMbtiType().name().substring(2);
        Set<Genre> favoriteGenres = MbtiSuffix.getGenres(userMbtiSuffix);


        int mbtiSuffixFavoriteWebtoonCount = (33 - recommendationCount) / 3;


        for (Genre favoriteGenre : favoriteGenres) {
            String genreName = favoriteGenre.name();

            Set<Webtoon> randomWebtoonsByFavoriteGenreName = webtoonRepository.findRandomWebtoonsByFavoriteGenreName(
                    genreName, mbtiSuffixFavoriteWebtoonCount);

            result.addAll(randomWebtoonsByFavoriteGenreName);
        }

        return result;
    }
}
