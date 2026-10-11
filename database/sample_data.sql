USE smart_campus;

INSERT INTO users (name, email, password, role) VALUES
    ('Admin', 'admin@smartcampus.com', 'admin123', 'ADMIN'),
    ('Dr. Kumar', 'kumar@smartcampus.com', '1234', 'FACULTY'),
    ('Student User', 'student@smartcampus.com', '1234', 'STUDENT')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    password = VALUES(password),
    role = VALUES(role);

INSERT INTO resources (resource_name, resource_type, location, capacity, availability_status) VALUES
    ('Room 301', 'CLASSROOM', 'Block A', 60, 'AVAILABLE'),
    ('AI Lab', 'LAB', 'Block B', 40, 'AVAILABLE'),
    ('Seminar Hall A', 'SEMINAR_HALL', 'Main Block', 200, 'AVAILABLE')
ON DUPLICATE KEY UPDATE
    resource_type = VALUES(resource_type),
    location = VALUES(location),
    capacity = VALUES(capacity),
    availability_status = VALUES(availability_status);

INSERT INTO equipment (equipment_name, category, quantity, available_quantity, condition_status) VALUES
    ('Projector', 'Audio Visual', 5, 5, 'GOOD'),
    ('Laptop', 'Computing', 10, 8, 'GOOD'),
    ('Microphone', 'Audio', 6, 6, 'GOOD')
ON DUPLICATE KEY UPDATE
    category = VALUES(category),
    quantity = VALUES(quantity),
    available_quantity = VALUES(available_quantity),
    condition_status = VALUES(condition_status);
