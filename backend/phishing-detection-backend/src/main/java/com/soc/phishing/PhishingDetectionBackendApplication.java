package com.soc.phishing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhishingDetectionBackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                PhishingDetectionBackendApplication.class,
                args);
    }
}