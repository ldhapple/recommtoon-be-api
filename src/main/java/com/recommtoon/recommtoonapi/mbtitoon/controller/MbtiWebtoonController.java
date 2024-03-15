package com.recommtoon.recommtoonapi.mbtitoon.controller;

import com.recommtoon.recommtoonapi.mbti.entity.Mbti;
import com.recommtoon.recommtoonapi.mbti.entity.MbtiType;
import com.recommtoon.recommtoonapi.mbtitoon.dto.MbtiFavoriteToonDto;
import com.recommtoon.recommtoonapi.mbtitoon.entity.MbtiWebtoon;
import com.recommtoon.recommtoonapi.mbtitoon.service.MbtiWebtoonService;
import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiError;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MBTI별 선호 웹툰 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mbti")
public class MbtiWebtoonController {

    private final MbtiWebtoonService mbtiWebtoonService;

    @Operation(summary = "MBTI별 선호 웹툰 조회", description = "각 MBTI별 선호 웹툰을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹툰 정보 조회 성공", content = @Content(schema = @Schema(implementation = MbtiFavoriteToonDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/favorite/{mbtiType}")
    public ApiSuccess<List<MbtiFavoriteToonDto>> getMbtiFavoriteToon(@PathVariable("mbtiType") String mbtiType) {
        Mbti mbti = Mbti.create(MbtiType.from(mbtiType));

        List<Webtoon> initMbtiFavoriteWebtoons = mbtiWebtoonService.getInitMbtiFavoriteWebtoons(mbti);

        List<MbtiFavoriteToonDto> mbtiFavoriteToons = initMbtiFavoriteWebtoons.stream()
                .map(w -> new MbtiFavoriteToonDto(w.getTitleId(), w.getImgSrc()))
                .collect(Collectors.toList());

        return ApiUtil.success(mbtiFavoriteToons);
    }
}
