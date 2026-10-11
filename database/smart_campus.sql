CREATE DATABASE IF NOT EXISTS smart_campus
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE smart_campus;

CREATE TABLE IF NOT EXISTS users (
    user_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'FACULTY', 'ADMIN') NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS resources (
    resource_id INT NOT NULL AUTO_INCREMENT,
    resource_name VARCHAR(100) NOT NULL,
    resource_type ENUM('CLASSROOM', 'LAB', 'SEMINAR_HALL', 'SPORTS_FACILITY') NOT NULL,
    location VARCHAR(150) DEFAULT NULL,
    capacity INT DEFAULT NULL,
    availability_status ENUM('AVAILABLE', 'MAINTENANCE') DEFAULT 'AVAILABLE',
    PRIMARY KEY (resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS events (
    event_id INT NOT NULL AUTO_INCREMENT,
    event_name VARCHAR(150) NOT NULL,
    description VARCHAR(500) DEFAULT NULL,
    event_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    organizer_id INT NOT NULL,
    PRIMARY KEY (event_id),
    KEY idx_events_organizer_id (organizer_id),
    CONSTRAINT fk_events_user FOREIGN KEY (organizer_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    resource_id INT NOT NULL,
    event_id INT DEFAULT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    booking_status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED', 'COMPLETED') DEFAULT 'PENDING',
    PRIMARY KEY (booking_id),
    KEY idx_bookings_user_id (user_id),
    KEY idx_bookings_resource_id (resource_id),
    KEY idx_bookings_event_id (event_id),
    KEY idx_bookings_status_date (booking_status, booking_date),
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_bookings_resource FOREIGN KEY (resource_id) REFERENCES resources(resource_id),
    CONSTRAINT fk_bookings_event FOREIGN KEY (event_id) REFERENCES events(event_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS equipment (
    equipment_id INT NOT NULL AUTO_INCREMENT,
    equipment_name VARCHAR(100) NOT NULL,
    category VARCHAR(100) DEFAULT NULL,
    quantity INT NOT NULL,
    available_quantity INT NOT NULL,
    condition_status ENUM('GOOD', 'DAMAGED', 'UNDER_MAINTENANCE') DEFAULT 'GOOD',
    PRIMARY KEY (equipment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS equipment_bookings (
    equipment_booking_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    equipment_id INT NOT NULL,
    quantity INT NOT NULL,
    issue_date DATE NOT NULL,
    return_date DATE DEFAULT NULL,
    booking_status ENUM('REQUESTED', 'APPROVED', 'RETURNED', 'CANCELLED') DEFAULT 'REQUESTED',
    PRIMARY KEY (equipment_booking_id),
    KEY idx_equipment_bookings_user_id (user_id),
    KEY idx_equipment_bookings_equipment_id (equipment_id),
    CONSTRAINT fk_equipment_bookings_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_equipment_bookings_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
