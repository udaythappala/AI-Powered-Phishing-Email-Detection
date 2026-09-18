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
    public EmailAnalysisResponse analyzeEmail(EmailAnalysisRequest request) {

        // Combine subject + body for ML prediction
        String emailText =
                request.getSubject() + " " + request.getEmailBody();

        // Send email to Python ML service
        Map<String, Object> mlResult = mlService.predict(emailText);

        String prediction =
                (String) mlResult.get("prediction");

        Double phishingScore =
                ((Number) mlResult.get("phishingScore")).doubleValue();

        // Generate explanation
        String reasons = generateReasons(
                request,
                prediction,
                phishingScore
        );

        // Recommended action
        String recommendedAction;

        if ("PHISHING".equalsIgnoreCase(prediction)) {

            recommendedAction =
                    "Do not click links or provide credentials. Report the email.";

        } else {

            recommendedAction =
                    "Email appears low risk, but verify the sender before taking action.";
        }

        // Create database entity
        EmailAnalysis analysis = new EmailAnalysis();

        analysis.setSender(request.getSender());
        analysis.setSubject(request.getSubject());
        analysis.setEmailBody(request.getEmailBody());
        analysis.setUrl(request.getUrl());
        analysis.setPrediction(prediction);
        analysis.setPhishingScore(phishingScore);
        analysis.setReasons(reasons);
        analysis.setRecommendedAction(recommendedAction);

        // Save to PostgreSQL
        EmailAnalysis savedAnalysis =
                repository.save(analysis);

        // Return response
        return new EmailAnalysisResponse(
                savedAnalysis.getId(),
                savedAnalysis.getPrediction(),
                savedAnalysis.getPhishingScore(),
                savedAnalysis.getReasons(),
                savedAnalysis.getRecommendedAction()
        );
    }

    // Generate simple analyst-friendly explanations
    private String generateReasons(
            EmailAnalysisRequest request,
            String prediction,
            Double score) {

        StringBuilder reasons = new StringBuilder();

        String subject = request.getSubject().toLowerCase();
        String body = request.getEmailBody().toLowerCase();

        if (subject.contains("urgent")) {
            reasons.append("Urgent language detected; ");
        }

        if (body.contains("click here")) {
            reasons.append("Suspicious click request detected; ");
        }

        if (body.contains("password")) {
            reasons.append("Password-related request detected; ");
        }

        if (body.contains("verify your account")) {
            reasons.append("Account verification request detected; ");
        }

        if (body.contains("payment")) {
            reasons.append("Payment-related request detected; ");
        }

        if (request.getUrl() != null &&
                !request.getUrl().isBlank()) {

            reasons.append("URL present in email; ");
        }

        if (reasons.length() == 0) {

            if ("PHISHING".equalsIgnoreCase(prediction)) {
                reasons.append(
                        "ML model identified suspicious email patterns."
                );
            } else {
                reasons.append(
                        "No major suspicious indicators detected."
                );
            }
        }

        return reasons.toString();
    }

    // Get all analyzed emails
    public List<EmailAnalysis> getAllAnalyses() {

        return repository.findAll();
    }

    // Get analysis by ID
    public EmailAnalysis getAnalysisById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Analysis not found with id: " + id
                        ));
    }

    // Delete analysis
    public void deleteAnalysis(Long id) {

        repository.deleteById(id);
    }
}