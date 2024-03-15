package com.recommtoon.recommtoonapi.recommendation.controller;

import com.recommtoon.recommtoonapi.recommendation.dto.WebtoonRecommendationDto;
import com.recommtoon.recommtoonapi.recommendation.service.RecommendationService;
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
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "웹툰 추천 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "개인별 웹툰 추천", description = "평가 개수가 10개 미만이면, MBTI별 선호 장르의 웹툰들이 보여진다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹툰 정보 조회 성공", content = @Content(schema = @Schema(implementation = WebtoonRecommendationDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ApiSuccess<List<WebtoonRecommendationDto>> recommendWebtoon(Authentication authentication) {
        String userName = authentication.getName();

        Set<Webtoon> recommendWebtoons = recommendationService.recommendWebtoon(userName, 23);

        List<WebtoonRecommendationDto> result = recommendWebtoons.stream()
                .map(w -> new WebtoonRecommendationDto(w.getTitleId(), w.getImgSrc()))
                .collect(Collectors.toList());

        return ApiUtil.success(result);
    }
}
