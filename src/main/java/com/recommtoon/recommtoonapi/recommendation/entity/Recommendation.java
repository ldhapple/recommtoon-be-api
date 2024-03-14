package com.recommtoon.recommtoonapi.recommendation.entity;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.base.entity.BaseEntity;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecommendationWebtoon> recommWebtoons = new HashSet<>();

    private Long evaluationCount;

    public void addRecommendedWebtoon(Webtoon webtoon) {
        RecommendationWebtoon recommWebtoon = new RecommendationWebtoon(this, webtoon);
        recommWebtoons.add(recommWebtoon);
    }

    public void clearRecommendedWebtoons() {
        this.recommWebtoons.clear();
    }

    public void updateEvaluationCount(Long evaluationCount) {
        this.evaluationCount = evaluationCount;
    }

    @Builder
    public Recommendation(Account account, Set<RecommendationWebtoon> recommWebtoons, Long evaluationCount) {
        this.account = account;
        this.recommWebtoons = recommWebtoons;
        this.evaluationCount = evaluationCount;
    }
}
