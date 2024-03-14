package com.recommtoon.recommtoonapi.recommendation.util;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.entity.Gender;
import com.recommtoon.recommtoonapi.evaluation.entity.Evaluation;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Component;

@Component
public class SimilarityCaclulator {

    public double[] getCombinedConsineSimilarity(List<Account> allUsers, List<Webtoon> allWebtoons,
                                                 List<Evaluation> userEvaluations, int userIndex) {
        double[][] userVectors = allUsers.stream()
                .map(this::preprocessUserData)
                .toArray(double[][]::new);

        double[][] ratingVectors = allUsers.stream()
                .map(user -> getUserRatingVector(allWebtoons, userEvaluations))
                .toArray(double[][]::new);

        // 추천 대상 유저와 각 유저들간의 코사인 유사도 계산
        double[] combinedSimilarities = new double[allUsers.size()];

        for (int i = 0; i < allUsers.size(); i++) {
            combinedSimilarities[i] = calculateCombinedCosineSimilarity(userVectors, ratingVectors,
                    userIndex, i);
        }

        return combinedSimilarities;
    }

    private double[] preprocessUserData(Account user) {
        //Gender, Mbti, Age
        double[] vector = new double[3];

        //성별은 이진수로 남자는 0, 여성은 1의 수치 부여
        vector[0] = user.getGender() == Gender.MALE ? 0 : 1;

        //MBTI 벡터
        vector[1] = convertMbti(user.getMbti());

        //나이는 10대, 20대와 같이 10단위로 수치를 부여
        vector[2] = user.getAge() / 10 - 1;

        //가중치 부여
        vector[0] *= 0.3; // 성별
        vector[1] *= 0.5; // MBTI
        vector[2] *= 0.2; // 나이

        return vector;
    }

    private double[] getUserRatingVector(List<Webtoon> allWebtoons, List<Evaluation> userEvaluations) {
        double[] ratings = new double[allWebtoons.size()];

        for (Evaluation eval : userEvaluations) {
            int index = allWebtoons.indexOf(eval.getWebtoon());
            if (index != -1) {
                ratings[index] = eval.getRating();
            }
        }
        return ratings;
    }

    private double calculateCombinedCosineSimilarity(double[][] userVectors, double[][] ratingVectors, int userIndex,
                                                     int i) {
        double personalSimilarity = cosineSimilarity(userVectors[userIndex],
                userVectors[i]);
        double ratingSimilarity = cosineSimilarity(ratingVectors[userIndex],
                ratingVectors[i]);

        return (0.4 * personalSimilarity) + (0.6 * ratingSimilarity); // 유사도 통합 (평가 유사도에 더 높은 가중치 부여)
    }

    private double cosineSimilarity(double[] vectorA, double[] vectorB) {
        RealVector vector1 = new ArrayRealVector(vectorA);
        RealVector vector2 = new ArrayRealVector(vectorB);

        double dotProduct = vector1.dotProduct(vector2);
        double normalization = vector1.getNorm() * vector2.getNorm();

        return normalization == 0 ? 0 : (dotProduct / normalization);
    }

    private double convertMbti(Mbti mbti) {
        String mbtiType = mbti.getMbtiType().name();
        String mbtiCode = mbtiType.substring(2); //TJ, TP, FJ, FP

        return switch (mbtiCode) {
            case "TJ" -> 0.2;
            case "TP" -> 0.4;
            case "FJ" -> 0.6;
            case "FP" -> 0.8;
            default -> 1;
        };
    }
}
