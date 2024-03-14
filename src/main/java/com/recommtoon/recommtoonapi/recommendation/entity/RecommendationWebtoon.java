package com.recommtoon.recommtoonapi.recommendation.entity;

import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@BatchSize(size = 40)
//@SequenceGenerator(name = "recommendation_webtoon_seq_generator",
//        sequenceName = "recommendation_webtoon_seq",
//        allocationSize = 40)
public class RecommendationWebtoon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;

    public RecommendationWebtoon(Recommendation recommendation, Webtoon webtoon) {
        this.recommendation = recommendation;
        this.webtoon = webtoon;
    }
}
