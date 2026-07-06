package com.springboot.demo.model;

/**
 * Domain model representing a Welcome Message Template matching the updated database schema.
 * All date-related fields return a Long value (timestamp) for frontend compatibility.
 */
public class WelcomeMessageTemplate {
    private Integer id;
    private String subject;
    private Integer brandId;
    private String languageCode;
    private String languageName;
    private String countryCode;
    private String countryName;
    private String currencyCode;
    private String currencyName;
    private String status;
    private String message;
    private Boolean showAtLogin;
    private Long createdAdded;
    private String lastUpdateBy;
    private Long lastUpdateDate;

    // Default Constructor
    public WelcomeMessageTemplate() {}

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
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

    public Long getCreatedAdded() {
        return createdAdded;
    }

    public void setCreatedAdded(Long createdAdded) {
        this.createdAdded = createdAdded;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
