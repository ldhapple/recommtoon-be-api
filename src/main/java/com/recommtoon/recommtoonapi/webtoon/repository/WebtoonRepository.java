package com.recommtoon.recommtoonapi.webtoon.repository;

import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
}
