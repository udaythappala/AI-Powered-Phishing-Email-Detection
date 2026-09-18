package com.soc.phishing.repository;

import com.soc.phishing.model.EmailAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAnalysisRepository
        extends JpaRepository<EmailAnalysis, Long> {
}