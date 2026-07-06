package com.springboot.demo.dto;

/**
 * Data Transfer Object representing the request body to create or update a Welcome Message Template.
 */
public class WelcomeMessageTemplateDto {
    private String subject;
    private Integer brandId;
    private String languageCode;
    private String countryCode;
    private String currencyCode;
    private String status;
    private String message;
    private Boolean showAtLogin;
    private String lastUpdateBy;

    // Getters and Setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getShowAtLogin() {
        return showAtLogin;
    }

    public void setShowAtLogin(Boolean showAtLogin) {
        this.showAtLogin = showAtLogin;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
