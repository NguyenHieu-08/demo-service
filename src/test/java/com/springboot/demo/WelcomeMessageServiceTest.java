package com.springboot.demo;

import com.springboot.demo.dto.WelcomeMessageTemplateDto;
import com.springboot.demo.exception.DuplicateTemplateException;
import com.springboot.demo.exception.ResourceNotFoundException;
import com.springboot.demo.model.WelcomeMessageTemplate;
import com.springboot.demo.repository.WelcomeMessageRepository;
import com.springboot.demo.service.WelcomeMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WelcomeMessageServiceTest {

    @Mock
    private WelcomeMessageRepository repository;

    @InjectMocks
    private WelcomeMessageService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() {
        WelcomeMessageTemplateDto dto = new WelcomeMessageTemplateDto();
        dto.setSubject("Welcome!");
        dto.setBrandId(1);
        dto.setLanguageCode("en-US");
        dto.setCountryCode("US");
        dto.setCurrencyCode("USD");
        dto.setStatus("Active");
        dto.setMessage("Hello");
        dto.setShowAtLogin(true);
        dto.setLastUpdateBy("admin");

        WelcomeMessageTemplate template = new WelcomeMessageTemplate();
        template.setId(1);
        template.setSubject(dto.getSubject());
        template.setLanguageCode(dto.getLanguageCode());
        template.setCreatedAdded(System.currentTimeMillis());

        when(repository.checkUniqueCombination(null, "en-US", "US", "USD")).thenReturn(true);
        when(repository.create(any(WelcomeMessageTemplateDto.class))).thenReturn(1);
        when(repository.findById(1)).thenReturn(Optional.of(template));

        WelcomeMessageTemplate result = service.create(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Welcome!", result.getSubject());
        verify(repository, times(1)).create(dto);
    }

    @Test
    void testCreate_ValidationFailure() {
        WelcomeMessageTemplateDto dto = new WelcomeMessageTemplateDto();
        // Missing required fields

        assertThrows(IllegalArgumentException.class, () -> service.create(dto));
    }

    @Test
    void testCreate_DuplicateFailure() {
        WelcomeMessageTemplateDto dto = new WelcomeMessageTemplateDto();
        dto.setSubject("Welcome!");
        dto.setBrandId(1);
        dto.setLanguageCode("en-US");
        dto.setCountryCode("US");
        dto.setCurrencyCode("USD");
        dto.setStatus("Active");
        dto.setLastUpdateBy("admin");

        // Mock checkUniqueCombination returning false
        when(repository.checkUniqueCombination(null, "en-US", "US", "USD")).thenReturn(false);

        DuplicateTemplateException exception = assertThrows(DuplicateTemplateException.class, () -> service.create(dto));
        assertEquals("The combination of Country - Currency - Language must be unique.", exception.getMessage());
    }

    @Test
    void testCreate_XssPrevention() {
        WelcomeMessageTemplateDto dto = new WelcomeMessageTemplateDto();
        dto.setSubject("<script>alert('hack')</script>");
        dto.setBrandId(1);
        dto.setLanguageCode("en-US");
        dto.setCountryCode("US");
        dto.setCurrencyCode("USD");
        dto.setStatus("Active");
        dto.setLastUpdateBy("admin");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertTrue(exception.getMessage().contains("Potential XSS payload detected"));
    }

    @Test
    void testUpdate_NotFound() {
        WelcomeMessageTemplateDto dto = new WelcomeMessageTemplateDto();
        dto.setSubject("Welcome!");
        dto.setBrandId(1);
        dto.setLanguageCode("en-US");
        dto.setCountryCode("US");
        dto.setCurrencyCode("USD");
        dto.setStatus("Active");
        dto.setLastUpdateBy("admin");

        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99, dto));
    }

    @Test
    void testSearch_Success() {
        WelcomeMessageTemplate template = new WelcomeMessageTemplate();
        template.setId(1);
        template.setSubject("Welcome!");

        when(repository.search("Welcome", 1, "en", "US", "USD", "Active"))
                .thenReturn(Collections.singletonList(template));

        List<WelcomeMessageTemplate> results = service.search("Welcome", 1, "en", "US", "USD", "Active");

        assertEquals(1, results.size());
        assertEquals("Welcome!", results.get(0).getSubject());
    }

    @Test
    void testCheckUniqueCombination() {
        when(repository.checkUniqueCombination(1, "en", "US", "USD")).thenReturn(true);
        boolean isUnique = service.checkUniqueCombination(1, "en", "US", "USD");
        assertTrue(isUnique);
        verify(repository, times(1)).checkUniqueCombination(1, "en", "US", "USD");
    }
}
