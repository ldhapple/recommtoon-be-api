package com.recommtoon.recommtoonapi.comment.entity;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.base.entity.BaseEntity;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comments extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;

    @Column(nullable = false, length = 500)
    private String content;

    private Long likeCount;

    public void updateLikeCount() {
        this.likeCount += 1;
    }

    @Builder
    public Comments(Account account, Webtoon webtoon, String content) {
        this.account = account;
        this.webtoon = webtoon;
        this.content = content;
        this.likeCount = 0L;
    }
}
