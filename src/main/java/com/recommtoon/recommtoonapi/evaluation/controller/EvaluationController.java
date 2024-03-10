package com.recommtoon.recommtoonapi.evaluation.controller;

import com.recommtoon.recommtoonapi.account.entity.Account;
import com.recommtoon.recommtoonapi.account.service.AccountService;
import com.recommtoon.recommtoonapi.evaluation.dto.EvaluationRequestDto;
import com.recommtoon.recommtoonapi.evaluation.entity.Evaluation;
import com.recommtoon.recommtoonapi.evaluation.service.EvaluationService;
import com.recommtoon.recommtoonapi.webtoon.dto.RatingWebtoonDto;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.service.WebtoonService;
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

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluation")
public class EvaluationController {

    private final WebtoonService webtoonService;
    private final AccountService accountService;
    private final EvaluationService evaluationService;

    @GetMapping("/card")
    public ResponseEntity<?> getRatingCards(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "16") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsername = authentication.getName();

        Page<RatingWebtoonDto> webtoons = webtoonService.getNoEvaluateCards(loginUsername, page, size);

        return ResponseEntity.ok(webtoons);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> evaluationCount(Authentication authentication) {
        String username = authentication.getName();

        Long evaluatedCount = evaluationService.getEvaluatedCount(username);

        return ResponseEntity.ok(evaluatedCount);
    }

    @PostMapping
    public ResponseEntity<Evaluation> evaluate(@RequestBody EvaluationRequestDto evaluationRequest, Authentication authentication) {
        Account account = accountService.findByUsername(authentication.getName());
        Webtoon webtoon = webtoonService.findById(evaluationRequest.getWebtoonId());

        Evaluation evaluation = Evaluation.builder()
                .account(account)
                .webtoon(webtoon)
                .rating(evaluationRequest.getRating())
                .build();

        Evaluation savedEvaluation = evaluationService.saveEvaluation(evaluation);

        return ResponseEntity.ok(savedEvaluation);
    }
}
