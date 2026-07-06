package com.springboot.demo.service;

import com.springboot.demo.dto.WelcomeMessageTemplateDto;
import com.springboot.demo.exception.DuplicateTemplateException;
import com.springboot.demo.exception.ResourceNotFoundException;
import com.springboot.demo.model.WelcomeMessageTemplate;
import com.springboot.demo.repository.WelcomeMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that coordinates database transactions, validation, and XSS protection.
 */
@Service
public class WelcomeMessageService {

    private final WelcomeMessageRepository repository;

    public WelcomeMessageService(WelcomeMessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a new welcome message template.
     */
    public WelcomeMessageTemplate create(WelcomeMessageTemplateDto dto) {
        validateDto(dto);
        if (!checkUniqueCombination(null, dto.getLanguageCode(), dto.getCountryCode(), dto.getCurrencyCode())) {
            throw new DuplicateTemplateException("The combination of Country - Currency - Language must be unique.");
        }
        Integer id = repository.create(dto);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Welcome message template not found after creation."));
    }

    /**
     * Update an existing welcome message template.
     */
    public WelcomeMessageTemplate update(Integer id, WelcomeMessageTemplateDto dto) {
        // Ensure template exists first
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Welcome message template with ID " + id + " not found."));
        
        validateDto(dto);
        if (!checkUniqueCombination(id, dto.getLanguageCode(), dto.getCountryCode(), dto.getCurrencyCode())) {
            throw new DuplicateTemplateException("The combination of Country - Currency - Language must be unique.");
        }
        repository.update(id, dto);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Welcome message template not found after update."));
    }

    /**
     * Get template by ID.
     */
    public WelcomeMessageTemplate getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Welcome message template with ID " + id + " not found."));
    }

    /**
     * Delete template by ID.
     */
    public void delete(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Welcome message template with ID " + id + " not found."));
        repository.delete(id);
    }

    /**
     * Search templates with optional filters.
     */
    public List<WelcomeMessageTemplate> search(
            String subject,
            Integer brandId,
            String languageCode,
            String countryCode,
            String currencyCode,
            String status
    ) {
        return repository.search(subject, brandId, languageCode, countryCode, currencyCode, status);
    }

    /**
     * Check if a combination of language, country, and currency is unique.
     */
    public boolean checkUniqueCombination(Integer id, String languageCode, String countryCode, String currencyCode) {
        return repository.checkUniqueCombination(id, languageCode, countryCode, currencyCode);
    }

    /**
     * Basic server-side validation and XSS protection.
     */
    private void validateDto(WelcomeMessageTemplateDto dto) {
        if (dto.getLanguageCode() == null || dto.getLanguageCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Language Code is required.");
        }
        if (dto.getBrandId() == null) {
            throw new IllegalArgumentException("Brand ID is required.");
        }
        if (dto.getCountryCode() == null || dto.getCountryCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Country Code is required.");
        }
        if (dto.getCurrencyCode() == null || dto.getCurrencyCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Currency Code is required.");
        }
        if (dto.getStatus() == null || dto.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required.");
        }
        if (dto.getSubject() == null || dto.getSubject().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (dto.getLastUpdateBy() == null || dto.getLastUpdateBy().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Update By is required.");
        }

        // XSS Prevention Validation
        validateXss("subject", dto.getSubject());
        validateXss("message", dto.getMessage());
    }

    /**
     * Validates field content to prevent cross-site scripting (XSS) attacks.
     */
    private void validateXss(String fieldName, String value) {
        if (value == null) return;
        String lowercaseValue = value.toLowerCase();
        if (lowercaseValue.contains("<script") || 
            lowercaseValue.contains("javascript:") || 
            lowercaseValue.contains("onload=") || 
            lowercaseValue.contains("onerror=") || 
            lowercaseValue.contains("onclick=") || 
            lowercaseValue.contains("onmouseover=") ||
            lowercaseValue.contains("<iframe") ||
            lowercaseValue.contains("<object") ||
            lowercaseValue.contains("<embed") ||
            lowercaseValue.contains("<svg") ||
            lowercaseValue.contains("alert(")) {
            throw new IllegalArgumentException("Potential XSS payload detected in field: " + fieldName);
        }
    }
}
