package com.recommtoon.recommtoonapi.comment.controller;

import com.recommtoon.recommtoonapi.comment.dto.CommentRequestDto;
import com.recommtoon.recommtoonapi.comment.dto.CommentResponseDto;
import com.recommtoon.recommtoonapi.comment.entity.Comments;
import com.recommtoon.recommtoonapi.comment.service.CommentsService;
import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiError;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiSuccess;
import com.recommtoon.recommtoonapi.webtoon.dto.WebtoonBoardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "웹툰별 댓글관리 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentsService commentsService;

    @Operation(summary = "댓글 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "해당 웹툰이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{titleId}")
    public ApiSuccess<List<CommentResponseDto>> getComments(@PathVariable String titleId) {
        List<CommentResponseDto> comments = commentsService.getCommentsByTitleId(titleId);

        return ApiUtil.success(comments);
    }

    @Operation(summary = "웹툰 정보 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹툰 정보 조회 성공", content = @Content(schema = @Schema(implementation = CommentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "해당 웹툰이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/new/{titleId}")
    public ApiSuccess<CommentResponseDto> createComment(@PathVariable String titleId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            Authentication authentication) {
        String username = authentication.getName();
        CommentResponseDto createdComment = commentsService.createComment(commentRequestDto, titleId, username);

        return ApiUtil.success(createdComment);
    }

    @Operation(summary = "웹툰 정보 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹툰 정보 조회 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "해당 댓글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/like/{commentId}")
    public ApiSuccess<String> thumbsUp(@PathVariable Long commentId) {
        commentsService.incrementLikeCount(commentId);

        return ApiUtil.success("좋아요 성공");
    }
}
