package com.recommtoon.recommtoonapi.evaluation.entity;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.base.entity.BaseEntity;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Evaluation extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;

    private Double rating;

    @Builder
    public Evaluation(Account account, Webtoon webtoon, Double rating) {
        this.account = account;
        this.webtoon = webtoon;
        this.rating = rating;
    }

    public void updateRating(Double rating) {
        this.rating = rating;
    }
}
