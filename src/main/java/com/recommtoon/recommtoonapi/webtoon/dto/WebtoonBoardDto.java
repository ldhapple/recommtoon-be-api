package com.recommtoon.recommtoonapi.webtoon.dto;

import com.recommtoon.recommtoonapi.webtoon.entity.Genre;
import java.util.Set;
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
    private Set<String> genres;
}
