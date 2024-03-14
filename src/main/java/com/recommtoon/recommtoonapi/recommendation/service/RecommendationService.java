package com.recommtoon.recommtoonapi.recommendation.service;

import static com.recommtoon.recommtoonapi.recommendation.util.RecommendationConst.*;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.entity.Gender;
import com.recommtoon.recommtoonapi.account.repository.AccountRepository;
import com.recommtoon.recommtoonapi.evaluation.entity.Evaluation;
import com.recommtoon.recommtoonapi.evaluation.repository.EvaluationRepository;
import com.recommtoon.recommtoonapi.exception.NotFoundException;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.recommendation.entity.Recommendation;
import com.recommtoon.recommtoonapi.recommendation.entity.RecommendationWebtoon;
import com.recommtoon.recommtoonapi.recommendation.repository.RecommendationRepository;
import com.recommtoon.recommtoonapi.recommendation.repository.RecommendationWebtoonRepository;
import com.recommtoon.recommtoonapi.recommendation.util.RecommendationConst;
import com.recommtoon.recommtoonapi.recommendation.util.SimilarityCaclulator;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.repository.WebtoonRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smile.neighbor.KDTree;
import smile.neighbor.Neighbor;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService {

    private final AccountRepository accountRepository;
    private final EvaluationRepository evaluationRepository;
    private final WebtoonRepository webtoonRepository;
    private final RecommendationRepository recommendationRepository;
    private final RecommendationWebtoonRepository recommendationWebtoonRepository;
    private final SimilarityCaclulator similarityCalculator;
    private final MbtiRecommendationService mbtiRecommendationService;

    public Set<Webtoon> recommendWebtoon(String userName, int K) {

        Account loginUser = accountRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
        Long userId = loginUser.getId();
        Long evaluationCount = evaluationRepository.countByAccountId(userId);

        Recommendation userRecommendation = getOrCreateRecommendation(userId, loginUser, evaluationCount);

        //평가 개수가 10개 미만이면 선호 웹툰 장르를 통한 추천.
        List<Evaluation> userEvaluations = evaluationRepository.findByAccountId(userId);
        if (evaluationCount < MINIMUM_EAVLUATION_COUNT.getValue()) {
            return mbtiRecommendationService.addMbtiSuffixFavoriteWebtoon(loginUser.getMbti(), new HashSet<>(),
                    userEvaluations);
        }

        //평가 개수가 변하지 않았다면 기존 추천 웹툰들을 보여줌.
        if (isEvaluationCountNotChange(userRecommendation, evaluationCount)) {
            return getPreviousRecommendWebtoons(userRecommendation);
        }

        // KNN 적용 (비슷한 이웃 K만큼 찾기)
        int[] neighbors = KnnGetNeighbors(loginUser, userEvaluations, K);

        Set<Webtoon> recommendationResult = getRecommendationResults(neighbors, userEvaluations, loginUser);
        saveRecommendationResults(userRecommendation, evaluationCount, recommendationResult);

        return recommendationResult;
    }

    private Set<Webtoon> getRecommendationResults(int[] neighbors, List<Evaluation> userEvaluations, Account loginUser) {
        Set<Webtoon> personalRecommendWebtoons = getNeighborRecommendedWebtoons(neighbors, userEvaluations, RECOMMENDATION_COUNT.getValue());

        return mbtiRecommendationService.addMbtiSuffixFavoriteWebtoon(loginUser.getMbti(),
                personalRecommendWebtoons, userEvaluations);
    }

    private void saveRecommendationResults(Recommendation userRecommendation, Long evaluationCount, Set<Webtoon> recommendationResult) {
        Recommendation savedRecommendation = recommendationRepository.save(userRecommendation);
        savedRecommendation.updateEvaluationCount(evaluationCount);

        Set<RecommendationWebtoon> recommendationWebtoons = recommendationResult.stream()
                .map(webtoon -> new RecommendationWebtoon(savedRecommendation, webtoon))
                .collect(Collectors.toSet());

        recommendationWebtoonRepository.batchInsertRecommendationWebtoons(recommendationWebtoons);
    }

    private int[] KnnGetNeighbors(Account loginUser, List<Evaluation> userEvaluations, int K) {
        List<Account> allUsers = accountRepository.findAll();
        List<Webtoon> allWebtoons = webtoonRepository.findAll();
        int userIndex = allUsers.indexOf(loginUser);

        // 추천 대상 유저와 각 유저들간의 코사인 유사도 계산
        double[] combinedSimilarities = similarityCalculator.getCombinedConsineSimilarity(allUsers, allWebtoons,
                userEvaluations, userIndex);

        return findKNearestNeighbors(combinedSimilarities, K);
    }

    private boolean isEvaluationCountNotChange(Recommendation userRecommendation, Long evaluationCount) {
        return userRecommendation.getEvaluationCount().equals(evaluationCount);
    }

    private Set<Webtoon> getPreviousRecommendWebtoons(Recommendation userRecommendation) {
        return userRecommendation.getRecommWebtoons().stream()
                .map(RecommendationWebtoon::getWebtoon)
                .collect(Collectors.toSet());
    }

    private Recommendation getOrCreateRecommendation(Long userId, Account loginUser, Long evaluationCount) {
        Recommendation userRecommendation = recommendationRepository.findByAccountId(userId)
                .orElseGet(() -> Recommendation.builder()
                        .account(loginUser)
                        .recommWebtoons(new HashSet<>())
                        .evaluationCount(evaluationCount)
                        .build());

        if (!userRecommendation.getEvaluationCount().equals(evaluationCount)) {
            userRecommendation.clearRecommendedWebtoons();
        }

        return userRecommendation;
    }

    private int[] findKNearestNeighbors(double[] similarities, int K) {
        /*
        코사인 유사도 배열이 유저와 다른 유저들간의 유사성이므로
        코사인 유사도 배열을 내림차순으로 정렬하고, 유사한 K만큼의 이웃을 추출한다. (인덱스를 반환한다.)
         */

        return IntStream.range(0, similarities.length)
                .boxed()
                .sorted((i, j) -> Double.compare(similarities[j], similarities[i]))
                .limit(K)
                .mapToInt(i -> i)
                .toArray();
    }

    private Set<Webtoon> getNeighborRecommendedWebtoons(int[] neighbors, List<Evaluation> userEvaluations,
                                                int recommendationCount) {
        Map<Webtoon, Integer> webtoonFrequency = new HashMap<>();

        //코사인 유사도가 높은 이웃이 평가한 웹툰 중 높게 평가한 웹툰들의 빈도수를 정리
        for (int neighborId : neighbors) {
            List<Evaluation> neighborEvaluations = evaluationRepository.findByAccountId((long) neighborId);
            filterHighRatedWebtoons(neighborEvaluations, webtoonFrequency);
        }

        Set<Webtoon> ratedWebtoons = userEvaluations.stream()
                .map(Evaluation::getWebtoon)
                .collect(Collectors.toSet());

        return getRecommendationExcludingEvaluated(recommendationCount,
                webtoonFrequency, ratedWebtoons);
    }

    private static void filterHighRatedWebtoons(List<Evaluation> neighborEvaluations,
                                                   Map<Webtoon, Integer> webtoonFrequency) {
        double averageRating = calculateUserAverageRating(neighborEvaluations);

        neighborEvaluations.stream()
                .filter(eval -> isHighRated(eval, averageRating))
                .forEach(eval -> {
                    Webtoon webtoon = eval.getWebtoon();
                    webtoonFrequency.put(webtoon, webtoonFrequency.getOrDefault(webtoon, 0) + 1);
                });
    }

    private static boolean isHighRated(Evaluation eval, double averageRating) {
        return Math.abs(averageRating - eval.getRating()) >= HIGH_RATING_THRESHOLD.getValue();
    }

    private static double calculateUserAverageRating(List<Evaluation> evaluations) {
        return evaluations.stream()
                .mapToDouble(Evaluation::getRating)
                .average()
                .orElse(0);
    }

    private static Set<Webtoon> getRecommendationExcludingEvaluated(int recommendationCount,
                                                                    Map<Webtoon, Integer> webtoonFrequency,
                                                                    Set<Webtoon> ratedWebtoons) {

        //사용자가 평가한 웹툰 제외 및 추천 개수만큼 내림차순 추출

        return webtoonFrequency.entrySet().stream()
                .sorted(Map.Entry.<Webtoon, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .filter(w -> !ratedWebtoons.contains(w))
                .limit(recommendationCount)
                .collect(Collectors.toSet());
    }
}
