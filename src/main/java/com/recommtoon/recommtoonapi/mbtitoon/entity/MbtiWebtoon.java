package com.recommtoon.recommtoonapi.mbtitoon.entity;

import com.recommtoon.recommtoonapi.base.entity.BaseEntity;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MbtiWebtoon extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mbti_id")
    private Mbti mbti;

    @ManyToOne
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;
}
