-- Create database if not exists
CREATE DATABASE IF NOT EXISTS firs_db;
USE firs_db;
-- ------------------------------------------------------------
-- 1. USERS TABLE
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50)
);
-- Insert default demo users (skip if email already exists)
INSERT INTO users (name, email, password, role)
SELECT *
FROM (
        SELECT 'Jason M.' AS name,
            'customer@firs.com' AS email,
            'customer123' AS password,
            'customer' AS role
        UNION ALL
        SELECT 'Lt. James Holloway',
            'gov@firs.com',
            'gov123',
            'govt'
        UNION ALL
        SELECT 'Ridge Arms LLC',
            'dealer@firs.com',
            'dealer123',
            'dealer'
        UNION ALL
        SELECT 'SuperAdmin',
            'admin@firs.com',
            'admin123',
            'admin'
    ) AS tmp
WHERE NOT EXISTS (
        SELECT 1
        FROM users
        WHERE users.email = tmp.email
    );
-- ------------------------------------------------------------
-- 2. PRODUCTS TABLE
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    brand VARCHAR(255),
    category VARCHAR(50),
    price DOUBLE
);
-- Insert product catalog (skip if name already exists)
INSERT INTO products (name, brand, category, price)
SELECT *
FROM (
        SELECT 'Glock 19 Gen5' AS name,
            'Glock' AS brand,
            'Handgun' AS category,
            649 AS price
        UNION ALL
        SELECT 'Sig Sauer P320',
            'Sig Sauer',
            'Handgun',
            629
        UNION ALL
        SELECT 'M1911',
            'Colt',
            'Handgun',
            549
        UNION ALL
        SELECT 'CZ P-10C',
            'CZ',
            'Handgun',
            499
        UNION ALL
        SELECT 'Glock 17 Gen5',
            'Glock',
            'Handgun',
            599
        UNION ALL
        SELECT 'Sig Sauer P365',
            'Sig Sauer',
            'Handgun',
            629
        UNION ALL
        SELECT 'S&W M&P Shield',
            'Smith & Wesson',
            'Handgun',
            479
        UNION ALL
        SELECT 'CZ P-07',
            'CZ',
            'Handgun',
            529
        UNION ALL
        SELECT 'Ruger GP100',
            'Ruger',
            'Revolver',
            749
        UNION ALL
        SELECT 'Colt Python',
            'Colt',
            'Revolver',
            1499
        UNION ALL
        SELECT 'Taurus 856',
            'Taurus',
            'Revolver',
            349
        UNION ALL
        SELECT 'Charter Arms Bulldog',
            'Charter Arms',
            'Revolver',
            399
        UNION ALL
        SELECT 'S&W 686+',
            'Smith & Wesson',
            'Revolver',
            829
        UNION ALL
        SELECT 'Ruger SP101',
            'Ruger',
            'Revolver',
            679
        UNION ALL
        SELECT 'AR-15 Platform',
            'Colt',
            'Rifle',
            1299
        UNION ALL
        SELECT 'M4 Carbine',
            'Colt',
            'Rifle',
            1499
        UNION ALL
        SELECT 'AK-47',
            'Kalashnikov',
            'Rifle',
            899
        UNION ALL
        SELECT 'Daniel Defense DDM4 V7',
            'Daniel Defense',
            'Rifle',
            2099
        UNION ALL
        SELECT 'FN SCAR 17S',
            'FN Herstal',
            'Rifle',
            3499
        UNION ALL
        SELECT 'Barrett MRAD Mk22',
            'Barrett',
            'Sniper',
            5999
        UNION ALL
        SELECT 'Remington 700',
            'Remington',
            'Sniper',
            899
        UNION ALL
        SELECT 'Accuracy International AT',
            'Accuracy International',
            'Sniper',
            4799
        UNION ALL
        SELECT 'Savage 110 Elite',
            'Savage',
            'Sniper',
            1699
    ) AS tmp
WHERE NOT EXISTS (
        SELECT 1
        FROM products
        WHERE products.name = tmp.name
    );
-- ------------------------------------------------------------
-- 3. ORDERS TABLE
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount DOUBLE,
    status VARCHAR(50),
    order_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
-- ------------------------------------------------------------
-- 4. ORDER_ITEMS TABLE
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT,
    price DOUBLE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);