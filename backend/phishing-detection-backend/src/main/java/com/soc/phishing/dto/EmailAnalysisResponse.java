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

    private String riskLevel;

    private String reasons;

    private String urlAnalysis;

    private String senderAnalysis;

    private String suspiciousIndicators;

    private String mitreAttack;

    private String recommendedAction;
}