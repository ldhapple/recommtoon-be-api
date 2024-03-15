package com.recommtoon.recommtoonapi.webtoon.controller;

import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiError;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import com.recommtoon.recommtoonapi.webtoon.dto.SearchWebtoonDto;
import com.recommtoon.recommtoonapi.webtoon.dto.WebtoonBoardDto;
import com.recommtoon.recommtoonapi.webtoon.service.WebtoonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "웹툰 검색 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webtoons")
public class WebtoonController {

    private final WebtoonService webtoonService;

    @Operation(summary = "전체 웹툰 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹툰 검색 성공", content = @Content(schema = @Schema(implementation = SearchWebtoonDto.class))),
            @ApiResponse(responseCode = "400", description = "올바른 값이 아닙니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ApiSuccess<?> searchResultWebtoons(@PageableDefault(size = 50) Pageable pageable,
                                              @RequestParam(required = false, defaultValue = "") String searchParam) {

        return ApiUtil.success(webtoonService.searchWebtoon(pageable, searchParam));
    }

    @GetMapping("/friends-webtoon")
    public ApiSuccess<?> searchFriendsWebtoons(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer age,
            Pageable pageable
    ) {
        return ApiUtil.success(webtoonService.searchFriendsWebtoon(name, gender, age, pageable));
    }
}
