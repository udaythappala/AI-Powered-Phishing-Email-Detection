package com.soc.phishing.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class MLService {

    private final RestClient restClient;

    public MLService() {

        this.restClient =
                RestClient.builder()
                        .baseUrl("http://localhost:5000")
                        .build();
    }

    public Map<String, Object> predict(String email) {

        Map<String, String> request =
                Map.of("email", email);

        return restClient.post()
                .uri("/predict")
                .body(request)
                .retrieve()
                .body(Map.class);
    }
}