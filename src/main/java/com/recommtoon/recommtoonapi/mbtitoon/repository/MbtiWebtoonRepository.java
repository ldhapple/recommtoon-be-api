package com.recommtoon.recommtoonapi.mbtitoon.repository;

import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbtitoon.entity.MbtiWebtoon;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MbtiWebtoonRepository extends JpaRepository<MbtiWebtoon, Long> {

    @Query("select mw from MbtiWebtoon mw join fetch mw.webtoon where mw.mbti = :mbti")
    List<MbtiWebtoon> findByMbti(@Param("mbti") Mbti mbti);
}
