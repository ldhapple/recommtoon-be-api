package com.recommtoon.recommtoonapi.recommendation.service;

import static com.recommtoon.recommtoonapi.recommendation.util.RecommendationConst.*;

import com.recommtoon.recommtoonapi.annotation.TimeTrace;
import com.recommtoon.recommtoonapi.evaluation.entity.Evaluation;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiSuffix;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiType;
import com.recommtoon.recommtoonapi.mbtitoon.entity.MbtiWebtoon;
import com.recommtoon.recommtoonapi.mbtitoon.repository.MbtiWebtoonRepository;
import com.recommtoon.recommtoonapi.recommendation.util.RecommendationConst;
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

    @TimeTrace
    public Set<Webtoon> addMbtiSuffixFavoriteWebtoon(Mbti userMbti, Set<Webtoon> personalRecommendationResult, List<Evaluation> userEvaluations) {

        Set<Long> ratedWebtoonIds = userEvaluations
                .stream()
                .map(evaluation -> evaluation.getWebtoon().getId())
                .collect(Collectors.toSet());

        Set<Webtoon> result = personalRecommendationResult;

        int recommendationCount = personalRecommendationResult.size();

        String userMbtiSuffix = userMbti.getMbtiType().name().substring(2);
        Set<Genre> favoriteGenres = MbtiSuffix.getGenres(userMbtiSuffix);


        int mbtiSuffixFavoriteWebtoonCount = (TOTAL_RECOMMENDATION_COUNT.getValue() - recommendationCount) / 3;


        for (Genre favoriteGenre : favoriteGenres) {
            String genreName = favoriteGenre.name();

            Set<Webtoon> randomWebtoonsByFavoriteGenreName = webtoonRepository.findRandomWebtoonsByFavoriteGenreName(
                    genreName, mbtiSuffixFavoriteWebtoonCount);

            randomWebtoonsByFavoriteGenreName.stream()
                    .filter(webtoon -> !ratedWebtoonIds.contains(webtoon.getId()))
                    .forEach(result::add);
        }

        return result;
    }
}
