package com.recommtoon.recommtoonapi.webtoon.repository;

import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
    @Query("select w from Webtoon w where w.titleId = :titleId")
    List<Webtoon> findByTitleId(@Param("titleId") String titleId);
}
