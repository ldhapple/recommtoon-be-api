package com.recommtoon.recommtoonapi.webtoon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class WebtoonBoardDto {

    private String title;
    private String link;
    private String author;
    private String story;
    private String imgSrc;
}
