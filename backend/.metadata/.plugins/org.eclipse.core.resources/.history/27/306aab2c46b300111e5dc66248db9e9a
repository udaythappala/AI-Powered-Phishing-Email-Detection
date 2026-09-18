package com.soc.phishing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailAnalysisRequest {

    @NotBlank(message = "Sender email is required")
    @Email(message = "Invalid sender email")
    private String sender;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Email body is required")
    private String emailBody;

    private String url;
}