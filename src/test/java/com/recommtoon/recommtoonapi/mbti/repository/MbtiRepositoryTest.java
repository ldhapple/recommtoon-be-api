package com.recommtoon.recommtoonapi.mbti.repository;

import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MbtiRepositoryTest {

    @Autowired
    MbtiRepository mbtiRepository;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 데이터 삽입
        Mbti mbti = Mbti.create(MbtiType.ISTJ);
        mbtiRepository.save(mbti);
    }

    @Test
    void testFindByMbtiType() {
        MbtiType mbtiType = MbtiType.from("istj");

        Mbti findMbti = mbtiRepository.findByMbtiType(mbtiType);

        assertThat(findMbti.getMbtiType()).isEqualTo(MbtiType.ISTJ);
    }
}