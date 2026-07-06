-- SQL Script to create welcome_message_template table and dependencies

CREATE TABLE IF NOT EXISTS `language` (
    `language_code` VARCHAR(50) PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `country` (
    `country_code` VARCHAR(50) PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `currency` (
    `currency_code` VARCHAR(50) PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `welcome_message_template` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `subject` VARCHAR(255) NOT NULL,
    `brand_id` INT NOT NULL DEFAULT 0,
    `language_code` VARCHAR(50) NOT NULL,
    `country_code` VARCHAR(50) NOT NULL,
    `currency_code` VARCHAR(50) NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `message` LONGTEXT NULL,
    `show_at_login` TINYINT(1) NOT NULL DEFAULT 1,
    `created_added` DATETIME(6) NOT NULL,
    `last_update_by` VARCHAR(100) NOT NULL,
    `last_update_date` DATETIME(6) NOT NULL,
    UNIQUE KEY `uq_lang_country_curr` (`language_code`, `country_code`, `currency_code`),
    FOREIGN KEY (`language_code`) REFERENCES `language` (`language_code`),
    FOREIGN KEY (`country_code`) REFERENCES `country` (`country_code`),
    FOREIGN KEY (`currency_code`) REFERENCES `currency` (`currency_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
