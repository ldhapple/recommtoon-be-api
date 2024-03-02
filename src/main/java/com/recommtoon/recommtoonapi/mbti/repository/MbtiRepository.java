package com.recommtoon.recommtoonapi.mbti.repository;

import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MbtiRepository extends JpaRepository<Mbti, Long> {
    Mbti findByMbtiType(MbtiType mbtiType);
}
