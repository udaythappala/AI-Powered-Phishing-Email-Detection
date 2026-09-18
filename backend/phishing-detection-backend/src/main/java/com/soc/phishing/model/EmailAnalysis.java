package com.soc.phishing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String emailBody;

    private String url;

    private String prediction;

    private Double phishingScore;

    private String riskLevel;

    @Column(columnDefinition = "TEXT")
    private String reasons;

    @Column(columnDefinition = "TEXT")
    private String urlAnalysis;

    @Column(columnDefinition = "TEXT")
    private String senderAnalysis;

    @Column(columnDefinition = "TEXT")
    private String suspiciousIndicators;

    @Column(columnDefinition = "TEXT")
    private String mitreAttack;

    @Column(columnDefinition = "TEXT")
    private String recommendedAction;
}