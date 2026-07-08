-- Stored Procedures for welcome_message_template CRUD operations with joins

DELIMITER //

-- 1. Create Stored Procedure
DROP PROCEDURE IF EXISTS `sp_create_welcome_message_template` //
CREATE PROCEDURE `sp_create_welcome_message_template`(
    IN `p_subject` VARCHAR(255),
    IN `p_brand_id` INT,
    IN `p_language_code` VARCHAR(50),
    IN `p_country_code` VARCHAR(50),
    IN `p_currency_code` VARCHAR(50),
    IN `p_status` VARCHAR(20),
    IN `p_message` LONGTEXT,
    IN `p_show_at_login` TINYINT(1),
    IN `p_last_update_by` VARCHAR(100)
)
BEGIN
    INSERT INTO `welcome_message_template` (
        `subject`, `brand_id`, `language_code`, `country_code`, `currency_code`, 
        `status`, `message`, `show_at_login`, `last_update_by`, 
        `created_added`, `last_update_date`
    )
    VALUES (
        p_subject, p_brand_id, p_language_code, p_country_code, p_currency_code, 
        p_status, p_message, p_show_at_login, p_last_update_by, 
        CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6)
    );
    
    -- Return the newly created record's ID
    SELECT LAST_INSERT_ID() AS `id`;
END //

-- 2. Update Stored Procedure
DROP PROCEDURE IF EXISTS `sp_update_welcome_message_template` //
CREATE PROCEDURE `sp_update_welcome_message_template`(
    IN `p_id` INT,
    IN `p_subject` VARCHAR(255),
    IN `p_brand_id` INT,
    IN `p_language_code` VARCHAR(50),
    IN `p_country_code` VARCHAR(50),
    IN `p_currency_code` VARCHAR(50),
    IN `p_status` VARCHAR(20),
    IN `p_message` LONGTEXT,
    IN `p_show_at_login` TINYINT(1),
    IN `p_last_update_by` VARCHAR(100)
)
BEGIN
    UPDATE `welcome_message_template`
    SET `subject` = p_subject,
        `brand_id` = p_brand_id,
        `language_code` = p_language_code,
        `country_code` = p_country_code,
        `currency_code` = p_currency_code,
        `status` = p_status,
        `message` = p_message,
        `show_at_login` = p_show_at_login,
        `last_update_by` = p_last_update_by,
        `last_update_date` = CURRENT_TIMESTAMP(6)
    WHERE `id` = p_id;
END //

-- 3. Delete Stored Procedure
DROP PROCEDURE IF EXISTS `sp_delete_welcome_message_template` //
CREATE PROCEDURE `sp_delete_welcome_message_template`(
    IN `p_id` INT
)
BEGIN
    DELETE FROM `welcome_message_template` WHERE `id` = p_id;
END //

-- 4. Get by ID Stored Procedure (with Joins)
DROP PROCEDURE IF EXISTS `sp_get_welcome_message_template_by_id` //
CREATE PROCEDURE `sp_get_welcome_message_template_by_id`(
    IN `p_id` INT
)
BEGIN
    SELECT 
        w.`id`, w.`subject`, w.`brand_id`, 
        w.`language_code`, l.`name` AS `language_name`,
        w.`country_code`, cn.`name` AS `country_name`,
        w.`currency_code`, cr.`name` AS `currency_name`,
        w.`status`, w.`message`, w.`show_at_login`, w.`created_added`, 
        w.`last_update_by`, w.`last_update_date`
    FROM `welcome_message_template` w
    LEFT JOIN `language` l ON w.`language_code` = l.`language_code`
    LEFT JOIN `country` cn ON w.`country_code` = cn.`country_code`
    LEFT JOIN `currency` cr ON w.`currency_code` = cr.`currency_code`
    WHERE w.`id` = p_id;
END //

-- 5. Search Stored Procedure (with Joins)
DROP PROCEDURE IF EXISTS `sp_search_welcome_message_templates` //
CREATE PROCEDURE `sp_search_welcome_message_templates`(
    IN `p_subject` VARCHAR(255),
    IN `p_brand_id` INT,
    IN `p_language_code` VARCHAR(50),
    IN `p_country_code` VARCHAR(50),
    IN `p_currency_code` VARCHAR(50),
    IN `p_status` VARCHAR(20),
    IN `p_limit` INT,
    IN `p_offset` INT,
    OUT `p_total_records` INT
)
BEGIN
    -- Build base query conditions
    SET @sql_where = ' WHERE 1=1';
    
    IF p_subject IS NOT NULL AND p_subject <> '' THEN
        SET @sql_where = CONCAT(@sql_where, ' AND w.subject LIKE ', QUOTE(CONCAT('%', p_subject, '%')));
    END IF;
    
    IF p_brand_id IS NOT NULL AND p_brand_id <> 0 THEN
        SET @sql_where = CONCAT(@sql_where, ' AND w.brand_id = ', p_brand_id);
    END IF;
    
    IF p_language_code IS NOT NULL AND p_language_code <> '' AND p_language_code <> 'All' THEN
        SET @sql_where = CONCAT(@sql_where, ' AND w.language_code = ', QUOTE(p_language_code));
    END IF;
    
    IF p_country_code IS NOT NULL AND p_country_code <> '' AND p_country_code <> 'All' THEN
        SET @sql_where = CONCAT(@sql_where, ' AND w.country_code = ', QUOTE(p_country_code));
    END IF;
    
    IF p_currency_code IS NOT NULL AND p_currency_code <> '' AND p_currency_code <> 'All' THEN
        SET @sql_where = CONCAT(@sql_where, ' AND w.currency_code = ', QUOTE(p_currency_code));
    END IF;
    
    IF p_status IS NOT NULL AND p_status <> '' AND p_status <> 'All' THEN
        SET @sql_where = CONCAT(@sql_where, ' AND w.status = ', QUOTE(p_status));
    END IF;

    -- 1. Get total records without pagination
    SET @count_sql = CONCAT('SELECT COUNT(*) INTO @total_count FROM welcome_message_template w ', @sql_where);
    PREPARE stmt_count FROM @count_sql;
    EXECUTE stmt_count;
    DEALLOCATE PREPARE stmt_count;
    SET p_total_records = @total_count;

    -- 2. Get paginated records
    SET @data_sql = CONCAT('SELECT ',
                           'w.id, w.subject, w.brand_id, ',
                           'w.language_code, l.name AS language_name, ',
                           'w.country_code, cn.name AS country_name, ',
                           'w.currency_code, cr.name AS currency_name, ',
                           'w.status, w.message, w.show_at_login, w.created_added, ',
                           'w.last_update_by, w.last_update_date ',
                           'FROM welcome_message_template w ',
                           'LEFT JOIN language l ON w.language_code = l.language_code ',
                           'LEFT JOIN country cn ON w.country_code = cn.country_code ',
                           'LEFT JOIN currency cr ON w.currency_code = cr.currency_code ',
                           @sql_where,
                           ' ORDER BY w.last_update_date DESC');
                           
    IF p_limit IS NOT NULL AND p_limit > 0 THEN
        SET @data_sql = CONCAT(@data_sql, ' LIMIT ', p_limit);
        IF p_offset IS NOT NULL AND p_offset >= 0 THEN
            SET @data_sql = CONCAT(@data_sql, ' OFFSET ', p_offset);
        END IF;
    END IF;
    
    PREPARE stmt_data FROM @data_sql;
    EXECUTE stmt_data;
    DEALLOCATE PREPARE stmt_data;
END //

-- 6. Check Unique Combination Stored Procedure
DROP PROCEDURE IF EXISTS `sp_check_welcome_message_unique_combination` //
CREATE PROCEDURE `sp_check_welcome_message_unique_combination`(
    IN `p_id` INT,
    IN `p_language_code` VARCHAR(50),
    IN `p_country_code` VARCHAR(50),
    IN `p_currency_code` VARCHAR(50)
)
BEGIN
    SELECT NOT EXISTS (
        SELECT 1 
        FROM `welcome_message_template` 
        WHERE `language_code` = p_language_code 
          AND `country_code` = p_country_code 
          AND `currency_code` = p_currency_code
          AND (p_id IS NULL OR p_id = 0 OR `id` <> p_id)
    ) AS `is_unique`;
END //

DELIMITER ;
