package com.recommtoon.recommtoonapi.webtoon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendsWebtoonDto {

    private String titleId;
    private String imgSrc;
    private double avgRating;
}
