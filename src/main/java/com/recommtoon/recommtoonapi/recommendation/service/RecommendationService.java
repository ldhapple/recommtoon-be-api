package com.recommtoon.recommtoonapi.recommendation.service;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.entity.Gender;
import com.recommtoon.recommtoonapi.account.repository.AccountRepository;
import com.recommtoon.recommtoonapi.evaluation.entity.Evaluation;
import com.recommtoon.recommtoonapi.evaluation.repository.EvaluationRepository;
import com.recommtoon.recommtoonapi.exception.NotFoundException;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.recommendation.util.SimilarityCaclulator;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.repository.WebtoonRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
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
    private final SimilarityCaclulator similarityCalculator;

    public Set<Webtoon> recommendWebtoon(String userName, int K) {

        Account loginUser = accountRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
        Long userId = loginUser.getId();

        List<Account> allUsers = accountRepository.findAll();
        List<Webtoon> allWebtoons = webtoonRepository.findAll();
        List<Evaluation> evaluations = evaluationRepository.findByAccountId(userId);
        int userIndex = allUsers.indexOf(loginUser);

        // 사용자 정보 (성별, MBTI, 나이)와 평가 정보를 벡터로 전환
        double[][] userVectors = allUsers.stream()
                .map(similarityCalculator::preprocessUserData)
                .toArray(double[][]::new);

        double[][] ratingVectors = allUsers.stream()
                .map(user -> similarityCalculator.getUserRatingVector(allWebtoons, evaluations))
                .toArray(double[][]::new);

        // 추천 대상 유저와 각 유저들간의 코사인 유사도 계산
        double[] combinedSimilarities = new double[allUsers.size()];

        for (int i = 0; i < allUsers.size(); i++) {
            double personalSimilarity = similarityCalculator.cosineSimilarity(userVectors[userIndex],
                    userVectors[i]);
            double ratingSimilarity = similarityCalculator.cosineSimilarity(ratingVectors[userIndex],
                    ratingVectors[i]);
            combinedSimilarities[i] = 0.4 * personalSimilarity + 0.6 * ratingSimilarity; // 유사도 통합 (평가 유사도에 더 높은 가중치 부여)
        }

        // KNN 적용 (비슷한 이웃 K만큼 찾기)
        int[] neighbors = findKNearestNeighbors(combinedSimilarities, K);

        // 이웃들이 높게 평가한 웹툰 추천
        return getRecommendedWebtoons(neighbors, userId, 24);
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

    private Set<Webtoon> getRecommendedWebtoons(int[] neighbors, Long userId, int maxRecommendations) {
        Map<Webtoon, Integer> webtoonFrequency = new HashMap<>();

        //코사인 유사도가 높은 이웃이 평가한 웹툰 중 3점 이상으로 평가한 웹툰들을 취합
        for (int neighborId : neighbors) {
            List<Evaluation> evaluations = evaluationRepository.findByAccountId((long) neighborId);
            for (Evaluation eval : evaluations) {
                if (eval.getRating() >= 3) {
                    webtoonFrequency.put(eval.getWebtoon(), webtoonFrequency.getOrDefault(eval.getWebtoon(), 0) + 1);
                }
            }
        }

        List<Evaluation> userEvaluations = evaluationRepository.findByAccountId(userId);
        Set<Webtoon> ratedWebtoons = userEvaluations.stream().map(Evaluation::getWebtoon).collect(Collectors.toSet());

        //사용자가 평가한 웹툰 제외 및 추천 개수 설정
        Set<Webtoon> recommendations = webtoonFrequency.entrySet().stream()
                .sorted(Map.Entry.<Webtoon, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .filter(w -> !ratedWebtoons.contains(w))
                .limit(maxRecommendations)
                .collect(Collectors.toSet());

        return recommendations;
    }
}
