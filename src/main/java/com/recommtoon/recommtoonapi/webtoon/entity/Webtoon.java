package com.recommtoon.recommtoonapi.webtoon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Webtoon {

    @Id @GeneratedValue
    private Long id;

    private String titleId;
    private String title;
    private String author;

    @Enumerated(value = EnumType.STRING)
    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "webtoon_genre", joinColumns = @JoinColumn(name = "webtoon_id"))
    @Column(name = "genre")
    private Set<Genre> genres = new HashSet<>();

//    private Set<Genre> genres = ConcurrentHashMap.newKeySet();
//    -> JPA에서는 트랜잭션과 영속성 컨텍스트로 데이터 일관성을 보장하기 때문에 동시성문제 X

    @Enumerated(value = EnumType.STRING)
    private Days days;

    @Column(length = 1500)
    private String story;

    private String imgSrc;
    private Long likeCount;

    public static Webtoon createWebtoon(String titleId, String title, String author, Set<Genre> genres,
                                        Days days, String story, String imgSrc) {
        Webtoon webtoon = new Webtoon();

        webtoon.titleId = titleId;
        webtoon.title = title;
        webtoon.author = author;
        webtoon.genres = genres;
        webtoon.days = days;
        webtoon.story = story;
        webtoon.imgSrc = imgSrc;
        webtoon.likeCount = 0L;

        return webtoon;
    }
}
