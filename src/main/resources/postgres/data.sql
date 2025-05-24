-- Connect to the database
\c incident_db;

-- Insert sample users
INSERT INTO users (username, password, email, role) VALUES
('john_doe', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'john@example.com', 'CITIZEN'),
('alice_smith', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'alice@example.com', 'CITIZEN'),
('dispatch_dave', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'dave@example.com', 'DISPATCHER'),
('responder_rachel', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'rachel@example.com', 'RESPONDER'),
('bob_johnson', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'bob@example.com', 'CITIZEN');

-- Insert sample incidents
INSERT INTO incidents (description, status, incident_type, created_by, assigned_by, assigned_to) VALUES
('Pothole on Main Street', DEFAULT, 'OTHER', 1, NULL, NULL),
('Fallen tree blocking road', 'ASSIGNED', 'OTHER', 2, 3, 4),
('Power outage in downtown area', 'RESOLVED', 'OTHER', 5, 3, 4),
('Broken traffic light at 5th Ave', 'REPORTED', 'OTHER', 2, NULL, NULL),
('Flooding in park area', 'ASSIGNED', 'OTHER', 1, 3, 4);