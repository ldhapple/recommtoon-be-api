package com.recommtoon.recommtoonapi.comment.controller;

import com.recommtoon.recommtoonapi.comment.dto.CommentRequestDto;
import com.recommtoon.recommtoonapi.comment.dto.CommentResponseDto;
import com.recommtoon.recommtoonapi.comment.entity.Comments;
import com.recommtoon.recommtoonapi.comment.service.CommentsService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentsService commentsService;

    @GetMapping("/{webtoonId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long webtoonId) {
        List<CommentResponseDto> comments = commentsService.getCommentsByWebtoonId(webtoonId);

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/new/{webtoonId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long webtoonId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            Authentication authentication) {
        String username = authentication.getName();
        CommentResponseDto createdComment = commentsService.createComment(commentRequestDto, webtoonId, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PostMapping("/like/{commentId}")
    public ResponseEntity<Void> thumbsUp(@PathVariable Long commentId) {
        commentsService.incrementLikeCount(commentId);

        return ResponseEntity.ok().build();
    }
}
