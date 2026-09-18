package com.soc.phishing.service;

import com.soc.phishing.dto.EmailAnalysisRequest;
import com.soc.phishing.dto.EmailAnalysisResponse;
import com.soc.phishing.model.EmailAnalysis;
import com.soc.phishing.repository.EmailAnalysisRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmailAnalysisService {

    private final EmailAnalysisRepository repository;
    private final MLService mlService;

    public EmailAnalysisService(
            EmailAnalysisRepository repository,
            MLService mlService) {

        this.repository = repository;
        this.mlService = mlService;
    }

    // Analyze email using Python ML service
    public EmailAnalysisResponse analyzeEmail(
            EmailAnalysisRequest request) {

        // Combine subject and email body
        String emailText =
                request.getSubject() + " "
                        + request.getEmailBody();

        // Send email to Python ML service
        Map<String, Object> mlResult =
                mlService.predict(emailText);

        String prediction =
                (String) mlResult.get("prediction");

        Double phishingScore =
                ((Number) mlResult.get("phishingScore"))
                        .doubleValue();

        // Generate reasons
        String reasons =
                generateReasons(request);

        // Calculate risk level
        String riskLevel =
                calculateRiskLevel(phishingScore);

        // Analyze URL
        String urlAnalysis =
                analyzeUrl(request.getUrl());

        // Recommended action
        String recommendedAction;

        if ("PHISHING".equalsIgnoreCase(prediction)) {

            recommendedAction =
                    "Do not click links or provide credentials. "
                    + "Report the email.";

        } else {

            recommendedAction =
                    "Email appears low risk, but verify the sender "
                    + "before taking action.";
        }

        // Create entity
        EmailAnalysis analysis =
                new EmailAnalysis();

        analysis.setSender(request.getSender());
        analysis.setSubject(request.getSubject());
        analysis.setEmailBody(request.getEmailBody());
        analysis.setUrl(request.getUrl());
        analysis.setPrediction(prediction);
        analysis.setPhishingScore(phishingScore);
        analysis.setReasons(reasons);
        analysis.setRecommendedAction(recommendedAction);
        analysis.setRiskLevel(riskLevel);
        analysis.setUrlAnalysis(urlAnalysis);

        // Save to PostgreSQL
        EmailAnalysis savedAnalysis =
                repository.save(analysis);

        // Return response
        return new EmailAnalysisResponse(
                savedAnalysis.getId(),
                savedAnalysis.getPrediction(),
                savedAnalysis.getPhishingScore(),
                savedAnalysis.getReasons(),
                savedAnalysis.getRecommendedAction(),
                savedAnalysis.getRiskLevel(),
                savedAnalysis.getUrlAnalysis()
        );
    }

    // Generate suspicious indicators
    private String generateReasons(
            EmailAnalysisRequest request) {

        StringBuilder reasons =
                new StringBuilder();

        String subject =
                request.getSubject().toLowerCase();

        String body =
                request.getEmailBody().toLowerCase();

        if (subject.contains("urgent")) {

            reasons.append(
                    "Urgent language detected; ");
        }

        if (body.contains("click here")) {

            reasons.append(
                    "Suspicious click request detected; ");
        }

        if (body.contains("password")) {

            reasons.append(
                    "Password-related request detected; ");
        }

        if (body.contains("verify your account")) {

            reasons.append(
                    "Account verification request detected; ");
        }

        if (body.contains("payment")) {

            reasons.append(
                    "Payment-related request detected; ");
        }

        if (body.contains("bank")) {

            reasons.append(
                    "Banking-related content detected; ");
        }

        if (request.getUrl() != null
                && !request.getUrl().isBlank()) {

            reasons.append(
                    "URL present in email; ");
        }

        if (reasons.length() == 0) {

            reasons.append(
                    "No major suspicious indicators detected.");
        }

        return reasons.toString();
    }

    // Calculate risk level
    private String calculateRiskLevel(
            Double phishingScore) {

        if (phishingScore >= 70) {

            return "HIGH";

        } else if (phishingScore >= 40) {

            return "MEDIUM";

        } else {

            return "LOW";
        }
    }

    // Analyze suspicious URL indicators
    private String analyzeUrl(String url) {

        if (url == null || url.isBlank()) {

            return "No URL provided";
        }

        String lowerUrl =
                url.toLowerCase();

        StringBuilder analysis =
                new StringBuilder();

        if (lowerUrl.startsWith("http://")) {

            analysis.append(
                    "URL uses HTTP instead of HTTPS; ");
        }

        if (lowerUrl.contains("@")) {

            analysis.append(
                    "URL contains @ symbol; ");
        }

        if (lowerUrl.contains("login")) {

            analysis.append(
                    "URL contains login keyword; ");
        }

        if (lowerUrl.contains("verify")) {

            analysis.append(
                    "URL contains verify keyword; ");
        }

        if (lowerUrl.contains("account")) {

            analysis.append(
                    "URL contains account keyword; ");
        }

        if (lowerUrl.contains("password")) {

            analysis.append(
                    "URL contains password keyword; ");
        }

        if (analysis.length() == 0) {

            return "No obvious URL indicators detected";
        }

        return analysis.toString();
    }

    // Get all analyses
    public List<EmailAnalysis> getAllAnalyses() {

        return repository.findAll();
    }

    // Get analysis by ID
    public EmailAnalysis getAnalysisById(
            Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Analysis not found with id: "
                                        + id));
    }

    // Delete analysis
    public void deleteAnalysis(Long id) {

        repository.deleteById(id);
    }
}