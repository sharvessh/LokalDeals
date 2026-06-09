CREATE DATABASE IF NOT EXISTS LokalDeals;
USE LokalDeals;

-- 1. Create USER Table
CREATE TABLE USER (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_type ENUM('CONSUMER', 'BUSINESS') NOT NULL,
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Create BUSINESS Table
CREATE TABLE BUSINESS (
    business_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL,
    category VARCHAR(100) NOT NULL,
    contact VARCHAR(50) NOT NULL,
    owner_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES USER(user_id) ON DELETE CASCADE
);

-- 3. Create CATEGORY Table
CREATE TABLE CATEGORY (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- 4. Create DEAL Table
CREATE TABLE DEAL (
    deal_id INT AUTO_INCREMENT PRIMARY KEY,
    business_id INT NOT NULL,
    category_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    discount_percentage DECIMAL(5, 2) NOT NULL,
    expiry_date DATETIME NOT NULL,
    FOREIGN KEY (business_id) REFERENCES BUSINESS(business_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES CATEGORY(category_id) ON DELETE RESTRICT
);

-- 5. Create REVIEW Table
CREATE TABLE REVIEW (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    business_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER(user_id) ON DELETE CASCADE,
    FOREIGN KEY (business_id) REFERENCES BUSINESS(business_id) ON DELETE CASCADE
);

-- 6. Create SAVED_DEAL Table
CREATE TABLE SAVED_DEAL (
    user_id INT NOT NULL,
    deal_id INT NOT NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, deal_id),
    FOREIGN KEY (user_id) REFERENCES USER(user_id) ON DELETE CASCADE,
    FOREIGN KEY (deal_id) REFERENCES DEAL(deal_id) ON DELETE CASCADE
);

-- Seed core category strings directly into the dictionary table
INSERT INTO CATEGORY (name) VALUES ('Groceries'), ('F&B'), ('Pharmacy'), ('Electronics');