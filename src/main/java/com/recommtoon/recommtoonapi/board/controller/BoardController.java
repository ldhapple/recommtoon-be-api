package com.recommtoon.recommtoonapi.board.controller;

import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import com.recommtoon.recommtoonapi.webtoon.dto.WebtoonBoardDto;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.service.WebtoonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final WebtoonService webtoonService;

    @GetMapping("/{titleId}")
    public ApiSuccess<WebtoonBoardDto> getWebtoonDetalis(@PathVariable String titleId) {
        WebtoonBoardDto webtoonDetails = webtoonService.getWebtoonByTitleId(titleId);

        return ApiUtil.success(webtoonDetails);
    }
}
