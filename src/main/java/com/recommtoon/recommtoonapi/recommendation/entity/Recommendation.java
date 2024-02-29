package com.recommtoon.recommtoonapi.recommendation.entity;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.base.entity.BaseEntity;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id")
    private List<Webtoon> recommWebtoon = new ArrayList<>();
}
