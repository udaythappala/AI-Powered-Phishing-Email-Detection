package com.soc.phishing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAnalysisResponse {

    private Long id;

    private String prediction;

    private Double phishingScore;

    private String reasons;

    private String recommendedAction;
}