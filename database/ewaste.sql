CREATE DATABASE IF NOT EXISTS ewaste_db;
USE ewaste_db;

-- 1. Collection Centers
CREATE TABLE IF NOT EXISTS collection_centers (
    center_id INT AUTO_INCREMENT PRIMARY KEY,
    center_name VARCHAR(100) NOT NULL,
    location VARCHAR(150) NOT NULL,
    contact VARCHAR(20) NOT NULL
);

-- 2. Users (Customers & Staff)
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    reward_points INT DEFAULT 0,
    center_id INT,
    FOREIGN KEY (center_id) REFERENCES collection_centers(center_id) ON DELETE SET NULL
);

-- 3. Waste Submissions
CREATE TABLE IF NOT EXISTS waste_submissions (
    waste_id VARCHAR(50) PRIMARY KEY,
    user_id INT NOT NULL,
    device_name VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    `condition` VARCHAR(50) NOT NULL,
    photo VARCHAR(255),
    center_id INT NOT NULL,
    status VARCHAR(50) DEFAULT 'Pending',
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    qr_path VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (center_id) REFERENCES collection_centers(center_id)
);

-- 4. Transportation Logs
CREATE TABLE IF NOT EXISTS transportation_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    waste_id VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    location VARCHAR(150) NOT NULL,
    FOREIGN KEY (waste_id) REFERENCES waste_submissions(waste_id) ON DELETE CASCADE
);

-- 5. Rewards
CREATE TABLE IF NOT EXISTS rewards (
    reward_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    waste_id VARCHAR(50) NOT NULL,
    points INT NOT NULL DEFAULT 100,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (waste_id) REFERENCES waste_submissions(waste_id) ON DELETE CASCADE
);

-- 6. Admins
CREATE TABLE IF NOT EXISTS admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Seed Collection Centers
INSERT INTO collection_centers (center_name, location, contact) VALUES
('Green Earth Recycling', 'Chennai', '+91 98765 43210'),
('Eco Waste Center', 'Tambaram', '+91 87654 32109'),
('E-Planet Recycling', 'Velachery', '+91 76543 21098')
ON DUPLICATE KEY UPDATE center_name=VALUES(center_name);

-- Seed Admin
INSERT INTO admins (username, password) VALUES
('admin', 'admin123')
ON DUPLICATE KEY UPDATE username=VALUES(username);

-- Seed Center Staff (registering as a user with a center_id)
-- Note: password is 'staff123' in plain text.
INSERT INTO users (name, email, phone, password, reward_points, center_id) VALUES
('Green Earth Staff', 'staff@greenearth.com', '+91 99999 88888', 'staff123', 0, 1)
ON DUPLICATE KEY UPDATE email=VALUES(email);
