package com.recommtoon.recommtoonapi.mbtitoon.service;

import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiType;
import com.recommtoon.recommtoonapi.mbti.repository.MbtiRepository;
import com.recommtoon.recommtoonapi.mbtitoon.dto.MbtiFavoriteToonDto;
import com.recommtoon.recommtoonapi.mbtitoon.entity.MbtiWebtoon;
import com.recommtoon.recommtoonapi.mbtitoon.repository.MbtiWebtoonRepository;
import com.recommtoon.recommtoonapi.webtoon.entity.Genre;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.repository.WebtoonRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MbtiWebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final MbtiWebtoonRepository mbtiWebtoonRepository;
    private final MbtiRepository mbtiRepository;

//    public Set<Webtoon> getInitMbtiFavoriteWebtoons(Mbti mbti) {
//        String mbtiType = mbti.getMbtiType().name();
//        Mbti savedMbti = mbtiRepository.findByMbtiType(mbti.getMbtiType());
//
//        Set<Genre> favoriteGenres = MbtiType.getGenres(mbtiType);
//
//        Set<Webtoon> result = new HashSet<>();
//
//        for (Genre favoriteGenre : favoriteGenres) {
//            result.addAll(webtoonRepository.findRandomWebtoonsByFavoriteGenreName(favoriteGenre.name(), 28 / 4));
//        }
//
//        for (Webtoon favoriteWebtoon : result) {
//            MbtiWebtoon mbtiToon = MbtiWebtoon.builder()
//                    .mbti(savedMbti)
//                    .webtoon(favoriteWebtoon)
//                    .build();
//
//            mbtiWebtoonRepository.save(mbtiToon);
//        }
//
//        return result;
//    }

    public List<Webtoon> getInitMbtiFavoriteWebtoons(Mbti mbti) {
        Mbti savedMbti = mbtiRepository.findByMbtiType(mbti.getMbtiType());
        List<MbtiWebtoon> mbtiToons = mbtiWebtoonRepository.findByMbti(savedMbti);

        return mbtiToons.stream().map(MbtiWebtoon::getWebtoon).collect(Collectors.toList());
    }

    /*
    데이터 쌓인 후 적용되어야 할 로직
    하루에 한 번 결과 계산 필요할 듯
     */
    public List<MbtiFavoriteToonDto> getMbtiFavoriteWebtoons(Mbti mbti) {
        MbtiType mbtiType = mbti.getMbtiType();

        return webtoonRepository.findTopRatedWebtoonsByMbti(mbtiType);
    }
}
