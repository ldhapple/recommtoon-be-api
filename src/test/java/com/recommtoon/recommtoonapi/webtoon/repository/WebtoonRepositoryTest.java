package com.recommtoon.recommtoonapi.webtoon.repository;

import com.recommtoon.recommtoonapi.webtoon.entity.Days;
import com.recommtoon.recommtoonapi.webtoon.entity.Genre;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WebtoonRepositoryTest {

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Test
    public void testFindByTitleId() {
        String titleId = "12345";
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.ACTION);
        Webtoon webtoon = Webtoon.createWebtoon(titleId, "test", "test", genres,
                Days.MON, "test", "test.com");

        webtoonRepository.save(webtoon);

        List<Webtoon> findWebtoon = webtoonRepository.findByTitleId(titleId);

        assertThat(webtoon).isEqualTo(findWebtoon.get(0));
    }
}