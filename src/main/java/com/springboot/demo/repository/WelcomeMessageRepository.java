package com.springboot.demo.repository;

import com.springboot.demo.dto.WelcomeMessageTemplateDto;
import com.springboot.demo.model.WelcomeMessageTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Repository for WelcomeMessageTemplate that communicates with the database
 * exclusively via Stored Procedures using Spring JDBC Template.
 */
@Repository
public class WelcomeMessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public WelcomeMessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Row mapper to map SQL result set to WelcomeMessageTemplate model with joins
    private final RowMapper<WelcomeMessageTemplate> rowMapper = (rs, rowNum) -> {
        WelcomeMessageTemplate template = new WelcomeMessageTemplate();
        template.setId(rs.getInt("id"));
        template.setSubject(rs.getString("subject"));
        template.setBrandId(rs.getInt("brand_id"));
        template.setLanguageCode(rs.getString("language_code"));
        template.setLanguageName(rs.getString("language_name"));
        template.setCountryCode(rs.getString("country_code"));
        template.setCountryName(rs.getString("country_name"));
        template.setCurrencyCode(rs.getString("currency_code"));
        template.setCurrencyName(rs.getString("currency_name"));
        template.setStatus(rs.getString("status"));
        template.setMessage(rs.getString("message"));
        template.setShowAtLogin(rs.getBoolean("show_at_login"));
        
        Timestamp createdAdded = rs.getTimestamp("created_added");
        if (createdAdded != null) {
            template.setCreatedAdded(createdAdded.getTime());
        }
        
        Timestamp lastUpdateDate = rs.getTimestamp("last_update_date");
        if (lastUpdateDate != null) {
            template.setLastUpdateDate(lastUpdateDate.getTime());
        }
        
        template.setLastUpdateBy(rs.getString("last_update_by"));
        return template;
    };

    /**
     * Call sp_create_welcome_message_template to insert a new template.
     * Returns the generated ID.
     */
    public Integer create(WelcomeMessageTemplateDto dto) {
        String sql = "CALL sp_create_welcome_message_template(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.queryForObject(sql, Integer.class,
                dto.getSubject(),
                dto.getBrandId() != null ? dto.getBrandId() : 0,
                dto.getLanguageCode(),
                dto.getCountryCode(),
                dto.getCurrencyCode(),
                dto.getStatus(),
                dto.getMessage(),
                dto.getShowAtLogin() != null && dto.getShowAtLogin() ? 1 : 0,
                dto.getLastUpdateBy()
        );
    }

    /**
     * Call sp_update_welcome_message_template to update an existing template.
     */
    public void update(Integer id, WelcomeMessageTemplateDto dto) {
        String sql = "CALL sp_update_welcome_message_template(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                id,
                dto.getSubject(),
                dto.getBrandId() != null ? dto.getBrandId() : 0,
                dto.getLanguageCode(),
                dto.getCountryCode(),
                dto.getCurrencyCode(),
                dto.getStatus(),
                dto.getMessage(),
                dto.getShowAtLogin() != null && dto.getShowAtLogin() ? 1 : 0,
                dto.getLastUpdateBy()
        );
    }

    /**
     * Call sp_delete_welcome_message_template to remove a template.
     */
    public void delete(Integer id) {
        String sql = "CALL sp_delete_welcome_message_template(?)";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Call sp_get_welcome_message_template_by_id to retrieve a specific template.
     */
    public Optional<WelcomeMessageTemplate> findById(Integer id) {
        String sql = "CALL sp_get_welcome_message_template_by_id(?)";
        List<WelcomeMessageTemplate> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.stream().findFirst();
    }

    /**
     * Call sp_search_welcome_message_templates to search with filters.
     */
    public List<WelcomeMessageTemplate> search(
            String subject,
            Integer brandId,
            String languageCode,
            String countryCode,
            String currencyCode,
            String status
    ) {
        String sql = "CALL sp_search_welcome_message_templates(?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.query(sql, rowMapper,
                subject,
                brandId != null ? brandId : 0,
                languageCode,
                countryCode,
                currencyCode,
                status
        );
    }

    /**
     * Call sp_check_welcome_message_unique_combination to verify if a combination is unique.
     */
    public boolean checkUniqueCombination(Integer id, String languageCode, String countryCode, String currencyCode) {
        String sql = "CALL sp_check_welcome_message_unique_combination(?, ?, ?, ?)";
        Boolean isUnique = jdbcTemplate.queryForObject(sql, Boolean.class, id, languageCode, countryCode, currencyCode);
        return isUnique != null && isUnique;
    }
}
