package com.soc.phishing.service;

import com.soc.phishing.dto.EmailAnalysisRequest;
import com.soc.phishing.dto.EmailAnalysisResponse;
import com.soc.phishing.exception.ResourceNotFoundException;
import com.soc.phishing.model.EmailAnalysis;
import com.soc.phishing.repository.EmailAnalysisRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // =========================================================
    // MAIN EMAIL ANALYSIS
    // =========================================================

    public EmailAnalysisResponse analyzeEmail(
            EmailAnalysisRequest request) {

        String emailText =
                request.getSubject() + " "
                        + request.getEmailBody();

        // Python ML prediction
        Map<String, Object> mlResult =
                mlService.predict(emailText);

        String prediction =
                (String) mlResult.get("prediction");

        Double phishingScore =
                ((Number) mlResult.get("phishingScore"))
                        .doubleValue();

        // Individual analysis modules
        String reasons =
                generateReasons(request);

        String riskLevel =
                calculateRiskLevel(phishingScore);

        String urlAnalysis =
                analyzeUrl(request.getUrl());

        String senderAnalysis =
                analyzeSender(request.getSender());

        String suspiciousIndicators =
                extractIndicators(request);

        String mitreAttack =
                mapMitreAttack(request, prediction);

        String recommendedAction =
                generateRecommendation(
                        prediction,
                        riskLevel);

        // Create database entity
        EmailAnalysis analysis =
                new EmailAnalysis();

        analysis.setSender(request.getSender());
        analysis.setSubject(request.getSubject());
        analysis.setEmailBody(request.getEmailBody());
        analysis.setUrl(request.getUrl());

        analysis.setPrediction(prediction);
        analysis.setPhishingScore(phishingScore);
        analysis.setRiskLevel(riskLevel);

        analysis.setReasons(reasons);
        analysis.setUrlAnalysis(urlAnalysis);
        analysis.setSenderAnalysis(senderAnalysis);
        analysis.setSuspiciousIndicators(
                suspiciousIndicators);

        analysis.setMitreAttack(mitreAttack);

        analysis.setRecommendedAction(
                recommendedAction);

        // Save in PostgreSQL
        EmailAnalysis savedAnalysis =
                repository.save(analysis);

        return convertToResponse(savedAnalysis);
    }

    // =========================================================
    // SUSPICIOUS REASONS
    // =========================================================

    private String generateReasons(
            EmailAnalysisRequest request) {

        List<String> reasons =
                new ArrayList<>();

        String subject =
                request.getSubject().toLowerCase();

        String body =
                request.getEmailBody().toLowerCase();

        if (subject.contains("urgent")) {
            reasons.add(
                    "Urgent language detected");
        }

        if (subject.contains("alert")) {
            reasons.add(
                    "Security alert language detected");
        }

        if (body.contains("click here")) {
            reasons.add(
                    "Suspicious click request detected");
        }

        if (body.contains("password")) {
            reasons.add(
                    "Password-related request detected");
        }

        if (body.contains("verify your account")) {
            reasons.add(
                    "Account verification request detected");
        }

        if (body.contains("verify")) {
            reasons.add(
                    "Verification request detected");
        }

        if (body.contains("payment")) {
            reasons.add(
                    "Payment-related content detected");
        }

        if (body.contains("bank")) {
            reasons.add(
                    "Banking-related content detected");
        }

        if (body.contains("credit card")) {
            reasons.add(
                    "Credit card information request detected");
        }

        if (body.contains("login")) {
            reasons.add(
                    "Login-related request detected");
        }

        if (request.getUrl() != null
                && !request.getUrl().isBlank()) {

            reasons.add(
                    "URL present in email");
        }

        if (reasons.isEmpty()) {
            return "No major suspicious indicators detected.";
        }

        return String.join("; ", reasons);
    }

    // =========================================================
    // RISK LEVEL
    // =========================================================

    private String calculateRiskLevel(
            Double phishingScore) {

        if (phishingScore >= 70) {
            return "HIGH";
        }

        if (phishingScore >= 40) {
            return "MEDIUM";
        }

        return "LOW";
    }

    // =========================================================
    // URL ANALYSIS
    // =========================================================

    private String analyzeUrl(String url) {

        if (url == null || url.isBlank()) {
            return "No URL provided";
        }

        String lowerUrl =
                url.toLowerCase();

        List<String> indicators =
                new ArrayList<>();

        if (lowerUrl.startsWith("http://")) {
            indicators.add(
                    "URL uses HTTP instead of HTTPS");
        }

        if (lowerUrl.contains("@")) {
            indicators.add(
                    "URL contains @ symbol");
        }

        if (lowerUrl.contains("login")) {
            indicators.add(
                    "URL contains login keyword");
        }

        if (lowerUrl.contains("verify")) {
            indicators.add(
                    "URL contains verify keyword");
        }

        if (lowerUrl.contains("account")) {
            indicators.add(
                    "URL contains account keyword");
        }

        if (lowerUrl.contains("password")) {
            indicators.add(
                    "URL contains password keyword");
        }

        if (lowerUrl.contains("secure")) {
            indicators.add(
                    "URL contains secure keyword");
        }

        if (indicators.isEmpty()) {
            return "No obvious URL indicators detected";
        }

        return String.join("; ", indicators);
    }

    // =========================================================
    // SENDER / DOMAIN ANALYSIS
    // =========================================================

    private String analyzeSender(String sender) {

        if (sender == null || sender.isBlank()) {
            return "No sender information provided";
        }

        String lowerSender =
                sender.toLowerCase();

        List<String> indicators =
                new ArrayList<>();

        if (lowerSender.contains("gmail.com")
                || lowerSender.contains("yahoo.com")
                || lowerSender.contains("outlook.com")
                || lowerSender.contains("hotmail.com")) {

            indicators.add(
                    "Sender uses a public email provider");
        }

        if (lowerSender.contains("support")
                || lowerSender.contains("security")
                || lowerSender.contains("admin")
                || lowerSender.contains("service")) {

            indicators.add(
                    "Sender uses a high-trust role-based name");
        }

        if (lowerSender.contains("fake")
                || lowerSender.contains("scam")
                || lowerSender.contains("phish")) {

            indicators.add(
                    "Sender domain contains suspicious keywords");
        }

        if (indicators.isEmpty()) {
            return "No obvious sender indicators detected";
        }

        return String.join("; ", indicators);
    }

    // =========================================================
    // SUSPICIOUS INDICATORS
    // =========================================================

    private String extractIndicators(
            EmailAnalysisRequest request) {

        List<String> indicators =
                new ArrayList<>();

        String subject =
                request.getSubject().toLowerCase();

        String body =
                request.getEmailBody().toLowerCase();

        if (subject.contains("urgent")) {
            indicators.add("URGENT_LANGUAGE");
        }

        if (body.contains("click here")) {
            indicators.add("SUSPICIOUS_LINK_REQUEST");
        }

        if (body.contains("password")) {
            indicators.add("PASSWORD_REQUEST");
        }

        if (body.contains("verify")) {
            indicators.add("VERIFICATION_REQUEST");
        }

        if (body.contains("payment")) {
            indicators.add("PAYMENT_REQUEST");
        }

        if (body.contains("bank")) {
            indicators.add("BANKING_CONTENT");
        }

        if (body.contains("credit card")) {
            indicators.add("CREDIT_CARD_REQUEST");
        }

        if (body.contains("login")) {
            indicators.add("LOGIN_REQUEST");
        }

        if (request.getUrl() != null
                && !request.getUrl().isBlank()) {

            indicators.add("URL_PRESENT");
        }

        if (indicators.isEmpty()) {
            return "NONE";
        }

        return String.join(", ", indicators);
    }

    // =========================================================
    // MITRE ATT&CK MAPPING
    // =========================================================

    private String mapMitreAttack(
            EmailAnalysisRequest request,
            String prediction) {

        if (!"PHISHING".equalsIgnoreCase(prediction)) {
            return "No phishing technique mapped";
        }

        List<String> techniques =
                new ArrayList<>();

        String body =
                request.getEmailBody().toLowerCase();

        String subject =
                request.getSubject().toLowerCase();

        if (body.contains("click")
                || body.contains("link")
                || body.contains("verify")) {

            techniques.add(
                    "T1566 - Phishing");
        }

        if (body.contains("password")
                || body.contains("login")
                || body.contains("credential")) {

            techniques.add(
                    "T1056.002 - Input Capture: GUI Input Capture");
        }

        if (subject.contains("urgent")
                || body.contains("immediately")) {

            techniques.add(
                    "Social engineering / urgency indicator");
        }

        if (techniques.isEmpty()) {
            return "T1566 - Phishing";
        }

        return String.join("; ", techniques);
    }

    // =========================================================
    // SOC RECOMMENDATION
    // =========================================================

    private String generateRecommendation(
            String prediction,
            String riskLevel) {

        if ("PHISHING".equalsIgnoreCase(prediction)) {

            if ("HIGH".equalsIgnoreCase(riskLevel)) {

                return "HIGH RISK: Do not click links, "
                        + "do not provide credentials, "
                        + "quarantine the email, "
                        + "and report the incident to the SOC team.";
            }

            if ("MEDIUM".equalsIgnoreCase(riskLevel)) {

                return "MEDIUM RISK: Avoid interacting with "
                        + "links or attachments and verify the "
                        + "sender through a trusted channel.";
            }

            return "Potential phishing detected. "
                    + "Verify the sender before taking action.";
        }

        return "No strong phishing indicators detected. "
                + "Continue normal security verification.";
    }

    // =========================================================
    // RESPONSE CONVERSION
    // =========================================================

    private EmailAnalysisResponse convertToResponse(
            EmailAnalysis analysis) {

        return new EmailAnalysisResponse(

                analysis.getId(),

                analysis.getPrediction(),

                analysis.getPhishingScore(),

                analysis.getRiskLevel(),

                analysis.getReasons(),

                analysis.getUrlAnalysis(),

                analysis.getSenderAnalysis(),

                analysis.getSuspiciousIndicators(),

                analysis.getMitreAttack(),

                analysis.getRecommendedAction()
        );
    }

    // =========================================================
    // GET HISTORY
    // =========================================================

    public List<EmailAnalysis> getAllAnalyses() {

        return repository.findAll();
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    public EmailAnalysis getAnalysisById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Analysis not found with id: "
                                        + id));
    }

    // =========================================================
    // DELETE
    // =========================================================

    public void deleteAnalysis(Long id) {

        if (!repository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "Analysis not found with id: " + id);
        }

        repository.deleteById(id);
    }
}