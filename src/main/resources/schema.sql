-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS taskmanagerapp
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

-- Use the created database
USE taskmanagerapp;

-- Create users table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  created_at DATETIME(6) DEFAULT NULL,
  fullname VARCHAR(255) DEFAULT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('ADMIN', 'USER') DEFAULT NULL,
  updated_at DATETIME(6) DEFAULT NULL,
  username VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL,
  email_verified BIT(1) NOT NULL
);

-- Create tasks table if it doesn't exist
CREATE TABLE IF NOT EXISTS tasks (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description VARCHAR(1000) DEFAULT NULL,
  completed BIT(1) NOT NULL,
  priority ENUM('HIGH', 'MEDIUM', 'LOW') DEFAULT NULL,
  due_date DATETIME(6) DEFAULT NULL,
  created_at DATETIME(6) DEFAULT NULL,
  updatedd_at DATETIME(6) DEFAULT NULL,
  user_id BIGINT NOT NULL,
  CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create email verification tokens table
CREATE TABLE IF NOT EXISTS email_verification_tokens (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  token VARCHAR(255) NOT NULL UNIQUE,
  user_id BIGINT NOT NULL,
  created_at DATETIME(6),
  expires_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_email_verification_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create password reset tokens table
CREATE TABLE IF NOT EXISTS password_reset_tokens (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  token VARCHAR(255) NOT NULL UNIQUE,
  user_id BIGINT NOT NULL,
  created_at DATETIME(6),
  expires_at DATETIME(6) NOT NULL,
  used BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);