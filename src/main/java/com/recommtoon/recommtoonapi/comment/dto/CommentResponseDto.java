package com.recommtoon.recommtoonapi.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String nickName;
    private String writeTime;
    private String content;
    private Long likeCount;
}
