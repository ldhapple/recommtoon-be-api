package com.recommtoon.recommtoonapi.webtoon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchWebtoonDto {

    private String titleId;
    private String imgSrc;
}
