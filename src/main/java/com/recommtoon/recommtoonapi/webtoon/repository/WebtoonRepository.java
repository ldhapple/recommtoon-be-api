package com.recommtoon.recommtoonapi.webtoon.repository;

import com.recommtoon.recommtoonapi.webtoon.entity.Genre;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon, Long>, CustomWebtoonRepository {
    @Query("select w from Webtoon w where w.titleId = :titleId")
    Optional<Webtoon> findByTitleId(@Param("titleId") String titleId);

    @Query(value = "select * from webtoon w inner join webtoon_genre wg on w.id = wg.webtoon_id where wg.genre = ?1 order by RAND() LIMIT ?2", nativeQuery = true)
    Set<Webtoon> findRandomWebtoonsByFavoriteGenreName(String genreName, int count);

    Page<Webtoon> findByTitleContainingIgnoreCase(String searchParam, Pageable pageable);
}
