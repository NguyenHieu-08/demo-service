package com.springboot.demo.controller;

import com.springboot.demo.dto.WelcomeMessageTemplateDto;
import com.springboot.demo.model.WelcomeMessageTemplate;
import com.springboot.demo.service.WelcomeMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing endpoints for Welcome Message Template CRUD actions.
 */
@RestController
@RequestMapping("/api/welcome-messages")
@CrossOrigin(origins = "*")
public class WelcomeMessageController {

    private final WelcomeMessageService service;

    public WelcomeMessageController(WelcomeMessageService service) {
        this.service = service;
    }

    /**
     * Create a new automated welcome message.
     */
    @PostMapping
    public ResponseEntity<WelcomeMessageTemplate> create(@RequestBody WelcomeMessageTemplateDto dto) {
        WelcomeMessageTemplate created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing welcome message template.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WelcomeMessageTemplate> update(
            @PathVariable("id") Integer id,
            @RequestBody WelcomeMessageTemplateDto dto
    ) {
        WelcomeMessageTemplate updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Get a specific welcome message template by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WelcomeMessageTemplate> getById(@PathVariable("id") Integer id) {
        WelcomeMessageTemplate template = service.getById(id);
        return ResponseEntity.ok(template);
    }

    /**
     * Delete a welcome message template.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search and filter welcome message templates.
     */
    @GetMapping
    public ResponseEntity<List<WelcomeMessageTemplate>> search(
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "brandId", required = false) Integer brandId,
            @RequestParam(value = "languageCode", required = false) String languageCode,
            @RequestParam(value = "countryCode", required = false) String countryCode,
            @RequestParam(value = "currencyCode", required = false) String currencyCode,
            @RequestParam(value = "status", required = false) String status
    ) {
        List<WelcomeMessageTemplate> templates = service.search(subject, brandId, languageCode, countryCode, currencyCode, status);
        return ResponseEntity.ok(templates);
    }

    /**
     * Check if a combination of Language, Country, and Currency is unique.
     */
    @GetMapping("/check-uniqueness")
    public ResponseEntity<java.util.Map<String, Boolean>> checkUniqueness(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam("languageCode") String languageCode,
            @RequestParam("countryCode") String countryCode,
            @RequestParam("currencyCode") String currencyCode
    ) {
        boolean isUnique = service.checkUniqueCombination(id, languageCode, countryCode, currencyCode);
        java.util.Map<String, Boolean> response = new java.util.HashMap<>();
        response.put("isUnique", isUnique);
        return ResponseEntity.ok(response);
    }
}
