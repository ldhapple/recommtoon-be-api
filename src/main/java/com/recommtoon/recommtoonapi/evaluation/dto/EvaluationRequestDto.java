package com.recommtoon.recommtoonapi.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EvaluationRequestDto {

    private String username;
    private Long webtoonId;
    private Double rating;
}
