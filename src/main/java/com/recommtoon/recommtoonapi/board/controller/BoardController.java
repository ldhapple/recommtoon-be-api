package com.recommtoon.recommtoonapi.board.controller;

import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiError;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import com.recommtoon.recommtoonapi.webtoon.dto.WebtoonBoardDto;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.service.WebtoonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "웹툰별 의견 공유 게시판 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final WebtoonService webtoonService;

    @Operation(summary = "웹툰 정보 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹툰 정보 조회 성공", content = @Content(schema = @Schema(implementation = WebtoonBoardDto.class))),
            @ApiResponse(responseCode = "400", description = "웹툰이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{titleId}")
    public ApiSuccess<WebtoonBoardDto> getWebtoonDetalis(@PathVariable String titleId) {
        WebtoonBoardDto webtoonDetails = webtoonService.getWebtoonByTitleId(titleId);

        return ApiUtil.success(webtoonDetails);
    }
}
