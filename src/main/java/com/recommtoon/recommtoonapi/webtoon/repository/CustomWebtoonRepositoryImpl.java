package com.recommtoon.recommtoonapi.webtoon.repository;

import static com.recommtoon.recommtoonapi.account.entity.QAccount.account;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recommtoon.recommtoonapi.account.entity.Gender;
import com.recommtoon.recommtoonapi.account.entity.QAccount;
import com.recommtoon.recommtoonapi.evaluation.entity.QEvaluation;
import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiType;
import com.recommtoon.recommtoonapi.mbtitoon.dto.MbtiFavoriteToonDto;
import com.recommtoon.recommtoonapi.webtoon.dto.FriendsWebtoonDto;
import com.recommtoon.recommtoonapi.webtoon.dto.RatingWebtoonDto;
import com.recommtoon.recommtoonapi.webtoon.entity.QWebtoon;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CustomWebtoonRepositoryImpl implements CustomWebtoonRepository {

    private final JPAQueryFactory queryFactory;

    public CustomWebtoonRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<RatingWebtoonDto> findWebtoonsNotEvaluated(Long userId) {

        QEvaluation qEvaluation = QEvaluation.evaluation;
        QWebtoon qWebtoon = QWebtoon.webtoon;

        List<Long> evaluatedWebtoonIds = queryFactory
                .select(qEvaluation.webtoon.id)
                .from(qEvaluation)
                .where(qEvaluation.account.id.eq(userId))
                .fetch();

        List<RatingWebtoonDto> content = queryFactory
                .select(Projections.constructor(RatingWebtoonDto.class,
                        qWebtoon.id.as("id"),
                        qWebtoon.imgSrc.as("imgSrc")))
                .from(qWebtoon)
                .where(qWebtoon.id.notIn(evaluatedWebtoonIds))
                .fetch();

        //offset, limit 사용 X

//        Collections.shuffle(content);
//
//        int fromIndex = (int) pageable.getOffset();
//        int toIndex = Math.min(fromIndex + pageable.getPageSize(), content.size());
//        List<RatingWebtoonDto> subList = content.subList(fromIndex, toIndex);
//
//        return new PageImpl<>(subList, pageable, content.size());

        return content;
    }

    @Override
    public List<MbtiFavoriteToonDto> findTopRatedWebtoonsByMbti(MbtiType mbtiType) {
        QAccount account = QAccount.account;
        QEvaluation evaluation = QEvaluation.evaluation;
        QWebtoon webtoon = QWebtoon.webtoon;

        int limit = 20;

        return queryFactory
                .select(Projections.constructor(MbtiFavoriteToonDto.class,
                        webtoon.titleId,
                        webtoon.imgSrc))
                .from(evaluation)
                .join(evaluation.account, account)
                .join(evaluation.webtoon, webtoon)
                .where(account.mbti.mbtiType.eq(mbtiType))
                .groupBy(webtoon.id)
                .orderBy(evaluation.id.count().desc(), evaluation.rating.avg().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public Page<FriendsWebtoonDto> findSearchConditionWebtoon(String name, String gender, Integer age,
                                                              Pageable pageable) {
        QWebtoon webtoon = QWebtoon.webtoon;
        QAccount account = QAccount.account;
        QEvaluation evaluation = QEvaluation.evaluation;

        JPAQuery<FriendsWebtoonDto> query = queryFactory
                .select(Projections.constructor(FriendsWebtoonDto.class,
                        webtoon.titleId,
                        webtoon.imgSrc,
                        evaluation.rating.avg().as("avgRating")))
                .from(evaluation)
                .join(evaluation.webtoon, webtoon)
                .join(evaluation.account, account)
                .where(usernameEq(name), genderEq(gender), ageEq(age))
                .groupBy(webtoon.titleId, webtoon.imgSrc)
                .orderBy(evaluation.rating.avg().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<FriendsWebtoonDto> content = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? account.username.eq(username) : null;
    }

    private BooleanExpression genderEq(String gender) {
        return hasText(gender) ? account.gender.eq(Gender.from(gender)) : null;
    }

    private BooleanExpression ageEq(Integer age) {
        return age != null ? account.age.eq(age) : null;
    }
}
