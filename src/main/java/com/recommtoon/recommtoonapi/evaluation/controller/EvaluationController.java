package com.recommtoon.recommtoonapi.evaluation.controller;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.service.AccountService;
import com.recommtoon.recommtoonapi.evaluation.dto.EvaluationRequestDto;
import com.recommtoon.recommtoonapi.evaluation.entity.Evaluation;
import com.recommtoon.recommtoonapi.evaluation.service.EvaluationService;
import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiError;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import com.recommtoon.recommtoonapi.webtoon.dto.RatingWebtoonDto;
import com.recommtoon.recommtoonapi.webtoon.dto.WebtoonBoardDto;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.service.WebtoonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "웹툰 점수 평가 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluation")
public class EvaluationController {

    private final WebtoonService webtoonService;
    private final AccountService accountService;
    private final EvaluationService evaluationService;

    @Operation(summary = "웹툰 정보 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹툰 정보 조회 성공", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/card")
    public ApiSuccess<?> getRatingCards(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "16") int size,
                                        Authentication authentication
    ) {
        String loginUsername = authentication.getName();

        Page<RatingWebtoonDto> webtoons = webtoonService.getNoEvaluateCards(loginUsername, page, size);

        return ApiUtil.success(webtoons);
    }

    @Operation(summary = "평가 개수 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "평가 개수 조회 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/count")
    public ApiSuccess<Long> evaluationCount(Authentication authentication) {
        String loginUsername = authentication.getName();

        Long evaluatedCount = evaluationService.getEvaluatedCount(loginUsername);

        return ApiUtil.success(evaluatedCount);
    }

    @Operation(summary = "웹툰 평가 하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹툰 평가 성공", content = @Content(schema = @Schema(implementation = Evaluation.class))),
            @ApiResponse(responseCode = "400", description = "해당 계정/웹툰이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ApiSuccess<Evaluation> evaluate(@RequestBody EvaluationRequestDto evaluationRequest,
                                           Authentication authentication) {
        Account account = accountService.findByUsername(authentication.getName());
        Webtoon webtoon = webtoonService.findById(evaluationRequest.getWebtoonId());

        Evaluation evaluation = Evaluation.builder()
                .account(account)
                .webtoon(webtoon)
                .rating(evaluationRequest.getRating())
                .build();

        Evaluation savedEvaluation = evaluationService.saveEvaluation(evaluation);

        return ApiUtil.success(savedEvaluation);
    }
}
